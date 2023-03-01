package com.rpap.taskmaster.activities;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskStatusEnum;
import com.amplifyframework.datastore.generated.model.task;
import com.amplifyframework.datastore.generated.model.taskTeam;
import com.rpap.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    public final static String TAG = "AddATaskActivity";
    Spinner taskStatusSpinner;
    Spinner taskTeamSpinner;

    CompletableFuture<List<taskTeam>> taskTeamFuture = new CompletableFuture<>();

    ArrayList<String> teamNames;
    ArrayList<taskTeam> taskTeam;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskStatusSpinner = findViewById(R.id.taskStatusSpinner);
        taskTeamSpinner = findViewById(R.id.addTaskActivityTeamSpinner);
        teamNames = new ArrayList<>();
        taskTeam = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(taskTeam.class),
                success -> {
                    Log.i(TAG, "Read taskTeam successfully!");
                    for (taskTeam databaseTaskTeam : success.getData()) {
                        teamNames.add(databaseTaskTeam.getName());
                        taskTeam.add(databaseTaskTeam);
                    }
                    taskTeamFuture.complete(taskTeam);
                    runOnUiThread(this::setUpSpinners);
                },
                failure -> {
                    taskTeamFuture.complete(null);
                    Log.e(TAG, "FAILED to read taskTeam" + failure);
                }
        );
        setUpSpinners();
        setUpSaveButton();

        Button addTaskButton = (Button) findViewById(R.id.addTaskActivityAddTaskButton);

//        public void setUpAddImageButton () {
//            findViewById(R.addTaskImagePicker).setOnClickListener(v -> {
//                    launchImageSelectionIntent();
//});
//        }
//
//        public void launchImageSelectionIntent() {
//            Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            imageFilePickingIntent.setType("*/*");
//            imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
//
//            activityResultLauncher.launch(imageFilePickingIntent);
//        }
//
//        private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher(){
//            ActivityResultLauncher<Intent> imagePickingActivityResultLauncher = registerForActivityResult(
//                    new ActivityResultContract<Intent, Object>() {
//                    }
//            )
//        }
//
//        public void uploadInputStreamToS3 () {
//
//        }
//
//        public void saveTask () {
//
//        }

    }

    public void setUpSpinners() {
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskStatusEnum.values()
        ));
        taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));
    }

    public void setUpSaveButton() {
        findViewById(R.id.addTaskActivityAddTaskButton).setOnClickListener(v -> {
            String selectedTaskTeamStringName = taskTeamSpinner.getSelectedItem().toString();
            try {
                taskTeam = (ArrayList<taskTeam>) taskTeamFuture.get();
            } catch (InterruptedException | ExecutionException ie) {
                ie.printStackTrace();
            }

            taskTeam selectedTeam = taskTeam.stream().filter(team -> team.getName().equals(selectedTaskTeamStringName)).findAny().orElseThrow(RuntimeException::new);

            task newTask = task.builder()
                    .title(((EditText) findViewById(R.id.addTaskActivityTaskTitleInput)).getText().toString())
                    .body(((EditText) findViewById(R.id.addTaskActivityTaskDescriptionInput)).getText().toString())
                    .status((TaskStatusEnum) taskStatusSpinner.getSelectedItem())
                    .taskTeam(selectedTeam)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    success -> Log.i(TAG, "Made A Task Successfully"),
                    failure -> Log.e(TAG, "FAILED To Make A Task", failure)
            );

            Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
        });
    }

}