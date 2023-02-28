package com.rpap.taskmaster.activities.AuthActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        findViewById(R.id.mainActivityLogInButton).setOnClickListener(v -> {
            if (callingActivity != null) {
                userEmail = callingActivity.getStringExtra(SignUpActivity.USER_EMAIL);
                ((EditText)findViewById(R.id.loginActivityTVEmail)).setText(userEmail);
            } else {
                userEmail = ((EditText)findViewById(R.id.loginActivityTVEmail)).getText().toString();
            }
            String userPassword = ((EditText) findViewById(R.id.logInActivityTVPassword)).getText().toString();
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