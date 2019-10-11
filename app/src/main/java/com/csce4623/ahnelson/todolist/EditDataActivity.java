package com.csce4623.ahnelson.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.csce4623.ahnelson.todolist.model.Alarm_Receiver;

import java.util.Calendar;
import java.util.Date;

public class EditDataActivity extends AppCompatActivity implements View.OnClickListener {



    private static String TAG = EditDataActivity.class.getName();
    HomeActivity homeActivity = new HomeActivity();
    Alarm_Receiver alarmReceiver = new Alarm_Receiver();
    Context context;
    PendingIntent pendingIntent;
    Intent alarmIntent;

    String contentNote , titleNote, alarmTimeNote;
    String  oldTitle;
    int listID;

    //  Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    EditText txtTitle, txtContent;
    DatabaseHelper mDatabaseHelper;
    String stringDate, stringTime;
    String[] columns = new String[] {"ID" , "TITLE", "CONTENT", "DATE", "TIME" };
    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note_activity);
        initializeComponents();

        //change title of screen
        setTitle("Edit Current Task");
    }


    void initializeComponents(){

        findViewById(R.id.btnSaveEdit).setOnClickListener(this);
        findViewById(R.id.btn_dateEdit).setOnClickListener(this);
        findViewById(R.id.btn_timeEdit).setOnClickListener(this);
        findViewById(R.id.btnDeleteEdit).setOnClickListener(this);

        txtTitle = (EditText) findViewById(R.id.tvNoteTitleEdit);
        txtContent = (EditText) findViewById(R.id.etNoteContentEdit);

        mDatabaseHelper = new DatabaseHelper(this);

//        txtDate=(EditText)findViewById(R.id.in_date);
//        txtTime=(EditText)findViewById(R.id.in_time);
        txtDate = getDateEditText();
        txtTime = getTimeEditText();

        //initialize alarm manager
        //  this.context = this;

        //Get the intent extra from HomeActivity
        Intent receivedIntent = getIntent();
        //now get the itemID we passed as an extra
        listID = receivedIntent.getIntExtra("listId", -1);
        oldTitle = receivedIntent.getStringExtra("titleText");

        txtTitle.setText(oldTitle);


    }


    public void onClick(View v){
        switch (v.getId()){
            //If save clicked, go back to main activity and add the reminder
            case R.id.btnSaveEdit:

                titleNote = txtTitle.getText().toString();
                //update database
                mDatabaseHelper.updateData(titleNote, listID, oldTitle);

                saveEditedNote();

                //Start notification
                this.start(mYear, mMonth, mDay, mHour, mMinute);
                //Send data to HomeActivity
                Intent intent = new Intent();
                intent.putExtra("titleText", titleNote);
                intent.putExtra("contentText", contentNote);
                intent.putExtra("dateText", stringDate);
                intent.putExtra("timeText", stringTime);

             //   listID++;
                intent.putExtra("listID", listID);

                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.btnDeleteEdit:
                titleNote = txtTitle.getText().toString();
                mDatabaseHelper.deleteName(listID, titleNote);

               // txtTitle.setText("");
                toastMessage("Data removed from Database");

                setResult(RESULT_OK);
                finish();
                break;
            //This shouldn't happen
            case R.id.btn_dateEdit:
                //pop up data came from: https://www.journaldev.com/9976/android-date-time-picker-dialog
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                break;
            case R.id.btn_timeEdit:
                // Get Current Time
                final Calendar ctime = Calendar.getInstance();
                mHour = ctime.get(Calendar.HOUR_OF_DAY);
                mMinute = ctime.get(Calendar.MINUTE);

                //convert 24-hour time to 12 hour time
                if(Calendar.HOUR_OF_DAY  > 12){

                }


                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                                mHour = hourOfDay;
                                mMinute = minute;
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
                break;
            default:
                break;

        }
    }



    public void saveEditedNote(){
        Toast.makeText(getApplicationContext(),"Edited Note Saved!",Toast.LENGTH_LONG).show();
        contentNote = this.getNoteContentEditText().getText().toString();
        titleNote = this.getTitleNoteEditText().getText().toString();
        stringDate =  this.getDateEditText().getText().toString();
        stringTime = this.getTimeEditText().getText().toString();

        Log.i(TAG, "Edit Debug: Note!->" + contentNote + titleNote + alarmTimeNote);
        Log.i(TAG, "Edit Debug: Time/Date!->" + stringDate + stringTime);

    }



    public void start(int mYear, int mMonth, int mDay, int mHour, int mMinute) {
        AlarmManager manager;
        if(Build.VERSION.SDK_INT>=23) {
            manager = (AlarmManager) getApplicationContext().getSystemService(AlarmManager.class);
        }else{
            manager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        }


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


        //Display  notification!
        alarmIntent = new Intent(EditDataActivity.this, Alarm_Receiver.class);
        alarmIntent.putExtra("notificationMsg",titleNote);
        pendingIntent = PendingIntent.getBroadcast(EditDataActivity.this, listID, alarmIntent, 0);
        Log.i("ToDoActivity", "Edit: Alarm Created at "  + cal_alarm.getTimeInMillis() );
        Log.i("ToDoActivity","Edit: Current Time: " + System.currentTimeMillis());
        //Fire alarm at the time specified
        //cal_alarm should be the calendar variable
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+6000, pendingIntent);
        // manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
    }

    //Get the Information of the note (title, content, date)
    private EditText getNoteContentEditText() {
        return (EditText) this.findViewById(R.id.etNoteContentEdit);
    }
    private EditText getTitleNoteEditText() {
        return (EditText) this.findViewById(R.id.tvNoteTitleEdit);
    }

    private EditText getDateEditText() {
        return (EditText) this.findViewById(R.id.in_dateEdit);
    }
    private EditText getTimeEditText() {
        return (EditText) this.findViewById(R.id.in_timeEdit);
    }


    private void toastMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
