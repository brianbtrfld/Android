package com.innovsys.managemyvmail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SignInActivity extends Activity implements OnClickListener
{
	
	Button buttonSignIn;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		
		buttonSignIn = (Button)findViewById(R.id.buttonSignIn);
		buttonSignIn.setOnClickListener(this);
		
		
		//Insert some test messages.
		new TestMessageGenerator(this).InsertTestMessages();
		
		
	}

	public void onClick(View v)
	{
		
		switch (v.getId()) 
		{
			case R.id.buttonSignIn:
				Intent intent = new Intent(v.getContext(), MainActivity.class);
				startActivity(intent);
				break;

			default:
				break;
		}
		
	}
}