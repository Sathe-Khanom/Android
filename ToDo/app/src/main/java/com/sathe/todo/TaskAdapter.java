package com.sathe.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private Context context;

    public interface TaskActionListener {
        void onEdit(int position);
        void onDelete(int position);
        void onChecked(int position, boolean isChecked);
    }

    private TaskActionListener listener;

    public TaskAdapter(List<Task> taskList, Context context, TaskActionListener listener) {
        this.taskList = taskList;
        this.context = context;
        this.listener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbTask;
        ImageButton btnEdit, btnDelete;
        TextView tvDate; // new field

        public TaskViewHolder(View itemView) {
            super(itemView);
            cbTask = itemView.findViewById(R.id.cbTask);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvDate = itemView.findViewById(R.id.tvDate); // bind
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        // Clear previous listener
        holder.cbTask.setOnCheckedChangeListener(null);

        // Set task title and checkbox state
        holder.cbTask.setText(task.getTitle());
        holder.cbTask.setChecked(task.isDone());

        // Set date
        holder.tvDate.setText(task.getDate());

        // Re-attach checkbox listener
        holder.cbTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onChecked(position, isChecked);
        });

        // Edit button click
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(position));

        // Delete button click
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
