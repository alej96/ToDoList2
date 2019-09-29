package com.csce4623.ahnelson.todolist.model;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.app.AlarmManager;
import java.util.Date;
import java.util.Calendar;
import android.content.Context;
import android.app.PendingIntent;

public class AlarmReceiver extends BroadcastReceiver {


    //Class came from: https://stackoverflow.com/questions/36652868/how-to-set-alarm-on-android-studio
    Context mContex;
    PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "ALARM", Toast.LENGTH_LONG).show();
    }

    public void start() {
        AlarmManager manager = (AlarmManager) mContex.getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance(); //get date of alarm
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY,14); //hour from app
        cal_alarm.set(Calendar.MINUTE,18); //get minute from app
        cal_alarm.set(Calendar.SECOND,0);
        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }

        Intent myIntent = new Intent(mContex, AlarmReceiver.class); //mContex used to be contex
        pendingIntent = PendingIntent.getBroadcast(mContex, 0, myIntent, 0);

        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
    }
}