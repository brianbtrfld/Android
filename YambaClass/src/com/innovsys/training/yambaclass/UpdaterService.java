package com.innovsys.training.yambaclass;

import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class UpdaterService extends IntentService 
{
    
    private static final String TAG = "UpdaterService";
    
    public UpdaterService() 
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent arg0) 
    {
        Log.v(TAG, "OnHandleIntent() invoked");
        
        int count = 0;
        
        try 
        {
            List<Twitter.Status> timeline;
            
                    try 
                    {
                        timeline = YambaApplication.getInstance().getTwitter().getHomeTimeline();
                    } 
                    catch (NullPointerException e) 
                    {
                        // Work-around for a bug in JTwitter library.
                        return;
                    }
            
            ContentValues values = new ContentValues();
            //SQLiteDatabase db = YambaApplication.getInstance().getTimelineDatabase();
            
            for (Twitter.Status status: timeline)  //interesting way to implement.
            {
                String name = status.user.name;
                String message = status.text;
                long id = status.id;
                Date createdAt = status.createdAt;
                
                
                //Determine max timestamp and only insert rows that are greater.
//                Cursor cursor = db.rawQuery("select max(created_at) as max_time from timeline;", null);
//                cursor.moveToFirst();
//                long maxTime = cursor.getLong(0);
//                cursor.close();
//                cursor = null;
                
//                if (createdAt.getTime() > maxTime)
//                {
                    Log.v(TAG, "Row inserted" + id + ": " + name + " posted at " + createdAt + ": " + message);
                    
                    values.clear();
//                    values.put(TimelineHelper.KEY_ID, id);
//                    values.put(TimelineHelper.KEY_USER, name);
//                    values.put(TimelineHelper.KEY_MESSAGE, message);
//                    values.put(TimelineHelper.KEY_CREATED_AT, createdAt.getTime());
                    
                    values.put(StatusContract.Columns._ID, id);
                    values.put(StatusContract.Columns.USER, name);
                    values.put(StatusContract.Columns.MESSAGE, message);
                    values.put(StatusContract.Columns.CREATED_AT, createdAt.getTime());
                    
                    //db.insert(TimelineHelper.T_TIMELINE, null, values);
                    Uri retVal = getContentResolver().insert(StatusContract.CONTENT_URI, values);
                    
                    if (retVal != null) 
                        count++;
                    
//                }
//                else
//                {
//                    Log.v(TAG, "NO Row inserted");
//                }
            }
        } 
        catch (TwitterException e) 
        {
            Log.w(TAG, "Failed to retrieve timeline data");
        }
        
        if (count > 0) 
        {
            pushNotification();
            notifyNewStatus(count);
        }
    }
    
    private void pushNotification() 
    {
        
        int icon = android.R.drawable.stat_notify_chat;
        CharSequence tickerText = getString(R.string.notifyMessage);
        long when = System.currentTimeMillis();
        
        Notification notification = new Notification(icon, tickerText, when);
        
        CharSequence contentTitle = "Content Title";
        CharSequence contentText = "Content Text";
        
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        notification.setLatestEventInfo(this, contentTitle, contentText, pi);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(YambaApplication.NEW_STATUS_NOTIFICATION, notification);
        
    }

    private void notifyNewStatus(int count)
    {
        Intent broadcastIntent = new Intent(YambaApplication.ACTION_NEW_STATUS);
        broadcastIntent.putExtra(YambaApplication.EXTRA_NEW_STATUS_COUNT, count);
        sendBroadcast(broadcastIntent, YambaApplication.PERM_NEW_STATUS);
        
        //testing git commit.
    }

}
