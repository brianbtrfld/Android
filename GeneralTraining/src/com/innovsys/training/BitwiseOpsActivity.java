package com.innovsys.training;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class BitwiseOpsActivity extends Activity implements OnClickListener 
{
	private static final int logLevelInfo = 1;
	private static final int logLevelWarning = 2;
	private static final int logLevelError = 4;
	
	CheckBox chkBoxInfo;
	CheckBox chkBoxWarning;
	CheckBox chkBoxError;
	Button buttonTest;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bitwiseops);

        chkBoxInfo = (CheckBox)findViewById(R.id.chkBoxInfo);
        chkBoxWarning = (CheckBox)findViewById(R.id.chkBoxWarning);
        chkBoxError = (CheckBox)findViewById(R.id.chkBoxError);
        
        buttonTest = (Button)findViewById(R.id.button1);
        buttonTest.setOnClickListener(this);
        
    }

	public void onClick(View v) 
	{
		
		int selectedOptions = 0;
		
		if (chkBoxInfo.isChecked())
		{
			selectedOptions = selectedOptions | logLevelInfo;
		}
		
		if (chkBoxWarning.isChecked())
		{
			selectedOptions = selectedOptions | logLevelWarning;
		}
		
		if (chkBoxError.isChecked())
		{
			selectedOptions = selectedOptions | logLevelError;
		}
		
		Toast.makeText(BitwiseOpsActivity.this, displayText(selectedOptions), Toast.LENGTH_LONG).show();
		
	}
	
	private String displayText(int logLevels)
	{
		String displayString = "";
		
		if ((logLevels & logLevelInfo) == logLevelInfo)
		{
			displayString = displayString + "Info:";
		}
		
		if ((logLevels & logLevelWarning) == logLevelWarning)
		{
			displayString = displayString + "Warning:";
		}
		
		if ((logLevels & logLevelError) == logLevelError)
		{
			displayString = displayString + "Error:";
		}
		
		return displayString;
	}
}