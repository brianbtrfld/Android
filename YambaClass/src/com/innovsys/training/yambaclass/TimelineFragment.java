package com.innovsys.training.yambaclass;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TimelineFragment extends ListFragment implements ViewBinder, LoaderManager.LoaderCallbacks<Cursor> 
{
    private TimelineReceiver m_receiver;
    private IntentFilter m_filter;
    
    private NotificationManager m_notificationManager;
    
    //private Cursor m_cursor;   REMOVED when Loader was implemented, LOADER owns the cursor.
    private SimpleCursorAdapter m_adapter;
    
    //private static final String[] columnNames = { TimelineHelper.KEY_USER, TimelineHelper.KEY_CREATED_AT, TimelineHelper.KEY_MESSAGE };
    private static final String[] columnNames = { StatusContract.Columns.USER, StatusContract.Columns.CREATED_AT, StatusContract.Columns.MESSAGE };
    
    
    //private static final int[] valueMap = { android.R.id.text1, android.R.id.text2 };
    private static final int[] valueMap = { R.id.timelineDataUser, R.id.timelineDataDate, R.id.timelineDataMessage };
    
    //Android has a set of standard layout resources that can
    //be used.
    //  ex.
    //  android.R.layout.simple_list_item_2
    //  android.R.id.text1
    //  android.R.id.text2
    //
    //  Documentation does not provide details on the layout resources, so 
    //  you have to go to the SDK folder and view the xml layout def files
    //  for details information on res IDs, etc.
    //  /sdk/platforms/android-##/data/res/simple_list_item_2.xml

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        
        
//                    m_cursor = YambaApplication.getInstance().getTimelineDatabase()
//                               .query(TimelineHelper.T_TIMELINE, 
//                                      null, 
//                                      null, 
//                                      null, 
//                                      null, 
//                                      null, 
//                                      TimelineHelper.KEY_CREATED_AT + " desc");
        
        
        //Create data adapter
        //NOTE:  Use AppContext instead of ActContext so that this adapter does not
        //       hold on to reference to Activity when it should be gc.  AppContext
        //       will always exist for the life of the app.
        //       ???  Ask how long the entity you are passing the reference to will
        //            exist in the Activity life cycle.
            /**m_adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                                android.R.layout.simple_list_item_2, 
                                                m_cursor, 
                                                columnNames, 
                                                valueMap, 
                                                0); */
        
        m_adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                            R.layout.timeline_row, 
                                            null, 
                                            columnNames, 
                                            valueMap, 
                                            0);
        
        //Create ViewBinder so that we can format the date field to a relative date.
        m_adapter.setViewBinder(this);
        
        //Hook up the list fragment to the data adapter.
        setListAdapter(m_adapter);
        
        m_receiver = new TimelineReceiver();
        //Establishes the IntentFilter used to register the broadcast receiver.
        m_filter = new IntentFilter(YambaApplication.ACTION_NEW_STATUS);
        
        m_notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
        super.onActivityCreated(savedInstanceState);
        
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
    {
     
        inflater.inflate(R.menu.menu_timeline_fragment, menu);
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
            case R.id.menuRefresh:
                //Start the Updater Service to refresh the timeline.
                Intent intent = new Intent(getActivity(), UpdaterService.class);
                getActivity().startService(intent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() 
    {
    
        super.onResume();
        
        //Deprecated method.
        //m_cursor.requery();
        
        getLoaderManager().restartLoader(0, null, this);
        
        //Register the receiver.
        getActivity().registerReceiver(m_receiver, m_filter, YambaApplication.PERM_NEW_STATUS, null);
        
        m_notificationManager.cancel(YambaApplication.NEW_STATUS_NOTIFICATION);
        
        
    }

    @Override
    public void onPause() 
    {
    
        super.onPause();
        
        //Deprecated method.
        //m_cursor.deactivate();
        
        //NOTE:  Could simply call LoaderManager().destroyLoader();
        //       Instead just remove onPause.
        
        //However, we are now bringing it back to life in order to unregister
        //the receiver.
        
        getActivity().unregisterReceiver(m_receiver);
    }
//
//    @Override
//    public void onDestroy() 
//    {
//    
//        super.onDestroy();
//        
//        m_cursor.close();
//    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) 
    {
        int id = view.getId();
        
        switch (id)
        {
            case R.id.timelineDataDate:
                //customize appearance of the date
                long timestamp = cursor.getLong(columnIndex);
                //provides string ex. 1 hour ago, 3 mins ago, etc.
                CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp);
                ((TextView)view).setText(relativeTime);
                return true;
                
            default:
                //Let the SimpleCursorAdapter perform the default binding of data.
                return false;
        }
        
        
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) 
    {
        return new CursorLoader(getActivity().getApplicationContext(), 
                                StatusContract.CONTENT_URI, 
                                null, 
                                null, 
                                null, 
                                null);
    
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) 
    {
        //Initially the Adapter has no cursor, see onCreate() above where
        //the Adapter is created.  Since cursor <null> was sent to creation
        //of Adapter.  Since this is the case, take the Cursor from the
        //Content Provider and assign that cursor to the adapter.
        m_adapter.swapCursor(cursor);
        
        //Forces a refresh of the View associated with the adapter.
        //i.e. the UI list of status updates gets refreshed.
        m_adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor) 
    {
    
        m_adapter.swapCursor(null);
    }
    
    private class TimelineReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) 
        {
            getLoaderManager().restartLoader(0, null, TimelineFragment.this);
            
            m_notificationManager.cancel(YambaApplication.NEW_STATUS_NOTIFICATION);
            
        }
        
    }
    
}
