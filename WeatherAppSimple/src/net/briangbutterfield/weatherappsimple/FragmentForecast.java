// SingleForecastFragment.java
// Displays forecast information for a single city.
package net.briangbutterfield.weatherappsimple;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentForecast extends Fragment
{
	public static final String LOCATION_KEY = "key_location";
	public static final String FORECAST_KEY = "key_forecast";

	private ForecastLocation _location = new ForecastLocation();
	private Forecast _forecast = new Forecast();
	
	private View _forecastView;
	private View _progressView;
	
	private TextView _textViewLocation;
	private TextView _textViewTemp;
	private TextView _textViewFeelsLike;
	private TextView _textViewHumidity;
	private TextView _textViewChanceOfPrecip;
	private ImageView _imageViewForecast;
	
	public FragmentForecast()
	{
	}

	@Override
	public void onCreate(Bundle argumentsBundle)
	{
		super.onCreate(argumentsBundle);
		
		_location.new LoadLocation(new AsynTaskListener()).execute(getArguments().getString(LOCATION_KEY));
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceStateBundle)
	{
		super.onSaveInstanceState(savedInstanceStateBundle);
		
		savedInstanceStateBundle.putParcelable(LOCATION_KEY, _location);
		savedInstanceStateBundle.putParcelable(FORECAST_KEY, _forecast);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_forecast, null);

		_forecastView = rootView.findViewById(R.id.scrollView);
		_progressView = rootView.findViewById(R.id.layoutProgress);
		
		_textViewLocation = (TextView) rootView.findViewById(R.id.textViewLocation);
		_textViewTemp = (TextView) rootView.findViewById(R.id.textViewTemp);
		_textViewFeelsLike = (TextView) rootView.findViewById(R.id.textViewFeelsLikeTemp);
		_textViewHumidity = (TextView) rootView.findViewById(R.id.textViewHumidity);
		_textViewChanceOfPrecip = (TextView) rootView.findViewById(R.id.textViewChanceOfPrecip);
		_imageViewForecast = (ImageView) rootView.findViewById(R.id.imageForecast);
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceStateBundle)
	{
		super.onActivityCreated(savedInstanceStateBundle);

		if (savedInstanceStateBundle == null)
		{
			_forecastView.setVisibility(View.GONE);
			_progressView.setVisibility(View.VISIBLE);
		}
		else
		{
			_location = savedInstanceStateBundle.getParcelable(LOCATION_KEY);
			_forecast = savedInstanceStateBundle.getParcelable(FORECAST_KEY);
			updateView();
		}
	}

	private void updateView()
	{
		if (_location.City != null)
		{
			_textViewLocation.setText(_location.City + " " + _location.State + ", " + _location.ZipCode + " " + _location.Country);
		}
		
		if (_forecast.Temp != null)
		{
			_textViewTemp.setText(_forecast.Temp + (char) 0x00B0 + "F");
			_textViewFeelsLike.setText(_forecast.FeelsLikeTemp + (char) 0x00B0 + "F");
			_textViewHumidity.setText(_forecast.Humidity + (char) 0x0025);
			_textViewChanceOfPrecip.setText(_forecast.ChanceOfPrecipitation + (char) 0x0025);
			
			if (_forecast.Image != null);
			{
				_imageViewForecast.setImageBitmap(_forecast.Image);
			}
		}
			
		_progressView.setVisibility(View.GONE);
		_forecastView.setVisibility(View.VISIBLE);
		
	}
	
	private class AsynTaskListener implements IListeners
	{
		@Override
		public void onLocationLoaded(ForecastLocation location)
		{
			
			_location = location;
			
			if (_location == null)
			{
				Toast.makeText(getActivity(), 
						       getResources().getString(R.string.null_data_toast), 
						       Toast.LENGTH_LONG).show();
				return;
			}

			updateView();
			
			_forecast.new LoadForecast(new AsynTaskListener()).execute(_location.ZipCode);
		}

		@Override
		public void onForecastLoaded(Forecast forecast)
		{
			
			_forecast = forecast;
			
			if (_forecast == null)
			{
				Toast.makeText(getActivity(), 
						       getResources().getString(R.string.null_data_toast), 
						       Toast.LENGTH_LONG).show();
				return;
			}
			
			updateView();
		}
	}
}