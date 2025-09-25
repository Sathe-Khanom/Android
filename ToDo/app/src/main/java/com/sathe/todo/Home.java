package com.sathe.todo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<String> taskList;
//    private FloatingActionButton fabAddTask;

    private Button fabAddTask;

    private static final String TASKS_KEY = "tasks_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        fabAddTask = findViewById(R.id.fabAddTask);

        loadTasks();

//        taskList = new ArrayList<>();
//        taskList.add("Buy groceries");
//        taskList.add("Complete homework");
//        taskList.add("Call John");

        taskAdapter = new TaskAdapter(taskList, new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String currentTask) {
                showEditDialog(position, currentTask);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTask = "New Task " + (taskList.size() + 1);
                taskList.add(newTask);
                taskAdapter.notifyItemInserted(taskList.size() - 1);
                Toast.makeText(Home.this, "Task Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to show edit dialog
    public void showEditDialog(int position, String currentTask) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        final EditText input = new EditText(this);
        input.setText(currentTask);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String updatedTask = input.getText().toString().trim();
            if (!updatedTask.isEmpty()) {
                taskList.set(position, updatedTask);
                taskAdapter.notifyItemChanged(position);
                saveTasks();
                Toast.makeText(Home.this, "Task updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Home.this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

        // Save task list to SharedPreferences
        public void saveTasks() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();

            JSONArray jsonArray = new JSONArray();
            for (String task : taskList) {
                jsonArray.put(task);
            };

            editor.putString(TASKS_KEY, jsonArray.toString());
            editor.apply();
        }

        // Load task list from SharedPreferences
        public void loadTasks() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String tasksJson = prefs.getString(TASKS_KEY, null);

            taskList = new ArrayList<>();
            if (tasksJson != null) {
                try {
                    JSONArray jsonArray = new JSONArray(tasksJson);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        taskList.add(jsonArray.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


}