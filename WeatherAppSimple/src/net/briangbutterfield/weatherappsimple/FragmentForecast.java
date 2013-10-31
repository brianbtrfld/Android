package net.briangbutterfield.weatherappsimple;

import net.briangbutterfield.weatherappsimple.Forecast.LoadForecast;
import net.briangbutterfield.weatherappsimple.ForecastLocation.LoadLocation;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
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

	private ForecastLocation _location = null;
	private LoadLocation _loadLocation = null;
	private Forecast _forecast = null;
	private LoadForecast _loadForecast = null;

	private View _forecastView;
	private View _progressView;

	private TextView _textViewLocation;
	private TextView _textViewTemp;
	private TextView _textViewFeelsLike;
	private TextView _textViewHumidity;
	private TextView _textViewChanceOfPrecip;
	private TextView _textViewAsOfTime;
	private ImageView _imageViewForecast;

	private class AsynTaskListener implements IListeners
	{
		// Provides implementation for the callbacks from the
		// associated AsyncTask.
		@Override
		public void onLocationLoaded(ForecastLocation location)
		{
	
			_location = location;
	
			if (_location == null)
			{
				Toast.makeText(getActivity(),
						       getResources().getString(R.string.toastNullData),
						       Toast.LENGTH_LONG).show();
	
				_progressView.setVisibility(View.GONE);
				_forecastView.setVisibility(View.GONE);
	
				return;
			}
	
			updateView();
		}
	
		@Override
		public void onForecastLoaded(Forecast forecast)
		{
			_forecast = forecast;
	
			if (_forecast == null)
			{
				Toast.makeText(getActivity(),
							   getResources().getString(R.string.toastNullData),
							   Toast.LENGTH_LONG).show();
	
				_progressView.setVisibility(View.GONE);
				_forecastView.setVisibility(View.GONE);
	
				return;
			}
	
			updateView();
		}
	}

	@Override
	public void onCreate(Bundle argumentsBundle)
	{
		super.onCreate(argumentsBundle);

		if (_location == null)
		{
			_location = new ForecastLocation();
		}
		_loadLocation = _location.new LoadLocation(new AsynTaskListener());
		
		// Using executeOnExecutor allows for the AsyncTask(s) to execute in parallel
		// still keeping with the AsyncTask thread pool limit.
		//			_loadLocation.execute(getArguments().getString(LOCATION_KEY));
		_loadLocation.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getArguments().getString(LOCATION_KEY));
		
		if (_forecast == null)
		{
			_forecast = new Forecast();
		}
		
		_loadForecast = _forecast.new LoadForecast(getActivity(), new AsynTaskListener());
		
		// Using executeOnExecutor allows for the AsyncTask(s) to execute in parallel
		// still keeping with the AsyncTask thread pool limit.
		//			_loadForecast.execute(getArguments().getString(LOCATION_KEY));
		_loadForecast.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getArguments().getString(LOCATION_KEY));
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceStateBundle)
	{
		super.onSaveInstanceState(savedInstanceStateBundle);

		// Pull the parcelables out of the bundle.
		if (_location != null && _forecast != null)
		{
			savedInstanceStateBundle.putParcelable(LOCATION_KEY, _location);
			savedInstanceStateBundle.putParcelable(FORECAST_KEY, _forecast);
		}
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
		_textViewAsOfTime = (TextView) rootView.findViewById(R.id.textViewAsOfTime);
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

	@Override
	public void onDestroy()
	{
		if (_loadLocation != null && _loadLocation.getStatus() == Status.RUNNING)
		{
			_loadLocation.cancel(true);
			_loadLocation = null;
		}
		
		if (_loadForecast != null && _loadForecast.getStatus() == Status.RUNNING)
		{
			_loadForecast.cancel(true);
			_loadForecast = null;
		}
		
		super.onDestroy();
	}

	private void updateView()
	{
		if (_location != null && _location.City != null)
		{
			_textViewLocation.setText(_location.City + " " + _location.State);
		}

		if (_forecast != null && _forecast.Temp != null)
		{
			_textViewTemp.setText(_forecast.Temp + (char) 0x00B0 + "F");
			_textViewFeelsLike.setText(_forecast.FeelsLikeTemp + (char) 0x00B0 + "F");
			_textViewHumidity.setText(_forecast.Humidity + (char) 0x0025);
			_textViewChanceOfPrecip.setText(_forecast.ChanceOfPrecipitation + (char) 0x0025);
			_textViewAsOfTime.setText(_forecast.AsOfTime);

			if (_forecast.Image != null)
			{
				_imageViewForecast.setImageBitmap(_forecast.Image);
			}
		}

		_progressView.setVisibility(View.GONE);
		_forecastView.setVisibility(View.VISIBLE);

	}
}