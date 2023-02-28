package com.rpap.taskmaster.activities;

import static com.rpap.taskmaster.activities.UserSettingsActivity.USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.task;
import com.rpap.taskmaster.R;
import com.rpap.taskmaster.activities.AuthActivites.LoginActivity;
import com.rpap.taskmaster.activities.AuthActivites.SignUpActivity;
import com.rpap.taskmaster.adapter.taskRecyclerViewAdapter;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "mainActivity";

    List<task> taskList;
    taskRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButtons();
        setUpRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Amplify.API.query(
                ModelQuery.list(task.class),
                success -> {
                    taskList.clear();
                    Log.i(TAG, "Read Tasks Successfully");
                    for (task databaseTask : success.getData()) {
                        taskList.add(databaseTask);
//                        String selectedTeamName = "red";
//                        if (databaseTask.getTaskTeam() != null) {
//
//                            if (databaseTask.getTaskTeam().getName().equals(selectedTeamName)) {
//                                taskList.add(databaseTask);
//                            }
//                        }
                    }
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                },
                failure -> Log.e(TAG, "FAILED to read task from Database")
        );

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String username = preferences.getString(USERNAME_TAG, "no username");

        ((TextView) findViewById(R.id.mainActivityUsernameTextView)).setText(username + "'s");


//        SharedPreferences teamSelect = PreferenceManager.getDefaultSharedPreferences(this);
//
//        String team = teamSelect.getString(TEAM_TAG, "no team");
//
//        ((TextView) findViewById(R.id.mainActivityTeamTextView)).setText(team);
    }

    public void setUpRecyclerView() {

        taskList = new ArrayList<>();
        RecyclerView tasksRecyclerView = findViewById(R.id.mainActivityRecyclerViewTasks);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager(layoutManager);

        adapter = new taskRecyclerViewAdapter(taskList, this);
        tasksRecyclerView.setAdapter(adapter);
    }

    public void setupButtons() {
//Add Task
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

        Amplify.Auth.getCurrentUser(
                success -> {
                    Log.i(TAG, "GotCurrent User");
                    username.set(success.getUsername());
                }
                failure -> {}
        );

        if (username.equals("")) {
            ((Button)findViewById(R.id.mainActivitySignUpButton)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.mainActivityLogInButton)).setVisibility(View.VISIBLE);
            // hide log out button
        } else {
            ((Button)findViewById(R.id.mainActivitySignUpButton)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.mainActivityLogInButton)).setVisibility(View.INVISIBLE);
        }

        //Login
        findViewById(R.id.mainActivityLogInButton).setOnClickListener(v -> {
            Intent goToLoginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(goToLoginActivityIntent);
        });

        //Sign Up
        findViewById(R.id.mainActivitySignUpButton).setOnClickListener(v -> {
            Intent goToSignUpActivityIntent = new Intent(this, SignUpActivity.class);
            startActivity(goToSignUpActivityIntent);
        });


    }
}