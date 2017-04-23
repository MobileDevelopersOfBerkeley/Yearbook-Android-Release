package com.mdb.yearbook.android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Shiv on 4/15/17.
 */

public class Scheduler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!YearbookActivity.alarmServiceOn) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            builder.setContentTitle("Upload a photo today!");
            builder.setSmallIcon(R.drawable.yb);
            Intent openIntent = new Intent(context, SplashActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            builder.setAutoCancel(true);
            notificationManager.notify(1, builder.build());

            //YearbookActivity.alarmServiceOn = true;
        }
    }
}
