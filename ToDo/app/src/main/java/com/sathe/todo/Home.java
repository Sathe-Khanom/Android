package com.sathe.todo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private EditText etTask;
    private Button btnAdd;
    private RecyclerView recyclerView;

    private List<Task> taskList;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etTask = findViewById(R.id.etTask);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList, this, new TaskAdapter.TaskActionListener() {
            @Override
            public void onEdit(int position) {
                showEditDialog(position);
            }

            @Override
            public void onDelete(int position) {
                taskList.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onChecked(int position, boolean isChecked) {
                taskList.get(position).setDone(isChecked);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String taskTitle = etTask.getText().toString().trim();
            if (!taskTitle.isEmpty()) {
                taskList.add(new Task(taskTitle));
                adapter.notifyItemInserted(taskList.size() - 1);
                etTask.setText("");
            }
        });
    }

    private void showEditDialog(int position) {
        Task task = taskList.get(position);

        // Inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_task, null);

        EditText editTaskInput = dialogView.findViewById(R.id.editTaskInput);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdateTask);

        // Set current task title
        editTaskInput.setText(task.getTitle());

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Handle Update button click
        btnUpdate.setOnClickListener(v -> {
            String updatedTitle = editTaskInput.getText().toString().trim();
            if (!updatedTitle.isEmpty()) {
                task.setTitle(updatedTitle);
                adapter.notifyItemChanged(position);
                dialog.dismiss();
            } else {
                Toast.makeText(Home.this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        dialog.show();
    }



}