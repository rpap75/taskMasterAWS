package com.rpap.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.rpap.taskmaster.R;

public class UserSettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    public static final String USERNAME_TAG = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String username = preferences.getString(USERNAME_TAG, "");
        EditText usernameEditText = findViewById(R.id.userSettingsActivityUsernameEditTextView);
        usernameEditText.setText(username);

        Button saveButton = findViewById(R.id.userSettingsActivitySaveButton);
        saveButton.setOnClickListener(v -> {
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            String usernameString = usernameEditText.getText().toString();

            preferencesEditor.putString(USERNAME_TAG, usernameString);
            preferencesEditor.apply();

//            Snackbar.make(findViewById(R.id.userSettingsActivity), "Settings Saved!", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show();
        });
    }
}