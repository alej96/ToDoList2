package com.csce4623.ahnelson.todolist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String TAG = ToDoListActivity.class.getName();
    //LOGTAG set to Class Name
    private static String LOGTAG = "ToDoProvider:";
    // Database Name for SQLITE DB
    private static final String DBNAME = "ToDoDB";
    // Authority is the package name
    private static final String AUTHORITY = "com.csce4623.ahnelson.todolist.todoprovider";
    //TABLE_NAME is defined as ToDoList
    private static final String TABLE_NAME = "ToDoList3";
    //Create a CONTENT_URI for use by other classes
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/"+TABLE_NAME);

    //Column names for the ToDoList Table
    public static final String TABLE_COL_ID = "ID";
    public static final String TABLE_COL_TITLE = "TITLE";
    public static final String TABLE_COL_CONTENT = "CONTENT";
    public static final String TABLE_COL_DATE = "DATE";
    public static final String TABLE_COL_TIME = "TIME";
    public static final Boolean TABLE_COL_DONE =  false;

    public DatabaseHelper( Context context) {

        super(context,TABLE_NAME , null, 1);
    }

    //Table create string based on column names
    @Override
    public void onCreate(SQLiteDatabase db) {

/*        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_COL_TITLE + " TEXT NOT NULL, " +
                TABLE_COL_CONTENT + " TEXT NOT NULL);" ;*/


        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_COL_TITLE + " TEXT NOT NULL, " +
                TABLE_COL_CONTENT + " TEXT NOT NULL, " +
                TABLE_COL_DATE +  " TEXT NOT NULL, " +
                TABLE_COL_TIME + " TEXT NOT NULL);" ;
    Log.i(TAG, "Table Created: " + createTable);
        db.execSQL(createTable);

    }

    //SQL state that drops if sql exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }

    //insert data to database
    public boolean addData(String item, String colName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //add data to specific column
        contentValues.put(colName, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void insertData(String title, String content){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_NAME + " ( " +
               TABLE_COL_TITLE + " , " + TABLE_COL_CONTENT + " ) VALUES ( '" +
               title + "' , '" + content +"' );";
        Log.d(TAG, "updateName: query: " + query);
        db.execSQL(query);
    }

    public void insertData(String title, String content, String date, String time){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_NAME + " ( " +
                TABLE_COL_TITLE + " , " + TABLE_COL_CONTENT + " , " +
                TABLE_COL_DATE  + " , " + TABLE_COL_TIME +" ) " +

                "VALUES ( '" + title + "' , '" + content +  "' , '" +
                        date +  "' , '" + time + "' );";
        Log.d(TAG, "updateName: query: " + query);
        db.execSQL(query);
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + TABLE_COL_ID + " FROM " + TABLE_NAME +
                " WHERE " + TABLE_COL_TITLE + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     */
    public void updateData(String newName, int id, String oldName, String colName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + colName +
                " = '" + newName + "' WHERE " + TABLE_COL_ID + " = '" + id + "'" +
                " AND " + colName + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }


    /**
     * Delete from database
     */
    public void deleteName(int id, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + TABLE_COL_ID + " = '" + id + "'" +
                " AND " + TABLE_COL_TITLE + " = '" + title + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + title + " from database.");
        db.execSQL(query);
    }

    /**
     * Get data point base in ID from database
     */

    public String getDataPoint(int itemID, String colName) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + colName  +" FROM " + TABLE_NAME +
                " WHERE " + TABLE_COL_ID + " = '" + itemID + "';";

        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        String output =  data.getString(data.getColumnIndex(colName));
        return output;
    }

    public Cursor getDataPoint(int itemID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + TABLE_COL_ID + " = '" + itemID + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }
    /**
     * Last Row from database
     */

//    public int lastRow( ){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT COUNT(*) FROM "+ TABLE_NAME + ";";
//
//
//    }
}
