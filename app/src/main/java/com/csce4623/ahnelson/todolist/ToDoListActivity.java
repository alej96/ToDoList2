package com.csce4623.ahnelson.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
    }

    public void saveNewNote(){
        Toast.makeText(getApplicationContext(),"Saved Note!",Toast.LENGTH_LONG).show();
    }

    void goToHomeActivity(){
        Intent toDoListIntent = new Intent(this, HomeActivity.class);
        startActivity(toDoListIntent);
    }


    public void onClick(View v){
        switch (v.getId()){
            //If save clicked, go back to main activity and add the reminder
            case R.id.btnSave:
               saveNewNote();
                goToHomeActivity();
                break;
            //This shouldn't happen
            default:
                break;
        }
    }
}
