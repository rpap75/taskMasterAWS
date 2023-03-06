package com.rpap.taskmaster.activities;

import static com.rpap.taskmaster.activities.UserSettingsActivity.NICKNAME_TAG;
import static com.rpap.taskmaster.activities.UserSettingsActivity.USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.SettingInjectorService;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.task;
import com.rpap.taskmaster.R;
import com.rpap.taskmaster.activities.AuthActivities.LoginActivity;
import com.rpap.taskmaster.adapter.taskRecyclerViewAdapter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
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

    private final MediaPlayer mp = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        logoutButton = findViewById(R.id.mainActivityButtonLogOut);

      int dateNow = new Date().getDate();

        AnalyticsEvent appStartedEvent = AnalyticsEvent.builder()
                .name("Application Started")
                .addProperty("Username", "rpapsin")
                .addProperty("TimeOfLaunch", dateNow)
                .build();

        Amplify.Analytics.recordEvent(appStartedEvent);

        Amplify.Predictions.convertTextToSpeech(
                "I like to eat spaghetti",
                result -> playAudio(result.getAudioData()),
                error -> Log.e("MyAmplifyApp", "Conversion failed", error)
        );

        setupButtons();
        setUpRecyclerView();

    }

    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
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


        adapter.setOnItemClickListener(new taskRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                taskList.remove(position);
                adapter.notifyItemChanged(position);
            }
        });

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
                    Intent goToLoginActivityIntent = new Intent(this, LoginActivity.class);
                    startActivity(goToLoginActivityIntent);
                }
        ));
    }
}