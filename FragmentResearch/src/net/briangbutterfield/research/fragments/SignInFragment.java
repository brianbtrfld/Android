package net.briangbutterfield.research.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SignInFragment extends Fragment implements OnClickListener
{
	
	private OnSignInListener m_listener;
	
	private Button m_buttonSignIn;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//Inflate the fragment user interface which creates the View hierarchy.
		View top = inflater.inflate(R.layout.signin_fragment, container, false);
		
		m_buttonSignIn = (Button)top.findViewById(R.id.buttonSignIn);
		m_buttonSignIn.setOnClickListener(this);
		
		return top;
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		//Do not display any menu items in action bar.
		for (int i = 0; i < menu.size(); i++)
		{
			menu.getItem(i).setVisible(false);
		}
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		try
		{
			m_listener = (OnSignInListener)activity;
		} 
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString());
		}
	}


	public void onClick(View v)
	{
		
		switch (v.getId()) 
		{
			case R.id.buttonSignIn:
				m_listener.onSignIn("url", "username", "pwd", "1234");
				break;
	
			default:
				break;
		}
		
	}


	public interface OnSignInListener
	{
		public void onSignIn(String url, String username, String password, String pin);
	}
}