package com.rpap.taskmaster.activities.AuthActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;
import com.rpap.taskmaster.R;

public class VerifySignUpActivity extends AppCompatActivity {

    public static final String TAG = "verify_sign_up_activity ";
    Intent callingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_sign_up);
        callingIntent = getIntent();

        setUpConfirmButton();
    }

    public void setUpConfirmButton() {
        findViewById(R.id.verifyActivitySubmitButton).setOnClickListener(view -> {
            String userEmail = "";
            if (callingIntent != null) {
                userEmail = callingIntent.getStringExtra(SignUpActivity.USER_EMAIL);
            }
            String confirmationCode = ((EditText) findViewById(R.id.verifySignUpActivityFormConfirmationCode)).getText().toString();
            String finalUserEmail = userEmail;
            Amplify.Auth.confirmSignUp(
                    userEmail,
                    confirmationCode,
                    success -> Log.i(TAG, "Confirmation Successful With User: " + finalUserEmail),
                    failure -> Log.e(TAG, "Failed To Verify User Confirmation Code: " + confirmationCode + " for user: " + finalUserEmail + "with failure: " + failure)
            );
            Intent goToLoginActivityIntent = new Intent(this, LoginActivity.class);
            goToLoginActivityIntent.putExtra(SignUpActivity.USER_EMAIL, userEmail);
            startActivity(goToLoginActivityIntent);
        });
    }
}