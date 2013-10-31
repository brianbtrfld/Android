package net.briangbutterfield.weatherappsimple;

import net.briangbutterfield.weatherappsimple.view.FragmentForecast;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;

public class MainActivity extends Activity
{
	private String[] _citiesArray;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get City array from resources.
		_citiesArray = getResources().getStringArray(R.array.cityArray);

		// By default, first element is "favorite" city, go get location.
		// TextUtils.split() takes a regular expression and in the case
		// of a pipe delimiter, it needs to be escaped.
		showForecast(TextUtils.split(_citiesArray[0], "\\|")[0]);
	}

	private void showForecast(String zipCode)
	{
		//@formatter:off
		
		FragmentForecast currentForecastFragment = (FragmentForecast) getFragmentManager().findFragmentById(R.id.fragmentFrameLayout);
		
		if (currentForecastFragment == null)
		{
			currentForecastFragment = new FragmentForecast();
			
			Bundle bundle = new Bundle();
			bundle.putString(FragmentForecast.LOCATION_KEY, zipCode);
			currentForecastFragment.setArguments(bundle);
			
			getFragmentManager().beginTransaction()
								.replace(R.id.fragmentFrameLayout, currentForecastFragment)
			 					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			 					.commit();
		}
		
		//@formatter:on
	}
}