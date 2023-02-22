package com.rpap.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskStatusEnum;
import com.amplifyframework.datastore.generated.model.task;
import com.rpap.taskmaster.R;

public class AddTaskActivity extends AppCompatActivity {

    public final static String TAG = "AddATaskActivity";

    Spinner taskStatusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskStatusSpinner = findViewById(R.id.taskStatusSpinner);


        setUpStatusSpinner();
        setUpSaveButton();
    }

    public void setUpStatusSpinner() {
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskStatusEnum.values()
        ));
    }

    public void setUpSaveButton() {
        findViewById(R.id.addTaskActivityAddTaskButton).setOnClickListener(v -> {
            task newTask = task.builder()
                    .title(((EditText) findViewById(R.id.addTaskActivityTaskTitleInput)).getText().toString())
                    .body(((EditText) findViewById(R.id.addTaskActivityTaskDescriptionInput)).getText().toString())
                    .status((TaskStatusEnum) taskStatusSpinner.getSelectedItem())
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    success -> Log.i(TAG, "Made S Task Successfully"),
                    failure -> Log.e(TAG, "FAILED To Make A Task", failure)
            );

            Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
        });
    }

}