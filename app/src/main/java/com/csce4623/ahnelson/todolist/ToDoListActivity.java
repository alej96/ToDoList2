package com.csce4623.ahnelson.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.view.Menu;
import android.view.MenuItem;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.util.Calendar;
import com.csce4623.ahnelson.todolist.HomeActivity;
import  com.csce4623.ahnelson.todolist.model.AlarmReceiver;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = ToDoListActivity.class.getName();
    HomeActivity homeActivity = new HomeActivity();
    AlarmReceiver alarmReceiver = new AlarmReceiver();

    String contentNote , titleNote, alarmTimeNote;

  //  Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    String stringDate, stringTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
        initializeComponents();
    }


    void initializeComponents(){

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btn_date).setOnClickListener(this);
        findViewById(R.id.btn_time).setOnClickListener(this);

//        txtDate=(EditText)findViewById(R.id.in_date);
//        txtTime=(EditText)findViewById(R.id.in_time);
        txtDate = getDateEditText();
        txtTime = getTimeEditText();

    }


    public void onClick(View v){
        switch (v.getId()){
            //If save clicked, go back to main activity and add the reminder
            case R.id.btnSave:
               saveNewNote();
               goToHomeActivity();
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

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            default:
                break;
        }
    }
    public void saveNewNote(){
        Toast.makeText(getApplicationContext(),"Note Saved!",Toast.LENGTH_LONG).show();
        contentNote = this.getNoteContentEditText().getText().toString();
        titleNote = this.getTitleNoteEditText().getText().toString();
        alarmTimeNote = this.getAlarmTimeEditText().getText().toString();
        stringDate =  this.getDateEditText().getText().toString();
        stringTime = this.getTimeEditText().getText().toString();

        Log.i(TAG, "Debug: Note!->" + contentNote + titleNote + alarmTimeNote);
        Log.i(TAG, "Debug: Time/Date!->" + stringDate + stringTime);

        alarmReceiver.start(mYear, mMonth, mDay, mHour, mMinute);

     //   homeActivity.createNewNote(titleNote, contentNote);
    }

    void goToHomeActivity(){
        Intent toDoListIntent = new Intent(this, HomeActivity.class);
        startActivity(toDoListIntent);
    }

    //Get the Information of the note (title, content, date)
    private EditText getNoteContentEditText() {
        return (EditText) this.findViewById(R.id.etNoteContent);
    }
    private EditText getTitleNoteEditText() {
        return (EditText) this.findViewById(R.id.tvNoteTitle);
    }
    private EditText getAlarmTimeEditText() {
        return (EditText) this.findViewById(R.id.etDatePicker);
    }
    private EditText getDateEditText() {
        return (EditText) this.findViewById(R.id.in_date);
    }
    private EditText getTimeEditText() {
        return (EditText) this.findViewById(R.id.in_time);
    }

}
