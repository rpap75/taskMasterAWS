package com.rpap.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.rpap.taskmaster.R;
import com.rpap.taskmaster.model.task;

public class AddTaskActivity extends AppCompatActivity {


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
                task.taskStatusEnum.values()
        ));
    }

    public void setUpSaveButton() {
        findViewById(R.id.addTaskActivityAddTaskButton).setOnClickListener(v -> {
            task newTask = new task(
                    ((EditText)findViewById(R.id.addTaskActivityTaskTitleInput)).getText().toString(),
                    ((EditText)findViewById(R.id.addTaskActivityTaskDescriptionInput)).getText().toString(),
                    task.taskStatusEnum.fromString(taskStatusSpinner.getSelectedItem().toString())
                    );
//            taskMasterDatabase.taskDao().insertATask(newTask); TODO This is where Amplify calls will go
            Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
        });
    }

}