package com.innovsys.managemyvmail;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class MessageDetailActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.messagedetail);
		
		EditText editTextPosition;
		EditText editTextId;
		
		editTextPosition = (EditText)findViewById(R.id.editText1);
		editTextId = (EditText)findViewById(R.id.editText2);
		
		editTextPosition.setText("Position: " + getIntent().getExtras().getString("position"));
		editTextId.setText("ID: " + getIntent().getExtras().getString("id"));
		
	}

}
