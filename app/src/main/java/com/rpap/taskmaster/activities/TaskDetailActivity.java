package com.rpap.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.rpap.taskmaster.R;
import com.rpap.taskmaster.adapter.taskRecyclerViewAdapter;

import java.io.File;

public class TaskDetailActivity extends AppCompatActivity {
    public final static String TAG = "task_details";

    Context callingActivity;
    String taskTitle = null;
    String taskBody = null;
    String taskStatus = null;
    String selectedTeam = null;
    String taskImageKey = null;
    String taskLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        consumeExtras();

        if (taskImageKey != null && !taskImageKey.isEmpty()) {
            Amplify.Storage.downloadFile(
                    taskImageKey,
                    new File(getApplication().getFilesDir(), taskImageKey),
                    success -> {
                        Log.i(TAG, "SUCCESS! Got The Image With Key: " + taskImageKey);
                        ImageView taskImageView = findViewById(R.id.taskDetailActivityImageViewImage);
                        taskImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure -> Log.e(TAG, "FAILED! Unable To Get Image From S3 With The Key " + taskImageKey + "with error " + failure)
            );
        }
    }

    public void consumeExtras() {
        Intent callingIntent = getIntent();
        if (callingIntent != null) {
            taskTitle = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_TITLE_TAG);
            taskBody = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_BODY_TAG);
            taskStatus = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_STATUS_TAG);
            selectedTeam = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_TEAM_TAG);
            taskImageKey = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_IMAGE_KEY_TAG);
            taskLocation = callingIntent.getStringExtra(taskRecyclerViewAdapter.TASK_LOCATION_TAG);
        }
        ((TextView) findViewById(R.id.taskDetailActivityTitle)).setText(taskTitle);
        ((TextView) findViewById(R.id.taskDetailActivityBody)).setText(taskBody);
        ((TextView) findViewById(R.id.taskDetailActivityStatus)).setText(taskStatus);
        ((TextView) findViewById(R.id.taskDetailActivityTVTeam)).setText(selectedTeam);
        ((TextView) findViewById(R.id.taskDetailActivityLocationTV)).setText(taskLocation);
    }
}

