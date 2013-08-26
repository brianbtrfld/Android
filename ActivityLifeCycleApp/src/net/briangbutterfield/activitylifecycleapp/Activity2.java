package net.briangbutterfield.activitylifecycleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Activity2 extends Activity
{
	
	private String _message = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity2);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		_message = this.getString(R.string.activity_two) + " | " + this.getString(R.string.lc_ondestroy);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		
		_message = this.getString(R.string.activity_two) + " | " + this.getString(R.string.lc_onpause);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		_message = this.getString(R.string.activity_two) + " | " + this.getString(R.string.lc_onresume);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		
		_message = this.getString(R.string.activity_two) + " | " + this.getString(R.string.lc_onrestart);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		
		_message = this.getString(R.string.activity_two) + " | " + this.getString(R.string.lc_onstart);
		Log.d(Common.TAG, _message);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		
		_message = this.getString(R.string.activity_two) + " | " + this.getString(R.string.lc_onstop);
		Log.d(Common.TAG, _message);
	}
	
	private void callActivity3()
	{
		Intent intent = new Intent(this, Activity3.class);
		startActivity(intent);
	}
}
