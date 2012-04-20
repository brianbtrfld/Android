package com.innovsys.training.yambaclass;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity 
{   
    private FragmentManager m_fragmentManager;
    private ComposeFragment m_composeFragment;
    private TimelineFragment m_timelineFragment;
    
    private MenuItem m_composeMenuItem;
    private MenuItem m_timelineMenuItem;
    
    private int m_fragmentVisible;
    
    private static final String FRAGMENT_VISIBLE = "fragment_visible";
    private static final String COMPOSE_TAG = "compose_fragment";
    private static final String TIMELINE_TAG = "timeline_fragment";
    
    private static final int COMPOSE_FRAGMENT_VISIBLE = 1;
    private static final int TIMELINE_FRAGMENT_VISIBLE = 2;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        //Must call through super class implementation for the 
        //life cycle methods otherwise exception will be thrown.
        //This is a good practice.
        super.onCreate(savedInstanceState);
        
        //Inflating the layout and creating all of the views.
        setContentView(R.layout.main);
        
        m_fragmentManager = getSupportFragmentManager();
        
        if (savedInstanceState == null) 
        {
            //Initialize our fragments.
            m_composeFragment = new ComposeFragment();
            m_timelineFragment = new TimelineFragment();
            
            m_fragmentManager.beginTransaction()
                             .add(R.id.fragmentContainer, m_composeFragment, COMPOSE_TAG)       
                             .add(R.id.fragmentContainer, m_timelineFragment, TIMELINE_TAG)
                             .commit();
            
            showFragment(TIMELINE_FRAGMENT_VISIBLE);
        }
        else
        {
            m_composeFragment = (ComposeFragment)m_fragmentManager.findFragmentByTag(COMPOSE_TAG);
            m_timelineFragment = (TimelineFragment)m_fragmentManager.findFragmentByTag(TIMELINE_TAG);
            
            showFragment(savedInstanceState.getInt(FRAGMENT_VISIBLE, TIMELINE_FRAGMENT_VISIBLE));
        }
        
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        
        //Store the current fragment that is visible so that 
        //when the activity is detroyed and created again, 
        //the right fragment can be displayed to the user.
        outState.putInt(FRAGMENT_VISIBLE, m_fragmentVisible);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        //Allow the superclass to add any options menu.
        super.onCreateOptionsMenu(menu);
        
        getMenuInflater().inflate(R.menu.menu_main, menu);
        
        m_composeMenuItem = menu.findItem(R.id.menuCompose);
        m_timelineMenuItem = menu.findItem(R.id.menuTimeline);
        
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) 
    {
        
        switch (m_fragmentVisible) 
        {
            case COMPOSE_FRAGMENT_VISIBLE:
                m_composeMenuItem.setVisible(false);
                m_timelineMenuItem.setVisible(true);
                break;
            case TIMELINE_FRAGMENT_VISIBLE:
                m_composeMenuItem.setVisible(true);
                m_timelineMenuItem.setVisible(false);
                break;
            default:
                break;
        }
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        
        Intent intent;
        int id = item.getItemId();
        
        switch (id) 
        {
            case R.id.menuPreferences:
                //Display the preference Activity.
                
                //Establish intent.
                intent = new Intent(this, PrefsActivity.class);
                
                //StartActivity is async call so it will return right away.
                this.startActivity(intent);
                
                return true;
                
//            case R.id.menuRefresh:
//                //Start the UpdaterService.
//                
//                intent = new Intent(this, UpdaterService.class);
//                startService(intent);
//                
//                return true;
                
            case R.id.menuCompose:
                
                showFragment(COMPOSE_FRAGMENT_VISIBLE);
                return true;
            
            case R.id.menuTimeline:
                //Display timeline activity.
                
                //intent = new Intent(this, TimelineActivity.class);
                //startActivity(intent);
                showFragment(TIMELINE_FRAGMENT_VISIBLE);
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
            case COMPOSE_FRAGMENT_VISIBLE:
                m_fragmentManager.beginTransaction()
                                 .hide(m_timelineFragment)
                                 .show(m_composeFragment)
                                 .commit();
                break;
            default:
                m_fragmentManager.beginTransaction()
                                 .hide(m_composeFragment)
                                 .show(m_timelineFragment)
                                 .commit();
                break;
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
        {
            //Force refresh of action bar.
            invalidateOptionsMenu();
        } 
    }
}