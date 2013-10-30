package net.briangbutterfield.weatherappsimple;


public interface IListeners
{
	public void onLocationLoaded(ForecastLocation forecastLocation);
	public void onForecastLoaded(Forecast forecast);
}
