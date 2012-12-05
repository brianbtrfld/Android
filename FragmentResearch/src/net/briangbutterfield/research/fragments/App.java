package net.briangbutterfield.research.fragments;

import android.app.Application;

public class App extends Application
{
	
	public static String TAG = "FragmentResearch";
	
	private static App m_instance;
	
	private AppLogger m_logger;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		m_instance = this;
	}


	public static App getInstance()
	{
		//Exposes a mechanism to get an instance of the 
        //custom application object.
        return m_instance;
	}
	
	public synchronized AppLogger getLogger()
	{
		
		//Synchronized is used to sync access to the getTwitter() object
        //to avoid a race condition between possible difference threads
        //needing the object, i.e. refresh or other functions in threads.
		
		if (m_logger == null)
		{
			m_logger = new AppLogger("all");
		}
		
		return m_logger;
	}
}
