package com.rpap.taskmaster.activities;

import static com.rpap.taskmaster.activities.UserSettingsActivity.NICKNAME_TAG;
import static com.rpap.taskmaster.activities.UserSettingsActivity.USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import com.rpap.taskmaster.adapter.taskRecyclerViewAdapter;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "mainActivity";
    public static final String TASK_INPUT_EXTRA_TAG = "userTask";
    public static final String SELECT_TEAM_TAG = "selectTeam";

    SharedPreferences preferences;

    AuthUser authUser;
    Button logoutButton;
    List<task> taskList;
    taskRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        logoutButton = findViewById(R.id.mainActivityButtonLogOut);

        setupButtons();
        setUpRecyclerView();

    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    protected void onResume() {
        super.onResume();

        String selectedTeam = preferences.getString(SELECT_TEAM_TAG, "No team selected");

        Amplify.API.query(
                ModelQuery.list(task.class),
                success -> {
                    taskList.clear();
                    Log.i(TAG, "Read Tasks Successfully");
                    for (task databaseTask : success.getData()) {
                        if (databaseTask.getTaskTeam() != null) {
                            if (databaseTask.getTaskTeam().getName().equals(selectedTeam)) {
                                taskList.add(databaseTask);
                            }
                        }
                    }
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                },
                failure -> Log.e(TAG, "FAILED to read task from Database" + failure)
        );


        String username = preferences.getString(USERNAME_TAG, "no username");

        ((TextView) findViewById(R.id.mainActivityUsernameTextView)).setText(username + "'s");
        ((TextView) findViewById(R.id.mainActivityTeamTextView)).setText(selectedTeam);


        String nickname = preferences.getString(NICKNAME_TAG, "no nickname");
        ((TextView) findViewById(R.id.mainActivityTVNickName)).setText(nickname);

    }

    public void renderButtons() {

        if (authUser != null) {
            logoutButton.setVisibility(View.VISIBLE);
        } else {

            logoutButton.setVisibility(View.INVISIBLE);
        }
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
        final String[] username = {preferences.getString(USERNAME_TAG, "no username")};
//Add Task
        Button addTaskIntentButton = findViewById(R.id.mainActivityAddTaskButton);
        addTaskIntentButton.setOnClickListener(v -> {
            Intent goToAddTaskIntent = new Intent(this, AddTaskActivity.class);
            startActivity(goToAddTaskIntent);
        });

        ImageView settingsButton = findViewById(R.id.mainActivitySettingsImageView);
        settingsButton.setOnClickListener(v -> {
            Intent goToSettingsIntent = new Intent(this, UserSettingsActivity.class);
            startActivity(goToSettingsIntent);
        });

        Amplify.Auth.getCurrentUser(
                success -> {
                    Log.i(TAG, "Got Current User");
                    username[0] = success.getUsername();
                },
                failure -> {
                }
        );

        logoutButton.setOnClickListener(v -> Amplify.Auth.signOut(
                success -> {
                    Log.i(TAG, "User successfully logged out.");
                    authUser = null;
                    runOnUiThread(this::renderButtons);
                }
        ));
    }
}