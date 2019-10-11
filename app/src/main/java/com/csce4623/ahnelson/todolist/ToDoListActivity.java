package com.csce4623.ahnelson.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import com.csce4623.ahnelson.todolist.model.Alarm_Receiver;

import java.util.Calendar;
import java.util.Date;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = ToDoListActivity.class.getName();
    HomeActivity homeActivity = new HomeActivity();
    DatabaseHelper mDatabaseHelper;
    Alarm_Receiver alarmReceiver = new Alarm_Receiver();
    Context context;
    PendingIntent pendingIntent;
    Intent alarmIntent;

    String contentNote , titleNote;
    int listID, positionArray;

  //  Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    String stringDate, stringTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    //Column names for the ToDoList Table
    public static final String TABLE_COL_ID = "ID";
    public static final String TABLE_COL_TITLE = "TITLE";
    public static final String TABLE_COL_CONTENT = "CONTENT";
    public static final String TABLE_COL_DATE = "DATE";
    public static final String TABLE_COL_TIME = "TIME";
    public static final Boolean TABLE_COL_DONE =  false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
        initializeComponents();

        //change title of screen
        setTitle("Create a New Task");
    }


    void initializeComponents(){

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btn_date).setOnClickListener(this);
        findViewById(R.id.btn_time).setOnClickListener(this);

//        txtDate=(EditText)findViewById(R.id.in_date);
//        txtTime=(EditText)findViewById(R.id.in_time);
        txtDate = getDateEditText();
        txtTime = getTimeEditText();
        mDatabaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        listID = intent.getIntExtra("listId", 0);
        positionArray = intent.getIntExtra("positionArray", 0);
        //initialize alarm manager
      //  this.context = this;

    }


    public void onClick(View v){
        switch (v.getId()){
            //If save clicked, go back to main activity and add the reminder
            case R.id.btnSave:

            //Read and Save Note
              saveNewNote();

               //Start notification
               this.start(mYear, mMonth, mDay, mHour, mMinute);

//               //create a pending intent that delays the intent
//                //unitl the specified calendar time
//                pendingIntent = PendingIntent.getBroadcast(ToDoListActivity.this, 0,
//                        alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                //set alarm manager
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendarTime.getTimeMillis(),
//                        pendingIntent);

                //Send data to HomeActivity
                Intent intent = new Intent();
                intent.putExtra("titleText", titleNote);
               intent.putExtra("contentText", contentNote);
                intent.putExtra("dateText", stringDate);
                intent.putExtra("timeText", stringTime);


                intent.putExtra("listID", listID);
                intent.putExtra("positionArray", positionArray);

                setResult(RESULT_OK, intent);

                this.createNewToDo();
                finish();
                break;
            //This shouldn't happen
            case R.id.btn_date:
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
            case R.id.btn_time:
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
            default:
                break;
        }
    }

    private void createNewToDo() {

       // AddData(titleNote, TABLE_COL_TITLE);
        //AddData(contentNote, TABLE_COL_CONTENT);
      //  homeActivity.AddData(contentNote, "CONTENT");
        mDatabaseHelper.insertData(titleNote, contentNote);
    }

    public void AddData(String newEntry , String colName){
        boolean intertData = mDatabaseHelper.addData(newEntry, colName);

        if(intertData){
            toastMessage("Data Successfully Inserted" );
        }else{
            toastMessage("Something went wrong, please debug");
        }
    }

    public void insertData(String title, String content){
        mDatabaseHelper.insertData(title, content);
    }

    public void saveNewNote(){
        Toast.makeText(getApplicationContext(),"Note Saved!",Toast.LENGTH_LONG).show();
        contentNote = this.getNoteContentEditText().getText().toString();
        titleNote = this.getTitleNoteEditText().getText().toString();
        stringDate =  this.getDateEditText().getText().toString();
        stringTime = this.getTimeEditText().getText().toString();

        Log.i(TAG, "Debug: Note!->" + contentNote + ",  " + titleNote );
        Log.i(TAG, "Debug: Time/Date!->" + stringDate + stringTime);

       //=====// alarmReceiver.start(mYear, mMonth, mDay, mHour, mMinute);

      //  homeActivity.createNewNote(titleNote, contentNote, stringDate, stringTime);
       // homeActivity.addToArrayList(titleNote, contentNote);
    }

    void goToHomeActivity(){
        Intent toDoListIntent = new Intent(this, HomeActivity.class);
        startActivity(toDoListIntent);
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


//        if(cal_alarm.before(cal_now)){
//            cal_alarm.add(Calendar.DATE,1);
//        }

        //Display  notification!
        alarmIntent = new Intent(ToDoListActivity.this, Alarm_Receiver.class);
        alarmIntent.putExtra("notificationMsg",titleNote);
        alarmIntent.putExtra("positionArray",positionArray);

        pendingIntent = PendingIntent.getBroadcast(ToDoListActivity.this, positionArray, alarmIntent, 0);
        Log.i("ToDoActivity", "Alarm Created at "  + cal_alarm.getTimeInMillis() );
        Log.i("ToDoActivity","Current Time: " + System.currentTimeMillis());
        //Fire alarm at the time specified
        //cal_alarm should be the calendar variable
       manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+6000, pendingIntent);
       // manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
    }

    //Get the Information of the note (title, content, date)
    private EditText getNoteContentEditText() {
        return (EditText) this.findViewById(R.id.etNoteContent);
    }
    private EditText getTitleNoteEditText() {
        return (EditText) this.findViewById(R.id.tvNoteTitle);
    }

    private EditText getDateEditText() {
        return (EditText) this.findViewById(R.id.in_date);
    }
    private EditText getTimeEditText() {
        return (EditText) this.findViewById(R.id.in_time);
    }

    private void toastMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
