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

//=======================================
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
//=========================================

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.csce4623.ahnelson.todolist.NotificationHelper;
import com.csce4623.ahnelson.todolist.AlertReceiver;

public class AlarmReceiver extends AppCompatActivity  {


    //Class came from: https://stackoverflow.com/questions/36652868/how-to-set-alarm-on-android-studio
    Context mContex;
    PendingIntent pendingIntent;
    //@Override
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
        cal_alarm.set(Calendar.HOUR_OF_DAY,18); //hour from app
        cal_alarm.set(Calendar.MINUTE,26); //get minute from app
        cal_alarm.set(Calendar.SECOND,0);
        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }

        Intent myIntent = new Intent(mContex, AlarmReceiver.class); //mContex used to be contex
        pendingIntent = PendingIntent.getBroadcast(mContex, 0, myIntent, 0);

        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
    }

    public void start(int mYear, int mMonth, int mDay, int mHour, int mMinute) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance(); //get date of alarm
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);

        //Set date
        cal_alarm.set(Calendar.YEAR,mYear);
        cal_alarm.set(Calendar.MONTH,mMonth);
        cal_alarm.set(Calendar.DAY_OF_MONTH,mDay);

        //Set time
        cal_alarm.set(Calendar.HOUR_OF_DAY,mHour); //hour from app
        cal_alarm.set(Calendar.MINUTE,mMinute); //get minute from app
        cal_alarm.set(Calendar.SECOND,0);




        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }

        //Display  notification!
        Intent myIntent = new Intent(this, AlarmReceiver.class); //mContex used to be contex
        pendingIntent = PendingIntent.getBroadcast(this, 1, myIntent, 0);

        //Fire alarm at the time specified
        //cal_alarm should be the calendar variable
        manager.setExact(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
    }

    //Check how to set up alarm manager: https://www.youtube.com/watch?v=yrpimdBRk5Q
//    private void startAlarm(Calendar c) {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//
//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.DATE, 1);
//        }
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    //}

}