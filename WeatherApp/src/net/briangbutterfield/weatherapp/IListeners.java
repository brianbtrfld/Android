package net.briangbutterfield.weatherapp;

import android.graphics.Bitmap;

public interface IListeners
{
	public void onLocationLoaded(ForecastLocation forecastLocation);
	public void onForecastLoaded(Bitmap image, String temperature, String feelsLike, String humidity, String precipitation);
}
