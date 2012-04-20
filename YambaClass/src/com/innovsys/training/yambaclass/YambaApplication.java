package com.innovsys.training.yambaclass;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
//import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener
{
    
    //By definition, the Application object is a singleton object, 
    //i.e. one instance per app.  The lifecycle for the Application
    //object is fairly simple compared to the Activity lifecycle.
    
    private static YambaApplication instance;
    
    public static final String ACTION_NEW_STATUS = "com.innovsys.training.yambaclass.ACTION_NEW_STATUS";
    public static final String EXTRA_NEW_STATUS_COUNT = "EXTRA_NEW_STATUS_COUNT";
    public static final String PERM_NEW_STATUS = "com.innovsys.training.yambaclass.permission.NEW_STATUS";
    public static final int NEW_STATUS_NOTIFICATION = 1;
    
    private Twitter twitter;
    //private TimelineHelper timelineHelper;
    private SharedPreferences prefs;
    
    private String prefUsernameKey;
    private String prefPasswordKey;
    private String prefURLSiteKey;
    
    
    public static YambaApplication getInstance() 
    {
        //Exposes a mechanism to get an instance of the 
        //custom application object.
        return instance;
    }

    public synchronized Twitter getTwitter() 
    {
        //Synchronized is used to sync access to the getTwitter() object
        //to avoid a race condition between possible difference threads
        //needing the object, i.e. update and retrieve timeline.
        
        if (twitter == null) 
        {
            //Get the string values from the preference key.
            String username = prefs.getString(prefUsernameKey, null);
            String password = prefs.getString(prefPasswordKey, null);
            String siteURL = prefs.getString(prefURLSiteKey, null);
            
            twitter = new Twitter(username, password);
            twitter.setAPIRootUrl(siteURL);
        }
        
        return twitter;
    }

    @Override
    public void onCreate() 
    {
        super.onCreate();
        
        instance = this;
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        //Register for preference value changes.
        prefs.registerOnSharedPreferenceChangeListener(this);
        
        prefUsernameKey = getString(R.string.prefsUsernameKey);
        prefPasswordKey = getString(R.string.prefsPasswordKey);
        prefURLSiteKey = getString(R.string.prefsSiteURLKey);
        
        //timelineHelper = new TimelineHelper(this);
        
    }
    
//    public SQLiteDatabase getTimelineDatabase()
//    {
//        return timelineHelper.getWritableDatabase();
//    }
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
    {
        twitter = null;
    }
    
    
}
