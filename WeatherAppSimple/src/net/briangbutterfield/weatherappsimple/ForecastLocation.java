package net.briangbutterfield.weatherappsimple;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.Log;

public class ForecastLocation implements Parcelable
{

	private static final String TAG = "Weather:ForecastLocation";
	
	//@formatter:off
	private String _URL = "http://i.wxbug.net/REST/Direct/GetLocation.ashx?zip=" + "%s" + 
			             "&api_key=q3wj56tqghv7ybd8dy6gg4e7";
	
	//@formatter:on

	public ForecastLocation()
	{
		ZipCode = null;
		City = null;
		State = null;
		Country = null;
		CurrentForecast = null;
	}

	public ForecastLocation(Parcel parcel)
	{
		ZipCode = parcel.readString();
		City = parcel.readString();
		State = parcel.readString();
		Country = parcel.readString();
		CurrentForecast = parcel.readParcelable(null);
	}

	public String ZipCode;
	public String City;
	public String State;
	public String Country;
	public Forecast CurrentForecast;

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{

		dest.writeString(ZipCode);
		dest.writeString(City);
		dest.writeString(State);
		dest.writeString(Country);

		dest.writeParcelable(CurrentForecast, 0);

	}

	public static final Parcelable.Creator<ForecastLocation> Creator = new Parcelable.Creator<ForecastLocation>()
	{
		public ForecastLocation createFromParcel(Parcel pc)
		{
			return new ForecastLocation(pc);
		}

		public ForecastLocation[] newArray(int size)
		{
			return new ForecastLocation[size];
		}
	};

	public class LoadLocation extends AsyncTask<String, Void, ForecastLocation>
	{
		private IListeners _listener;

		public LoadLocation(IListeners listener)
		{
			_listener = listener;
		}

		@Override
		protected ForecastLocation doInBackground(String... params)
		{
			ForecastLocation forecastLocation = null;

			try
			{
				if (params.length == 1 && params[0] != null)
				{
					// Since there is a zip code passed to the task, build
					// the URL.
					URL url = new URL(String.format(_URL, params[0]));
					
					// Open stream and assign to JsonReader.
					// NOTE:  This is a "bulk" read, not buffered.
					Reader streamReader = new InputStreamReader(url.openStream());
					JsonReader jsonReader = new JsonReader(streamReader);
					
					// Parse the JSON-based stream.
					forecastLocation = readJSON(jsonReader);
					
					// Always close a stream.
					jsonReader.close();
					streamReader.close();
				}
			}
			catch (MalformedURLException e)
			{
				Log.e(TAG, e.toString());
			}
			catch (IOException e)
			{
				Log.e(TAG, e.toString());
			}
			catch (Exception e)
			{
				Log.e(TAG, e.toString());
			}
			return forecastLocation;
		}

		protected void onPostExecute(ForecastLocation forecastLocation)
		{
			_listener.onLocationLoaded(forecastLocation);
		}

		private ForecastLocation readJSON(JsonReader jsonReader) throws IOException
		{

			ForecastLocation forecastLocation = new ForecastLocation();

			try
			{
				jsonReader.beginObject();

				String name = jsonReader.nextName();

				if (name.equals("location") == true)
				{
					jsonReader.beginObject();

					while (jsonReader.hasNext())
					{
						name = jsonReader.nextName();

						if (name.equals("city") == true)
						{
							forecastLocation.City = jsonReader.nextString();
						}
						else if (name.equals("state") == true)
						{
							forecastLocation.State = jsonReader.nextString();
						}
						else if (name.equals("country") == true)
						{
							forecastLocation.Country = jsonReader.nextString();
						}
						else if (name.equals("zipCode") == true)
						{
							forecastLocation.ZipCode = jsonReader.nextString();
						}
						else
						{
							jsonReader.skipValue();
						}
					}
				}
			}
			catch (IOException e)
			{
				Log.e(TAG, e.toString());
			}
			catch (Exception e)
			{
				Log.e(TAG, e.toString());
			}

			return forecastLocation;
		}
	}

}
