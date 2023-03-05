package com.rpap.taskmaster.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    public static final String TASK_LOCATION_TAG = "task_location_key";

    private OnItemClickListener listener;

    Context callingActivity;
    List<task> taskList;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    public taskRecyclerViewAdapter(List<task> taskList, Context callingActivity) {
        this.taskList = taskList;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public taskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tasks, parent, false);



        return new taskViewHolder(taskFragment, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull taskViewHolder holder, int position) {
        TextView tasksFragmentTitleView = holder.itemView.findViewById(R.id.tasksFragmentTextViewTitle);
        TextView tasksFragmentStatusView = holder.itemView.findViewById(R.id.tasksFragmentTextViewStatus);

        task task = taskList.get(position);

        String taskStatus = String.valueOf(taskList.get(position).getStatus());

        tasksFragmentTitleView.setText((position + 1) + ". " + task.getTitle());
        tasksFragmentStatusView.setText(taskStatus);

        View taskViewHolder = holder.itemView;

        taskViewHolder.setOnClickListener(v -> {
            Intent goToTaskDetailsIntent = new Intent(callingActivity, TaskDetailActivity.class);
            goToTaskDetailsIntent.putExtra(TASK_TITLE_TAG, task.getTitle());
            goToTaskDetailsIntent.putExtra(TASK_BODY_TAG, task.getBody());
            goToTaskDetailsIntent.putExtra(TASK_STATUS_TAG, task.getStatus().toString());
            goToTaskDetailsIntent.putExtra(TASK_TEAM_TAG, task.getTaskTeam().getName());
            goToTaskDetailsIntent.putExtra(TASK_IMAGE_KEY_TAG, task.getS3ImageKey());
            goToTaskDetailsIntent.putExtra(TASK_LOCATION_TAG, task.getTaskLocation());

            callingActivity.startActivity(goToTaskDetailsIntent);
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class taskViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public taskViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.taskDeleteButton);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}