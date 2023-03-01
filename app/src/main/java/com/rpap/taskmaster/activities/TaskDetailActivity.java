package com.rpap.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.rpap.taskmaster.R;
import com.rpap.taskmaster.adapter.taskRecyclerViewAdapter;

public class TaskDetailActivity extends AppCompatActivity {
    Context callingActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        consumeExtras();
    }
    public void consumeExtras(){
        Intent callingIntent = getIntent();
        String taskTitle = null;
        String taskBody = null;
        String taskStatus = null;
        String selectedTeam = null;
        if (callingIntent != null) {
            taskTitle = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_TITLE_TAG);
            taskBody = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_BODY_TAG);
            taskStatus = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_STATUS_TAG);
            selectedTeam = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_TEAM_TAG);
        }
        ((TextView) findViewById(R.id.taskDetailActivityTitle)).setText(taskTitle);
        ((TextView) findViewById(R.id.taskDetailActivityBody)).setText(taskBody);
        ((TextView) findViewById(R.id.taskDetailActivityStatus)).setText(taskStatus);
        ((TextView) findViewById(R.id.taskDetailActivityTVTeam)).setText(selectedTeam);
    }
}

