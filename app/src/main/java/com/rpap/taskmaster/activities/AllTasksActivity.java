package com.rpap.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.rpap.taskmaster.R;
import com.rpap.taskmaster.adapter.taskRecyclerViewAdapter;

import java.util.List;

public class AllTasksActivity extends AppCompatActivity {
//    taskMasterDatabase taskMasterDatabase;
//    public static final String DATABASE_NAME = "task_master";
//
//
//    List<task> taskList;
//    taskRecyclerViewAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all_tasks);
//
//        taskMasterDatabase = Room.databaseBuilder(
//                        getApplicationContext(),
//                        taskMasterDatabase.class,
//                        DATABASE_NAME)
//                .fallbackToDestructiveMigration() //DO NOT USE IN PRODUCTION
//                .allowMainThreadQueries()
//                .build();
//
//        taskList = taskMasterDatabase.taskDao().findAll();
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        setUpRecyclerView();
//
//    }
//
//    public void setUpRecyclerView() {
//
//        RecyclerView tasksRecyclerView = findViewById(R.id.allTaskActivityRecyclerViewTasks);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        tasksRecyclerView.setLayoutManager(layoutManager);
//
//        adapter = new taskRecyclerViewAdapter(taskList, this);
//        tasksRecyclerView.setAdapter(adapter);
//    }
}