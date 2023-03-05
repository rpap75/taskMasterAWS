package com.rpap.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.taskTeam;
import com.rpap.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class UserSettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;

    public static final String SELECT_TEAM_TAG = "selectTeam";
    public static final String TAG = "settingsActivity";
    public static final String USERNAME_TAG = "Username";
    public static final String NICKNAME_TAG = "Nickname";

    CompletableFuture<List<taskTeam>> teamFuture = new CompletableFuture<>();
    Spinner teamSpinner;
    ArrayList<String> teamNames;
    ArrayList<taskTeam> team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        teamSpinner = findViewById(R.id.userSettingsActivityTeamSpinner);
        teamNames = new ArrayList<>();
        team = new ArrayList<>();

        String nickName = preferences.getString(NICKNAME_TAG, "");
        EditText nicknameEditText = findViewById(R.id.userSettingsActivityNicknameFormForm);
        nicknameEditText.setText(nickName);

        Amplify.API.query(
                ModelQuery.list(taskTeam.class),
                success -> {
                    Log.i(TAG, "Team Success!");
                    for (taskTeam databaseTeam : success.getData()) {
                        teamNames.add(databaseTeam.getName());
                        team.add(databaseTeam);
                    }
                    teamFuture.complete(team);
                    runOnUiThread(this::setUpTeamSpinner);
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.e(TAG, "FAILED to read team!" + failure);
                }
        );

        Intent callingIntent = getIntent();
        String userInputString = null;
        if (callingIntent != null) {
            userInputString = callingIntent.getStringExtra(MainActivity.TASK_INPUT_EXTRA_TAG);
        }

        TextView userInputTextView = findViewById(R.id.UserSettingsActivityUsernameTextView);
        if (userInputString != null) {
            userInputTextView.setText(userInputString);
        } else {
            userInputTextView.setText(USERNAME_TAG);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String username = preferences.getString(USERNAME_TAG, "");
        EditText usernameEditText = findViewById(R.id.userSettingsActivityUsernameEditTextView);
        usernameEditText.setText(username);

        setUpSaveButton();
    }
    public void setUpTeamSpinner(){
        teamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
    teamNames
        ));
    }

    public void setUpSaveButton() {
        @SuppressLint("CutPasteId") EditText nicknameEditText = findViewById(R.id.userSettingsActivityNicknameFormForm);
        Button saveButton = findViewById(R.id.userSettingsActivitySaveButton);
        EditText usernameEditText = findViewById(R.id.userSettingsActivityUsernameEditTextView);
        saveButton.setOnClickListener(v -> {

            SharedPreferences.Editor preferencesEditor = preferences.edit();
            String usernameString = usernameEditText.getText().toString();
            String selectTeamName = teamSpinner.getSelectedItem().toString();
            String nickNameString = nicknameEditText.getText().toString();

            preferencesEditor.putString(SELECT_TEAM_TAG, selectTeamName);
            preferencesEditor.putString(USERNAME_TAG, usernameString);
            preferencesEditor.putString(NICKNAME_TAG, nickNameString);
            preferencesEditor.apply();

            Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show();
        });
    }

}