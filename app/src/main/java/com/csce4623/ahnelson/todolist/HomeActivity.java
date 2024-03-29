package com.csce4623.ahnelson.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Create HomeActivity and implement the OnClick listener
public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = ToDoListActivity.class.getName();

  //   ToDoListActivity toDoListAct = new ToDoListActivity();
    ListView listNotes;
    ArrayList<String> arrayNotes;
    ArrayAdapter<String> adapter;

    String hmTitle, hmContent, hmTime, hmDate;

    public HomeActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeComponents();
    }
    //Set the OnClick Listener for buttons
    void initializeComponents(){
        findViewById(R.id.btnNewNote).setOnClickListener(this);
        findViewById(R.id.btnDeleteNote).setOnClickListener(this);

         listNotes = (ListView) findViewById(R.id.all_toDo_lists);
         arrayNotes = new ArrayList<String>();
         adapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_gallery_item,
                 arrayNotes);

        listNotes.setAdapter(adapter);

        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(getApplicationContext(), "Item List Clicked! At position: " +
                            position + " and ID: " + id , Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            //If new Note, call createNewNote()
            case R.id.btnNewNote:
                this.goToDoListActivity();
               this.createNewNote(hmTitle, hmContent, hmDate, hmTime);
                break;
            //If delete note, call deleteNewestNote()
            case R.id.btnDeleteNote:
                deleteNewestNote();
                break;
            case R.id.btnSave:

                break;
            //This shouldn't happen
            default:
                break;
        }
    }
    void goToDoListActivity(){
        Intent toDoListIntent = new Intent(this, ToDoListActivity.class);
        startActivityForResult(toDoListIntent,  1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                hmTitle = data.getStringExtra("titleText");
                hmContent  = data.getStringExtra("contentText");
                hmDate = data.getStringExtra("dateText");
                hmTime = data.getStringExtra("timeText");


                //Add the resutls to the 2D Array
                addToArrayList(hmTitle, hmContent, hmDate,  hmTime);
            }
        }
    }

    void addToArrayList(String title, String notes, String date, String time){
        Log.i(TAG, "Debug:1) Title +notes before array!->" + title + ",    " +  notes +
                ",  " + date + ",  " + time);
        arrayNotes.add(title);
        Log.i(TAG, "Debug: 2)  after array!->" + title + ",    " +  notes +
                ",  " + date + ",  " + time);
        adapter.notifyDataSetChanged();
        Log.i(TAG, "Debug: 3) after notify change!->" + title +  ",    " +  notes +
                ",  " + date + ",  " + time);

    }


    //Create a new note with the title "New Note" and content "Note Content"
//    void createNewNote(){
//        //Create a ContentValues object
//        ContentValues myCV = new ContentValues();
//        //Put key_value pairs based on the column names, and the values
//        myCV.put(ToDoProvider.TODO_TABLE_COL_TITLE,"New Note");
//        myCV.put(ToDoProvider.TODO_TABLE_COL_CONTENT,"Note Content");
//        //Perform the insert function using the ContentProvider
//        getContentResolver().insert(ToDoProvider.CONTENT_URI,myCV);
//        //Set the projection for the columns to be returned
//        String[] projection = {
//                ToDoProvider.TODO_TABLE_COL_ID,
//                ToDoProvider.TODO_TABLE_COL_TITLE,
//                ToDoProvider.TODO_TABLE_COL_CONTENT};
//        //Perform a query to get all rows in the DB
//        Cursor myCursor = getContentResolver().query(ToDoProvider.CONTENT_URI,projection,null,null,null);
//        //Create a toast message which states the number of rows currently in the database
//        Toast.makeText(getApplicationContext(),Integer.toString(myCursor.getCount()),Toast.LENGTH_LONG).show();
//    }

    //Create a new note with the that passes a tittle and content as parameters
    void createNewNote(String title, String content, String date, String time){
        //Create a ContentValues object
        ContentValues myCV = new ContentValues();
        //Put key_value pairs based on the column names, and the values
        myCV.put(ToDoProvider.TODO_TABLE_COL_TITLE, title);
        myCV.put(ToDoProvider.TODO_TABLE_COL_CONTENT, content);
        myCV.put(ToDoProvider.TODO_TABLE_COL_DATE, date);
        myCV.put(ToDoProvider.TODO_TABLE_COL_TIME, time);
        //Perform the insert function using the ContentProvider
        getContentResolver().insert(ToDoProvider.CONTENT_URI,myCV);
        //Set the projection for the columns to be returned
        String[] projection = {
                ToDoProvider.TODO_TABLE_COL_ID,
                ToDoProvider.TODO_TABLE_COL_TITLE,
                ToDoProvider.TODO_TABLE_COL_CONTENT,
                ToDoProvider.TODO_TABLE_COL_DATE,
                ToDoProvider.TODO_TABLE_COL_TIME};
        //Perform a query to get all rows in the DB
        Cursor myCursor = getContentResolver().query(ToDoProvider.CONTENT_URI,projection,null,null,null);
        //Create a toast message which states the number of rows currently in the database
        Toast.makeText(getApplicationContext(),"content added! " + Integer.toString(myCursor.getCount()),Toast.LENGTH_LONG).show();

        //keeps track of array
       // this.addToArrayList(title, content, date , time);
    }

    //Delete the newest note placed into the database
    void deleteNewestNote(){
        //Create the projection for the query
        String[] projection = {
                ToDoProvider.TODO_TABLE_COL_ID,
                ToDoProvider.TODO_TABLE_COL_TITLE,
                ToDoProvider.TODO_TABLE_COL_CONTENT,
                ToDoProvider.TODO_TABLE_COL_DATE,
                ToDoProvider.TODO_TABLE_COL_TIME};

        //Perform the query, with ID Descending
        Cursor myCursor = getContentResolver().query(ToDoProvider.CONTENT_URI,projection,null,null,"_ID DESC");
        if(myCursor != null & myCursor.getCount() > 0) {
            //Move the cursor to the beginning
            myCursor.moveToFirst();
            //Get the ID (int) of the newest note (column 0)
            int newestId = myCursor.getInt(0);
            //Delete the note
            int didWork = getContentResolver().delete(Uri.parse(ToDoProvider.CONTENT_URI + "/" + newestId), null, null);
            //If deleted, didWork returns the number of rows deleted (should be 1)
            if (didWork == 1) {
                //If it didWork, then create a Toast Message saying that the note was deleted
                Toast.makeText(getApplicationContext(), "Deleted Note " + newestId, Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getApplicationContext(), "No Note to delete!", Toast.LENGTH_LONG).show();

        }
    }

}
