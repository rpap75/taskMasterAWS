package com.rpap.taskmaster.activities;

import static com.rpap.taskmaster.activities.UserSettingsActivity.USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rpap.taskmaster.R;
import com.rpap.taskmaster.adapter.taskRecyclerViewAdapter;
import com.rpap.taskmaster.model.task;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<task> taskList;
    taskRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskList = new ArrayList<>();


        taskList.add(new task("Test 1", "Test1 Test1", task.taskStatusEnum.ASSIGNED));
        taskList.add(new task("Test 2", "Test2 Test2", task.taskStatusEnum.ASSIGNED));

        setupButtons();
        setUpRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
//            taskList.addAll(taskMasterDatabase.taskDao().findAll()); TODO This is where Amplify call will go
        adapter.notifyDataSetChanged();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String username = preferences.getString(USERNAME_TAG, "no username");

        ((TextView) findViewById(R.id.mainActivityUsernameTextView)).setText(username + "'s");

    }

    public void setUpRecyclerView() {

        RecyclerView tasksRecyclerView = findViewById(R.id.mainActivityRecyclerViewTasks);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager(layoutManager);

        adapter = new taskRecyclerViewAdapter(taskList, this);
        tasksRecyclerView.setAdapter(adapter);
    }

    public void setupButtons() {

        Button addTaskIntentButton = (Button) findViewById(R.id.mainActivityAddTaskButton);
        addTaskIntentButton.setOnClickListener(v -> {
            Intent goToAddTaskIntent = new Intent(this, AddTaskActivity.class);
            startActivity(goToAddTaskIntent);
        });
        Button allTasksIntentButton = (Button) findViewById(R.id.mainActivityAllTasksButton);
        allTasksIntentButton.setOnClickListener(v -> {
            Intent goToAllTasksIntent = new Intent(this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });

        ImageView settingsButton = (ImageView) findViewById(R.id.mainActivitySettingsImageView);
        settingsButton.setOnClickListener(v -> {
            Intent goToSettingsIntent = new Intent(this, UserSettingsActivity.class);
            startActivity(goToSettingsIntent);
        });
    }
}