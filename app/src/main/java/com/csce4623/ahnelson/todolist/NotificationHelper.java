package com.csce4623.ahnelson.todolist;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.content.Intent;


public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {

      //  String intentTitle =
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Alarm!")
                .setContentText("Your AlarmManager is working.")
                .setSmallIcon(R.drawable.ic_android);
    }

    public NotificationCompat.Builder getChannelNotification(String Notetitle) {

        Intent resultIntent = new Intent(this, HomeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Reminder!")
                .setContentText(Notetitle)
                .setSmallIcon(R.drawable.ic_android)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }
}