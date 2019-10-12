package com.csce4623.ahnelson.todolist.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.csce4623.ahnelson.todolist.NotificationHelper;

public class Alarm_Receiver extends BroadcastReceiver {

    String notificationMsg ;
    int listID;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ToDoActivity", "Alarm Recieved!");
        try{
            //get the title from intent
            notificationMsg = intent.getStringExtra("titleText");
            listID = intent.getIntExtra("positionArray" , 0);
            Log.i("ToDoActivity", notificationMsg + " ,  " + listID);
        }catch (Error e){
            Log.e("ToDoActivity",e.toString());
        }


        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(notificationMsg, listID);
        notificationHelper.getManager().notify(1, nb.build());
    }

}
