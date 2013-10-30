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
		private static final String TAG = "ReadLocatonTask.java";

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

				URL url = new URL("http://i.wxbug.net/REST/Direct/GetLocation.ashx?zip=" + params[0]
						+ "&api_key=q3wj56tqghv7ybd8dy6gg4e7");

				Reader streamReader = new InputStreamReader(url.openStream());

				JsonReader jsonReader = new JsonReader(streamReader);

				forecastLocation = readJSON(jsonReader);

				jsonReader.close();
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

					String nextNameString;

					while (jsonReader.hasNext())
					{
						nextNameString = jsonReader.nextName();

						if (nextNameString.equals("city") == true)
						{
							forecastLocation.City = jsonReader.nextString();
						}
						else if (nextNameString.equals("state") == true)
						{
							forecastLocation.State = jsonReader.nextString();
						}
						else if (nextNameString.equals("country") == true)
						{
							forecastLocation.Country = jsonReader.nextString();
						}
						else if (nextNameString.equals("zipCode") == true)
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
