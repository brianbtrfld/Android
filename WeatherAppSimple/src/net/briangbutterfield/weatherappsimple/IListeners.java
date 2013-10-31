package net.briangbutterfield.weatherappsimple;

import net.briangbutterfield.weatherappsimple.model.Forecast;
import net.briangbutterfield.weatherappsimple.model.ForecastLocation;


public interface IListeners
{
	public void onLocationLoaded(ForecastLocation forecastLocation);
	public void onForecastLoaded(Forecast forecast);
}
