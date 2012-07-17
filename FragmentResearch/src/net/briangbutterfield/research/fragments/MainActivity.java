package net.briangbutterfield.research.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements  SignInFragment.OnSignInListener,
															   MessageListFragment.OnMessageSelectedListener,
															   SettingsFragment.OnSettingsChangeListener
                                                            
{
	
	private static final String FRAGMENT_VISIBLE = "fragment_visible";
	private static final String SIGNIN_TAG = "signin_fragment";
	private static final String MESSAGELIST_TAG = "messagelist_fragment";
	private static final String MESSAGEDETAIL_TAG = "messagedetail_fragment";
	private static final String SETTINGS_TAG = "settings_fragment";
	
	private static final int SIGNIN_FRAGMENT_VISIBLE = 1;
	private static final int MESSAGELIST_FRAGMENT_VISIBLE = 2;
	private static final int MESSAGEDETAIL_FRAGMENT_VISIBLE = 3;
	private static final int SETTINGS_FRAGMENT_VISIBLE = 4;
	
	private FragmentManager m_fragmentManager;
	private SignInFragment m_signInFragment;
	private MessageListFragment m_messageListFragment;
	private MessageDetailFragment m_messageDetailFragment;
	private SettingsFragment m_settingsFragment;
	
	private int m_fragmentVisible;
	
	AppLocalStorage m_localDB;
	
	private MenuItem m_messageListMenuItem;
	private MenuItem m_settingsMenuItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		//Insert some test messages.
		//new TestMessageGenerator(this).InsertTestMessages();
		
		m_fragmentManager = getSupportFragmentManager();
		m_localDB = new AppLocalStorage(this);
		
		//Sample use of the Logger.
		App.getInstance().getLogger().logMessage("Error", "this is a test");
		
		//*************************************************************************************************************
		//Section that helps drive when the Sign In screen is not displayed during app running, shutdown, etc.
		//*************************************************************************************************************
		String loginStatus = m_localDB.setLoginStatus();
		
		if (savedInstanceState != null && loginStatus.equalsIgnoreCase("true") == true)
		{
			m_messageListFragment = (MessageListFragment)m_fragmentManager.findFragmentByTag(MESSAGELIST_TAG);
			m_messageDetailFragment = (MessageDetailFragment)m_fragmentManager.findFragmentByTag(MESSAGEDETAIL_TAG);
			m_settingsFragment = (SettingsFragment)m_fragmentManager.findFragmentByTag(SETTINGS_TAG);
            
            showFragment(savedInstanceState.getInt(FRAGMENT_VISIBLE, MESSAGELIST_FRAGMENT_VISIBLE));
        }
		else if (savedInstanceState != null && loginStatus.equalsIgnoreCase("false") == true)
		{
			m_signInFragment = (SignInFragment)m_fragmentManager.findFragmentByTag(SIGNIN_TAG);
			m_messageListFragment = (MessageListFragment)m_fragmentManager.findFragmentByTag(MESSAGELIST_TAG);
			m_messageDetailFragment = (MessageDetailFragment)m_fragmentManager.findFragmentByTag(MESSAGEDETAIL_TAG);
			m_settingsFragment = (SettingsFragment)m_fragmentManager.findFragmentByTag(SETTINGS_TAG);
			
			showFragment(SIGNIN_FRAGMENT_VISIBLE);
		}
		else if (savedInstanceState == null && loginStatus.equalsIgnoreCase("false") == true)
		{
			
			m_signInFragment = new SignInFragment();
			m_messageListFragment = new MessageListFragment();
			m_messageDetailFragment = new MessageDetailFragment();
			m_settingsFragment = new SettingsFragment();
			
			m_fragmentManager.beginTransaction()
			                 .add(R.id.fragmentContainer, m_signInFragment, SIGNIN_TAG)
				             .add(R.id.fragmentContainer, m_messageListFragment, MESSAGELIST_TAG)
				             .add(R.id.fragmentContainer, m_messageDetailFragment, MESSAGEDETAIL_TAG)
				             .add(R.id.fragmentContainer, m_settingsFragment, SETTINGS_TAG)
				             .commit();
			
			showFragment(SIGNIN_FRAGMENT_VISIBLE);
		}
		else if (savedInstanceState == null && loginStatus.equalsIgnoreCase("true") == true)
		{
			
			m_messageListFragment = new MessageListFragment();
			m_messageDetailFragment = new MessageDetailFragment();
			m_settingsFragment = new SettingsFragment();
			
			m_fragmentManager.beginTransaction()
				             .add(R.id.fragmentContainer, m_messageListFragment, MESSAGELIST_TAG)
				             .add(R.id.fragmentContainer, m_messageDetailFragment, MESSAGEDETAIL_TAG)
				             .add(R.id.fragmentContainer, m_settingsFragment, SETTINGS_TAG)
				             .commit();
			
			showFragment(MESSAGELIST_FRAGMENT_VISIBLE);
		}
		//*************************************************************************************************************
		//*************************************************************************************************************
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
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
			case MESSAGEDETAIL_FRAGMENT_VISIBLE:
				m_messageListMenuItem.setVisible(true);
				m_settingsMenuItem.setVisible(false);
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

	
	public void onSignIn(String url, String username, String password, String pin)
	{
		Toast.makeText(this, url + username + password + pin, Toast.LENGTH_LONG).show();
	
		m_fragmentManager.beginTransaction()
		                 .remove(m_signInFragment)
		                 .commit();
		
		m_localDB.updateLoginStatus("true");
		
		showFragment(MESSAGELIST_FRAGMENT_VISIBLE);
	}

	public void onMessageSelected(String id, String position)
	{
		m_messageDetailFragment.setMessageDetails(id, position);
		showFragment(MESSAGEDETAIL_FRAGMENT_VISIBLE);
	}

	public void onSettingsChange(boolean logout, String temp1, String temp2)
	{
	
		if (logout == true)
		{
			m_signInFragment = new SignInFragment();
			
			m_fragmentManager.beginTransaction()
                             .add(R.id.fragmentContainer, m_signInFragment, SIGNIN_TAG)
                             .commit();
			
			m_localDB.updateLoginStatus("false");
			
			showFragment(SIGNIN_FRAGMENT_VISIBLE);
		}
	}

	private void showFragment(int visibleFragment)
    {
        m_fragmentVisible = visibleFragment;
        
        switch (visibleFragment) 
        {
        	
        	case SIGNIN_FRAGMENT_VISIBLE:
        		
        		if (m_messageListFragment != null)
				{
        			m_fragmentManager.beginTransaction()
        						     .show(m_signInFragment)
                    				 .hide(m_messageListFragment)
                    				 .hide(m_messageDetailFragment)
                    				 .hide(m_settingsFragment)
                    				 .commit();
				}
        		else
        		{
        			m_fragmentManager.beginTransaction()
                    				 .show(m_signInFragment)
                    				 .commit();
        		}
        		
        		break;
            
        	case MESSAGELIST_FRAGMENT_VISIBLE:
                m_fragmentManager.beginTransaction()
                                 .show(m_messageListFragment)
                				 .hide(m_messageDetailFragment)
                				 .hide(m_settingsFragment)
                                 .commit();
                break;
            case MESSAGEDETAIL_FRAGMENT_VISIBLE:
            	m_fragmentManager.beginTransaction()
            	                 .hide(m_messageListFragment)
                				 .show(m_messageDetailFragment)
                				 .hide(m_settingsFragment)
                                 .commit();
            	break;
            case SETTINGS_FRAGMENT_VISIBLE:
            	m_fragmentManager.beginTransaction()
            				     .hide(m_messageListFragment)
                				 .hide(m_messageDetailFragment)
                				 .show(m_settingsFragment)
            					 .commit();
            	break;
            default:
                
                break;
        }
        
    }
	
}
