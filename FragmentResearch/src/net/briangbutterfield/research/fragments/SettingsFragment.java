package net.briangbutterfield.research.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsFragment extends Fragment implements OnClickListener
{

	private OnSettingsChangeListener m_listener;
	
	private Button m_buttonSignOut;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        //NON-UI initialization.
        super.onCreate(savedInstanceState);
        
        //Forces the state retention of the Fragment when an Activity is destroyed and
        //re-created, this tells the Activity Manager to not call onCreate or onDestroy
        //methods of the Fragment, hence retaining the object in memory while the associated
        //Activity goes through its life cycle.
        setRetainInstance(true);
        
        setHasOptionsMenu(true);
        
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        //Inflate the fragment UI.
        //Creates View Hierarchy.
        View top = inflater.inflate(R.layout.settings_fragment, container, false);
        
        m_buttonSignOut = (Button)top.findViewById(R.id.buttonSignOut);
        m_buttonSignOut.setOnClickListener(this);
		
        return top;
    }

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		try
		{
			m_listener = (OnSettingsChangeListener)activity;
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
			case R.id.buttonSignOut:
				m_listener.onSettingsChange(true, "temp1", "temp2");
				break;
	
			default:
				break;
		}	
	}

	//Interface:  OnSettingsChangeListener
	public interface OnSettingsChangeListener
	{
		public void onSettingsChange(boolean logout, String temp1, String temp2);
	}
}
