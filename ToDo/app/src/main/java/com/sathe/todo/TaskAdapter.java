package com.sathe.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<String> taskList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position, String currentTask);
    }

    public TaskAdapter(List<String> taskList, OnItemClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView;

        public TaskViewHolder(View itemView, OnItemClickListener listener, List<String> taskList) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.textViewTask);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, taskList.get(position));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView, listener, taskList);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String task = taskList.get(position);
        holder.taskTextView.setText(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
