package com.rpap.taskmaster.activities.AuthActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.rpap.taskmaster.R;

public class SignUpActivity extends AppCompatActivity {

    public final String TAG = "sign_up_activity";
    public static final String USER_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpSignUpButton();
    }

    public void setUpSignUpButton() {
        findViewById(R.id.signUpActivitySignUpButton).setOnClickListener(view -> {
            String userEmail = ((EditText) findViewById(R.id.signUpActivityTVEmail)).getText().toString();
            String userPassword = ((EditText) findViewById(R.id.signUpActivityTVPassword)).getText().toString();
            String userNickname = ((EditText) findViewById(R.id.signUpActivityTVNickname)).getText().toString();

            Amplify.Auth.signUp(
                    userEmail,
                    userPassword,
                    userNickname,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), userEmail)
                            .userAttribute(AuthUserAttributeKey.nickname(), userNickname)
                            .build(),
                    success -> Log.i(TAG, "Sign Up Success"),
                    failure -> Log.e(TAG, "Sign Up Failed With Email: " + userEmail + failure)
            );
            Intent goToConfirmSignUpActivity = new Intent(this, VerifySignUpActivity);
            goToConfirmSignUpActivity.putExtra(USER_EMAIL, userEmail);
            startActivities(goToConfirmSignUpActivity);
        });
    }
}
