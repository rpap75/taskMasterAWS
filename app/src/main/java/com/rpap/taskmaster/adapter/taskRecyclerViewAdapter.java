package com.rpap.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rpap.taskmaster.R;
import com.rpap.taskmaster.activities.TaskDetailActivity;
import com.rpap.taskmaster.model.task;

import java.util.List;

public class taskRecyclerViewAdapter extends RecyclerView.Adapter<taskRecyclerViewAdapter.taskViewHolder> {
    public static final String TASK_TITLE_TAG = "task_title";
    public static final String TASK_BODY_TAG = "task_body";
    public static final String TASK_STATUS_TAG = "task_status";

    Context callingActivity;

    List<task> taskList;

    public taskRecyclerViewAdapter(List<task> taskList, Context callingActivity) {
        this.taskList = taskList;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public taskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tasks, parent, false);
        return new taskViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull taskViewHolder holder, int position) {
        TextView tasksFragmentTitleView = holder.itemView.findViewById(R.id.tasksFragmentTextViewTitle);
//        TextView tasksFragmentBodyView = holder.itemView.findViewById(R.id.tasksFragmentTextViewBody);
        TextView tasksFragmentStatusView = holder.itemView.findViewById(R.id.tasksFragmentTextViewStatus);

//        TextView allTasksFragmentTitleView = holderAll.itemView.findViewById(R.id.allTasksFragmentTextViewTitle);
//        TextView allTasksFragmentBodyView = holderAll.itemView.findViewById(R.id.allTasksFragmentTextViewBody);
//        TextView allTasksFragmentStatusView = holderAll.itemView.findViewById(R.id.allTasksFragmentTextViewStatus);

        task task = taskList.get(position);

        String taskTitle = taskList.get(position).getTitle();
        String taskBody = taskList.get(position).getBody();
        String taskStatus = String.valueOf(taskList.get(position).getStatus());

        tasksFragmentTitleView.setText((position + 1) + ". " + taskTitle + "\n" + taskStatus);
//        tasksFragmentBodyView.setText(taskBody);
        tasksFragmentStatusView.setText(taskStatus);
        View taskViewHolder = holder.itemView;

//        allTasksFragmentTitleView.setText((position + 1) + ". " + taskTitle + "\n" + taskBody + "\n" + taskStatus);
//        allTasksFragmentBodyView.setText(taskBody);
//        allTasksFragmentStatusView.setText(taskStatus);
//        View allTaskViewHolder = holder.itemView;

//        allTaskViewHolder.setOnClickListener(v -> {
//            Intent goToTaskDetailsIntent = new Intent(callingActivity, TaskDetailActivity.class);
//            goToTaskDetailsIntent.putExtra(TASK_TITLE_TAG, taskTitle);
//            goToTaskDetailsIntent.putExtra(TASK_BODY_TAG, taskBody);
//            goToTaskDetailsIntent.putExtra(TASK_STATUS_TAG, taskStatus);
//            callingActivity.startActivity(goToTaskDetailsIntent);
//        });

        taskViewHolder.setOnClickListener(v -> {
            Intent goToTaskDetailsIntent = new Intent(callingActivity, TaskDetailActivity.class);
            goToTaskDetailsIntent.putExtra(TASK_TITLE_TAG, taskTitle);
            goToTaskDetailsIntent.putExtra(TASK_BODY_TAG, taskBody);
            goToTaskDetailsIntent.putExtra(TASK_STATUS_TAG, taskStatus);
            callingActivity.startActivity(goToTaskDetailsIntent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class taskViewHolder extends RecyclerView.ViewHolder {

        public taskViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


//    @Override
//    public int getItemCountAll() {
//        return taskList.size();
//    }
//
//    public static class allTaskViewHolder extends RecyclerView.ViewHolder {
//
//        public allTaskViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
}