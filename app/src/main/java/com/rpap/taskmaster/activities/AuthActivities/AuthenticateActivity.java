//package com.rpap.taskmaster.activities.AuthActivites;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import com.amplifyframework.core.Amplify;
//import com.rpap.taskmaster.R;
//
//public class AuthenticateActivity extends AppCompatActivity {
//
//    public static final String TAG = "LogInActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_authenticate);
//
//        setupButtons();
//
//    }
//
////        Amplify.Auth.signUp(
////                "ryanpapsin175@gmail.com"),
////                "p@assword";
////        AuthSignUpOptions.builder()
////                .userAttribute(AuthUserAttributeKey.email(), "ryanpapsin175@gmail.com")
////                .userAttribute(AuthUserAttributeKey.nickname())
////                .build();
////                success -> Log.i(TAG, "Sign Up Success"),
////                failure -> Log.e(TAG, "Sign Up Failed With Email: ryanpapsin175@gmail.com" + failure)
////        );
//
////        Amplify.Auth.confirmSignUp(
////                "ryanpapsin175@gmail.com",
////                "",
////                success -> Log.i(TAG, "Confirm Sign Up Success"),
////              failure -> Log.e(TAG, "Confirm Sign Up Failed With Email: ryanpapsin175@gmail.com" + failure)
////        );
////        Amplify.Auth.signIn(
////                "ryanpapsin175@gmail.com",
////                "",
////                success -> Log.i(TAG, "Confirm Log Up Success"),
////                failure -> Log.e(TAG, "Confirm Log Up Failed With Email: ryanpapsin175@gmail.com" + failure)
////        );
//
//        Amplify.Auth.fetchAuthSession(
//                success -> Log.i(TAG, "Current Auth Session" + success),
//                failure -> Log.e(TAG, "Failed To Fetch Auth Session" + failure)
//        );
//
//    }
