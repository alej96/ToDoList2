package com.csce4623.ahnelson.todolist.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.csce4623.ahnelson.todolist.NotificationHelper;

public class Alarm_Receiver extends BroadcastReceiver {

    String notificationMsg ;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ToDoActivity", "Alarm Recieved!");
        try{

            notificationMsg = intent.getStringExtra("notificationMsg");
            Log.i("ToDoActivity",intent.getStringExtra("notificationMsg"));
        }catch (Error e){
            Log.e("ToDoActivity",e.toString());
        }

        //get the title from intent



        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(notificationMsg);
        notificationHelper.getManager().notify(1, nb.build());
    }

}
