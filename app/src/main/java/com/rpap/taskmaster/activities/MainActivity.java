package com.rpap.taskmaster.activities;

import static com.rpap.taskmaster.activities.AuthActivities.SignUpActivity.NICKNAME_TAG;
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
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.task;
import com.rpap.taskmaster.R;
import com.rpap.taskmaster.activities.AuthActivities.LoginActivity;
import com.rpap.taskmaster.activities.AuthActivities.SignUpActivity;
import com.rpap.taskmaster.adapter.taskRecyclerViewAdapter;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "mainActivity";
    public static final String TASK_INPUT_EXTRA_TAG = "userTask";
    public static final String SELECT_TEAM_TAG = "selectTeam";

    SharedPreferences preferences;

    List<task> taskList;
    taskRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setupButtons();
        setUpRecyclerView();

//       //manual file upload to s3
//            File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");
//
//            try {
//                BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
//                writer.append("Example file contents");
//                writer.close();
//            } catch (Exception exception) {
//                Log.e("MyAmplifyApp", "Upload failed", exception);
//            }
//
//            Amplify.Storage.uploadFile(
//                    "ExampleKey",
//                    exampleFile,
//                    success -> Log.i("MyAmplifyApp", "Successfully uploaded: " + success.getKey()),
//                   failure -> Log.e("MyAmplifyApp TEST", "Upload failed", failure)
//            );
        }

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
                        taskList.add(databaseTask);
                        String selectedTeamName = selectedTeam;
                        if (databaseTask.getTaskTeam() != null) {
                            if (databaseTask.getTaskTeam().getName().equals(selectedTeamName)) {
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
        ((TextView)findViewById(R.id.mainActivityTeamTextView)).setText(selectedTeam);


        String nickname = preferences.getString(NICKNAME_TAG, "no nickname");

        ((TextView) findViewById(R.id.mainActivityTVNickName)).setText(nickname);
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
        final String[] nickname = {preferences.getString(NICKNAME_TAG, "no nickname")};
        final String[] username = {preferences.getString(USERNAME_TAG, "no username")};
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
                    Log.i(TAG, "Got Current User");
                    username[0] = success.getUsername();
                },
                failure -> {}
        );


//        logoutButton.setOnClickListener(v -> {
//            Amplify.Auth.signOut(
//                    success -> {
//                        Log.i(TAG, "User successfully logged out.");
//                        authUser = null;
//                        runOnUiThread(this::renderButtons);
//                    }
//            );
//        });

        if (username[0].equals("")) {
            ((Button)findViewById(R.id.mainActivitySignUpButton)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.mainActivityLogInButton)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.mainActivityButtonLogOut)).setVisibility(View.INVISIBLE);
            // hide log out button
        } else {
            ((Button)findViewById(R.id.mainActivitySignUpButton)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.mainActivityLogInButton)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.mainActivityButtonLogOut)).setVisibility(View.VISIBLE);
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