package com.innovsys.training.yambaclass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver 
{
    
    //NOTE:  In order to execute this Receiver, the following UsePermission
    //      android.permission.RECEIVE_BOOT_COMPLETED

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) 
    {
        Log.v(TAG, "onReceive() invoked");
        
        //Do a one-shot fetch of data at system boot.
        context.startService(new Intent(context, UpdaterService.class));

        Intent startUpdaterService = new Intent(context, UpdaterService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, startUpdaterService, PendingIntent.FLAG_UPDATE_CURRENT);
        
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 10, 60000, pendingIntent);
        
        
    }

}
