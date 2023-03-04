package com.rpap.taskmaster.activities.AuthActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;
import com.rpap.taskmaster.R;
import com.rpap.taskmaster.activities.MainActivity;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "login_activity";
    Intent callingActivity;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callingActivity = getIntent();


        setUpLoginButton();
    }

    public void setUpLoginButton() {

        Button addTaskIntentButton = findViewById(R.id.logInActivitySignUpButton);
        addTaskIntentButton.setOnClickListener(v -> {
            Intent goToAddTaskIntent = new Intent(this, SignUpActivity.class);
            startActivity(goToAddTaskIntent);
        });

        findViewById(R.id.logInActivityLogInButton).setOnClickListener(v -> {
            if (callingActivity != null && callingActivity.getStringExtra(SignUpActivity.USER_EMAIL)!= null) {
                userEmail = callingActivity.getStringExtra(SignUpActivity.USER_EMAIL);
                ((EditText)findViewById(R.id.logInActivityEmailForm)).setText(userEmail);
            } else {
                userEmail = ((EditText)findViewById(R.id.logInActivityEmailForm)).getText().toString();
            }
            String userPassword = ((EditText) findViewById(R.id.logInActivityPasswordForm)).getText().toString();
            Amplify.Auth.signIn(
                    userEmail,
                    userPassword,
                    success -> Log.i(TAG, "Successfully Logged In User: " + userEmail),
                    failure -> Log.i(TAG, "Failed to login user: " + userEmail + "with error code:" + failure)
            );
            Intent goToMainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(goToMainActivityIntent);
        });
    }
}