package com.rpap.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.task;
import com.rpap.taskmaster.R;
import com.rpap.taskmaster.activities.TaskDetailActivity;

import java.util.List;

public class taskRecyclerViewAdapter extends RecyclerView.Adapter<taskRecyclerViewAdapter.taskViewHolder> {
    public static final String TASK_TITLE_TAG = "task_title";
    public static final String TASK_BODY_TAG = "task_body";
    public static final String TASK_STATUS_TAG = "task_status";
    public static final String TASK_TEAM_TAG = "task_team";
    public static final String TASK_IMAGE_KEY_TAG = "task_image_key";
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
        TextView tasksFragmentStatusView = holder.itemView.findViewById(R.id.tasksFragmentTextViewStatus);

        task task = taskList.get(position);

//        String taskTitle = taskList.get(position).getTitle();
//        String taskBody = taskList.get(position).getBody();
//        String taskStatus = String.valueOf(taskList.get(position).getStatus());
//        String taskTeam = String.valueOf(taskList.get(position).getTaskTeam().getName());
//        String taskImage = String.valueOf(taskList.get(position).task.getS3ImageKey());

        tasksFragmentTitleView.setText((position + 1) + ". " + task.getTitle() + "\n" + task.getTaskTeam()
        );
//        tasksFragmentStatusView.setText(taskStatus);
        View taskViewHolder = holder.itemView;

        taskViewHolder.setOnClickListener(v -> {
            Intent goToTaskDetailsIntent = new Intent(callingActivity, TaskDetailActivity.class);
            goToTaskDetailsIntent.putExtra(TASK_TITLE_TAG, task.getTitle());
            goToTaskDetailsIntent.putExtra(TASK_BODY_TAG, task.getBody());
            goToTaskDetailsIntent.putExtra(TASK_STATUS_TAG, task.getStatus());
            goToTaskDetailsIntent.putExtra(TASK_TEAM_TAG, task.getTaskTeam().getName());
            goToTaskDetailsIntent.putExtra(TASK_IMAGE_KEY_TAG, task.getS3ImageKey());

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
}