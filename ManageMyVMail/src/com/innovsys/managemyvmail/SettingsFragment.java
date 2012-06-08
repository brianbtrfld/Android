package com.innovsys.managemyvmail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment
{

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
        
        return top;
    }

}
