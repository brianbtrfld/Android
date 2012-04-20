package com.innovsys.training.yambaclass;

import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity 
{

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) 
        {
            //deprecated as of API 11.
            addPreferencesFromResource(R.xml.prefs);
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) 
    {
        loadHeadersFromResource(R.xml.prefs_headers, target);
    }

}
