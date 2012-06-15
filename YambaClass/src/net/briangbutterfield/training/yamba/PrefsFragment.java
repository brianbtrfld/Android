package net.briangbutterfield.training.yamba;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefsFragment extends PreferenceFragment 
{

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Use of PreferenceFragment is documented in the PreferenceActivity.
        //  Only supported in SDK API > 10
        addPreferencesFromResource(R.xml.prefs);
       
    }

}
