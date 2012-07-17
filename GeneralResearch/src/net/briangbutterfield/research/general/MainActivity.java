package net.briangbutterfield.research.general;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener
{
	
	Button buttonBitwise;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		buttonBitwise = (Button) findViewById(R.id.buttonBitwiseOps);
		
		buttonBitwise.setOnClickListener(this);
		
		
		
	}

	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.buttonBitwiseOps:
				Intent intent = new Intent(v.getContext(), BitwiseOpsActivity.class);
				startActivity(intent);
				break;

			default:
				break;
		}
		
	}

}
