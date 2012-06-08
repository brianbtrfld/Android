package com.innovsys.managemyvmail;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity
{
	
	private static final String FRAGMENT_VISIBLE = "fragment_visible";
	private static final String MESSAGELIST_TAG = "messagelist_fragment";
	private static final String SETTINGS_TAG = "settings_fragment";
	
	private static final int MESSAGELIST_FRAGMENT_VISIBLE = 1;
	private static final int SETTINGS_FRAGMENT_VISIBLE = 2;
	
	private FragmentManager m_fragmentManager;
	private MessageListFragment m_messageListFragment;
	private SettingsFragment m_settingsFragment;
	
	private int m_fragmentVisible;
	
	private MenuItem m_messageListMenuItem;
	private MenuItem m_settingsMenuItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		m_fragmentManager = getSupportFragmentManager();
		
		if (savedInstanceState == null)
		{
			m_messageListFragment = new MessageListFragment();
			m_settingsFragment = new SettingsFragment();
			
			m_fragmentManager.beginTransaction()
			                 .add(R.id.fragmentContainer, m_messageListFragment, MESSAGELIST_TAG)
			                 .add(R.id.fragmentContainer, m_settingsFragment, SETTINGS_TAG)
			                 .commit();
			
			showFragment(MESSAGELIST_FRAGMENT_VISIBLE);
		}
		else
        {
			m_messageListFragment = (MessageListFragment)m_fragmentManager.findFragmentByTag(MESSAGELIST_TAG);
            
            showFragment(savedInstanceState.getInt(FRAGMENT_VISIBLE, MESSAGELIST_FRAGMENT_VISIBLE));
        }
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		
		super.onSaveInstanceState(outState);
        
        //Store the current fragment that is visible so that 
        //when the activity is destroyed and created again, 
        //the right fragment can be displayed to the user.
        outState.putInt(FRAGMENT_VISIBLE, m_fragmentVisible);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//Allow the superclass to add any options menu.
		super.onCreateOptionsMenu(menu);
		
		getMenuInflater().inflate(R.menu.main_menu, menu);
		
		m_messageListMenuItem = menu.findItem(R.id.menuMessages);
		m_settingsMenuItem = menu.findItem(R.id.menuSettings);
		
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		switch (m_fragmentVisible)
		{
			case MESSAGELIST_FRAGMENT_VISIBLE:
				m_messageListMenuItem.setVisible(false);
				m_settingsMenuItem.setVisible(true);
				break;
			case SETTINGS_FRAGMENT_VISIBLE:
				m_messageListMenuItem.setVisible(true);
				m_settingsMenuItem.setVisible(false);
			default:
				break;
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
        
        switch (id) 
        {
                
            case R.id.menuMessages:
                
                showFragment(MESSAGELIST_FRAGMENT_VISIBLE);
                return true;
            
            case R.id.menuSettings:
                
                showFragment(SETTINGS_FRAGMENT_VISIBLE);
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
	}

	private void showFragment(int visibleFragment)
    {
        m_fragmentVisible = visibleFragment;
        
        switch (visibleFragment) 
        {
            case MESSAGELIST_FRAGMENT_VISIBLE:
                m_fragmentManager.beginTransaction()
                				 .hide(m_settingsFragment)
                                 .show(m_messageListFragment)
                                 .commit();
                break;
            case SETTINGS_FRAGMENT_VISIBLE:
            	m_fragmentManager.beginTransaction()
            				     .hide(m_messageListFragment)
            					 .show(m_settingsFragment)
            					 .commit();
            	break;
            default:
                
                break;
        }
        
    }
	
}
