package net.briangbutterfield.activitylifecycleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Activity1 extends Activity implements OnClickListener
{

	//NOTES:
	//  Lifecycle Documentation [http://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle]
	//  Emulator Rotation [Win = Left Ctrl + F12 OR Mac = fn + Left Ctrl + F12]
	
	private String _message = "";
	private boolean _stateBoolean = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity1);
		
		Button buttonStartActivity2 = (Button) findViewById(R.id.buttonStartActivity2);
		buttonStartActivity2.setOnClickListener(this);
		
		_message = this.getString(R.string.activity_one) + " | " + this.getString(R.string.lc_oncreate);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		_message = this.getString(R.string.activity_one) + " | " + this.getString(R.string.lc_ondestroy);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		
		_stateBoolean = true;
		
		_message = this.getString(R.string.activity_one) + " | " + this.getString(R.string.lc_onpause);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putBoolean("StateBoolean", _stateBoolean);
		
		_message = this.getString(R.string.activity_one) + " | " + 
		           this.getString(R.string.lc_onsaveinstancestate) + 
				   " | StateBoolean = " + String.valueOf(_stateBoolean);
		
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// NOTE:  Only called when Activity is killed, i.e. low memory condition or device rotation.
		super.onRestoreInstanceState(savedInstanceState);
		
		_stateBoolean = savedInstanceState.getBoolean("StateBoolean");
		
		_message = this.getString(R.string.activity_one) + " | " + 
		           this.getString(R.string.lc_onrestoreinstancestate) + 
				   " | StateBoolean = " + String.valueOf(_stateBoolean);
		
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		_message = this.getString(R.string.activity_one) + " | " + this.getString(R.string.lc_onresume);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		
		_message = this.getString(R.string.activity_one) + " | " + this.getString(R.string.lc_onrestart);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		
		_message = this.getString(R.string.activity_one) + " | " + this.getString(R.string.lc_onstart);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		
		_message = this.getString(R.string.activity_one) + " | " + this.getString(R.string.lc_onstop);
		Log.d(Common.TAG, _message);
	}
	
	@Override
	public void onClick(View v)
	{
		callActivity2();
	}

	private void callActivity2()
	{
		// Create an intent to start the activity, notice the use of the 
		// actual class name.
		Intent intent = new Intent(this, Activity2.class);
		startActivity(intent);
	}

}
