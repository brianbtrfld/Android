package net.briangbutterfield.training.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener, TextWatcher, OnSharedPreferenceChangeListener 
{
	private static final String TAG = "StatusActivity";
	
	TextView textCount;
	EditText editText;
	Button updateButton;
	Twitter twitter;

	//testing github
	
	SharedPreferences prefs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //inflates all XML into java code.
        setContentView(R.layout.status);
        
        //setup preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //connects up to the OnSharedPreferenceChangeListener interface
        //which supports listening to when the user changes the shared
        //preferences.
        prefs.registerOnSharedPreferenceChangeListener(this);
        
        //find views
        textCount = (TextView) findViewById(R.id.textCount);
        editText = (EditText) findViewById(R.id.editText);
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        
        //set the button click listener
        updateButton.setOnClickListener(this);
        
        //set the initial value for text count field to 140
        //and set the text color to GREEN.
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        
        //associate TextWatcher listener with this object.
        editText.addTextChangedListener(this);
    }
    
    private Twitter getTwitter()
    {
    	if (twitter == null)
    	{
    		String username, password, apiRoot;
    		
    		username = prefs.getString("username", "");
    		password = prefs.getString("password", "");
    		apiRoot = prefs.getString("apiroot", "");
    		
    		twitter = new Twitter(username, password);
            twitter.setAPIRootUrl(apiRoot);
    	}
    	
    	return twitter;
    }
    
    class PostToTwitter extends AsyncTask<String, Integer, String>
    {

		@Override
		protected String doInBackground(String... statuses) 
		{
			try
			{
				Twitter.Status status = getTwitter().updateStatus(statuses[0]);
			    return status.text;
			}
			catch (TwitterException e)
			{
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return "Failed to post";
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
			//not used in this case
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			//Called when the task is complete.
			//  parameter [result] is the return value from the doInBackground() method.
			//  displays a message to the user for a specified duration.
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
    	
    }

	public void onClick(View v) 
	{
		//get the status update text from the edit text box.
		String status = editText.getText().toString();
		
		//execute async thread to call twitter api.
		new PostToTwitter().execute(status);
		
		Log.d(TAG, "onClicked");
	}

	public void afterTextChanged(Editable statusText) 
	{
	
		int count = 140 - statusText.length();
		textCount.setText(Integer.toString(count));
		textCount.setTextColor(Color.GREEN);
		
		if (count < 10)
			textCount.setTextColor(Color.YELLOW);
		
		if (count < 0)
			textCount.setTextColor(Color.RED);
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) 
	{
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		
		inflater.inflate(R.menu.menu_main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		switch (item.getItemId())
		{
			case R.id.itemPrefs:
				startActivity(new Intent(this, PrefsActivity.class));
				break;
		}
		return true;
	}

	/**
	 * OnSharedPreferenceChangeListener 
	 * method implementations */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{
		//invalidate the twitter object so getTwitter() will establish new
		//connection and credentials to the URL.
		twitter = null;
	}
}