package net.briangbutterfield.weatherappsimple;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.Log;

public class Forecast implements Parcelable
{

	// http://developer.weatherbug.com/docs/read/WeatherBug_API_JSON

	public Forecast()
	{
		Temp = null;
		FeelsLikeTemp = null;
		Humidity = null;
		ChanceOfPrecipitation = null;
		Image = null;
	}

	public Forecast(Parcel parcel)
	{
		Temp = parcel.readString();
		FeelsLikeTemp = parcel.readString();
		Humidity = parcel.readString();
		ChanceOfPrecipitation = parcel.readString();

		byte[] data = null;
		parcel.readByteArray(data);
		Image = convertByteArrayToBitmap(data);
	}

	public String Temp;
	public String FeelsLikeTemp;
	public String Humidity;
	public String ChanceOfPrecipitation;
	public Bitmap Image;

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(Temp);
		dest.writeString(FeelsLikeTemp);
		dest.writeString(Humidity);
		dest.writeString(ChanceOfPrecipitation);
		dest.writeByteArray(convertBitmapToByteArray(Image));
	}

	public static final Parcelable.Creator<Forecast> Creator = new Parcelable.Creator<Forecast>()
	{
		public Forecast createFromParcel(Parcel pc)
		{
			return new Forecast(pc);
		}

		public Forecast[] newArray(int size)
		{
			return new Forecast[size];
		}
	};

	public class LoadForecast extends AsyncTask<String, Void, Forecast>
	{
		private static final String TAG = "ReadForecastTask.java";
		private IListeners _listener;

		private int bitmapSampleSize = -1;

		public LoadForecast(IListeners listener)
		{
			_listener = listener;
		}

		public void setSampleSize(int sampleSize)
		{
			this.bitmapSampleSize = sampleSize;
		}

		protected Forecast doInBackground(String... params)
		{
			Forecast forecast = null;

			try
			{
				URL webServiceURL = new URL("http://i.wxbug.net/REST/Direct/GetForecastHourly.ashx?zip="
						+ params[0] + "&ht=t&ht=i&ht=cp&ht=fl&ht=h&api_key=q3wj56tqghv7ybd8dy6gg4e7");

				Reader forecastReader = new InputStreamReader(webServiceURL.openStream());

				JsonReader forecastJsonReader = new JsonReader(forecastReader);

				forecastJsonReader.beginObject();

				String name = forecastJsonReader.nextName();

				if (name.equals("forecastHourlyList") == true)
				{
					forecast = readForecast(forecastJsonReader);
				}

				forecastJsonReader.close();

			}
			catch (MalformedURLException e)
			{
				Log.e(TAG, e.toString());
			}
			catch (IOException e)
			{
				Log.e(TAG, e.toString());
			}
			catch (IllegalStateException e)
			{
				Log.e(TAG, e.toString() + params[0]);
			}
			catch (Exception e)
			{
				Log.e(TAG, e.toString());
			}

			return forecast;
		}

		protected void onPostExecute(Forecast forecast)
		{
			_listener.onForecastLoaded(forecast);
		}

		private Bitmap getIconBitmap(String conditionString, int bitmapSampleSize)
		{
			Bitmap iconBitmap = null;
			try
			{
				URL weatherURL = new URL("http://img.weather.weatherbug.com/forecast/icons/localized/500x420/en/trans/"
						+ conditionString + ".png");

				BitmapFactory.Options options = new BitmapFactory.Options();
				if (bitmapSampleSize != -1)
				{
					options.inSampleSize = bitmapSampleSize;
				}

				iconBitmap = BitmapFactory.decodeStream(weatherURL.openStream(), null, options);
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

			return iconBitmap;
		}

		private Forecast readForecast(JsonReader reader)
		{
			Forecast forecast = new Forecast();

			try
			{
				reader.beginArray();
				reader.beginObject();

				while (reader.hasNext())
				{
					String name = reader.nextName();

					if (name.equals("temperature") == true)
					{
						forecast.Temp = reader.nextString();
					}
					else if (name.equals("feelsLike") == true)
					{
						forecast.FeelsLikeTemp = reader.nextString();
					}
					else if (name.equals("humidity") == true)
					{
						forecast.Humidity = reader.nextString();
					}
					else if (name.equals("chancePrecip") == true)
					{
						forecast.ChanceOfPrecipitation = reader.nextString();
					}
					else if (name.equals("icon") == true)
					{
						forecast.Image = getIconBitmap(reader.nextString(), bitmapSampleSize);
					}
					else
					{
						reader.skipValue();
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
			return forecast;
		}
	}

	private byte[] convertBitmapToByteArray(Bitmap image)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		try
		{
			stream.close();
		}
		catch (IOException e)
		{
		}
		finally
		{
			stream = null;
			byteArray = null;
		}

		return byteArray;
	}

	private Bitmap convertByteArrayToBitmap(byte[] data)
	{
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		return bitmap;
	}

}
