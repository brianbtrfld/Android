// WeatherViewerActivity.java
// Main Activity for the Weather Viewer app.
package net.briangbutterfield.weatherappsimple;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.deitel.weatherviewer.R;

public class MainActivity extends Activity
{
	public static final String PREFERRED_CITY_NAME_KEY = "preferred_city_name";
	public static final String PREFERRED_CITY_ZIPCODE_KEY = "preferred_city_zipcode";
	public static final String SHARED_PREFERENCES_NAME = "weather_viewer_shared_preferences";

	private String lastSelectedCity;
	private SharedPreferences weatherSharedPreferences;

	private Map<String, String> favoriteCitiesMap;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		favoriteCitiesMap = new HashMap<String, String>();

		weatherSharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceStateBundle)
	{
		super.onSaveInstanceState(savedInstanceStateBundle);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceStateBundle)
	{
		super.onRestoreInstanceState(savedInstanceStateBundle);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();

		if (favoriteCitiesMap.isEmpty())
		{
			loadSavedCities();
		}

		if (favoriteCitiesMap.isEmpty())
		{
			addSampleCities();
		}

		loadSelectedForecast();
	}

	private void loadSelectedForecast()
	{
		if (lastSelectedCity != null)
		{
			selectForecast(lastSelectedCity);
		} 
		else
		{
			String cityNameString = weatherSharedPreferences.getString(PREFERRED_CITY_NAME_KEY, "57702");
			selectForecast(cityNameString); 
		}
	}

	public void setPreferred(String cityNameString)
	{
		String cityZipcodeString = favoriteCitiesMap.get(cityNameString);
		Editor preferredCityEditor = weatherSharedPreferences.edit();

		preferredCityEditor.putString(PREFERRED_CITY_NAME_KEY, cityNameString);
		preferredCityEditor.putString(PREFERRED_CITY_ZIPCODE_KEY, cityZipcodeString);
		preferredCityEditor.apply();
		
		lastSelectedCity = null;
		loadSelectedForecast();

	}

	private void loadSavedCities()
	{
		Map<String, ?> citiesMap = weatherSharedPreferences.getAll();

		for (String cityString : citiesMap.keySet())
		{
			if (!(cityString.equals(PREFERRED_CITY_NAME_KEY) || cityString.equals(PREFERRED_CITY_ZIPCODE_KEY)))
			{
				addCity(cityString, (String) citiesMap.get(cityString), false);
			}
		}
	}

	private void addSampleCities()
	{
		String[] sampleCityNamesArray = getResources().getStringArray(R.array.default_city_names);
		String[] sampleCityZipcodesArray = getResources().getStringArray(R.array.default_city_zipcodes);

		for (int i = 0; i < sampleCityNamesArray.length; i++)
		{
			if (i == 0)
			{
				setPreferred(sampleCityNamesArray[i]);
			}

			addCity(sampleCityNamesArray[i], sampleCityZipcodesArray[i], false);
		}
	}

	public void addCity(String city, String zipcode, boolean select)
	{
		favoriteCitiesMap.put(city, zipcode);
		
		Editor preferenceEditor = weatherSharedPreferences.edit();
		preferenceEditor.putString(city, zipcode);
		preferenceEditor.apply();
	}

	public void selectForecast(String name)
	{
		lastSelectedCity = name;
		String zipcodeString = favoriteCitiesMap.get(name);
		if (zipcodeString == null)
		{
			return;
		}

		// get the current visible ForecastFragment
		FragmentForecast currentForecastFragment = (FragmentForecast) getFragmentManager().findFragmentById(R.id.fragmentFrameLayout);

		if (currentForecastFragment == null || !currentForecastFragment.getZipcode().equals(zipcodeString))
		{
			
			currentForecastFragment = new FragmentForecast();
			
			Bundle argumentsBundle = new Bundle();
			argumentsBundle.putString(FragmentForecast.LOCATION_KEY, zipcodeString);
			currentForecastFragment.setArguments(argumentsBundle);

			FragmentTransaction forecastFragmentTransaction = getFragmentManager().beginTransaction();

			forecastFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			forecastFragmentTransaction.replace(R.id.fragmentFrameLayout, currentForecastFragment);

			forecastFragmentTransaction.commit();
		}
	}
}