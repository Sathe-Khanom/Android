package com.sathe.todo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private EditText etTask;
    private Button btnPickDate, btnAdd;
    private RecyclerView recyclerView;

    private List<Task> taskList;
    private TaskAdapter adapter;
    private TaskDatabaseHelper dbHelper;


    private String selectedDate = ""; // store user-selected date



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etTask = findViewById(R.id.etTask);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);



        dbHelper = new TaskDatabaseHelper(this);

        // Load saved tasks
        taskList = dbHelper.getAllTasks();

        adapter = new TaskAdapter(taskList, this, new TaskAdapter.TaskActionListener() {
            @Override
            public void onEdit(int position) {
                showEditDialog(position);
            }

            @Override
            public void onDelete(int position) {
                dbHelper.deleteTask(taskList.get(position).getId());
                taskList.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onChecked(int position, boolean isChecked) {
                Task t = taskList.get(position);
                t.setDone(isChecked);
                dbHelper.updateTask(t);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Pick Date Button
        btnPickDate.setOnClickListener(v -> {
            // Get current date as default
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Home.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth, // or Theme_DeviceDefault_Light_Dialog
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        btnPickDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // optional
            datePickerDialog.show();
        });

        // Add Task Button
        btnAdd.setOnClickListener(v -> {
            String taskTitle = etTask.getText().toString().trim();
            if (taskTitle.isEmpty()) {
                Toast.makeText(Home.this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedDate.isEmpty()) {
                Toast.makeText(Home.this, "Please pick a date", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task(taskTitle, selectedDate);
            long id = dbHelper.addTask(task);
            task.setId((int) id);

            taskList.add(task);
            adapter.notifyItemInserted(taskList.size() - 1);

            // Reset input
            etTask.setText("");
            selectedDate = "";
            btnPickDate.setText("Pick Date");
        });
    }

    // Edit Task Dialog
    private void showEditDialog(int position) {
        Task task = taskList.get(position);

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_task, null);

        EditText editTaskInput = dialogView.findViewById(R.id.editTaskInput);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdateTask);

        editTaskInput.setText(task.getTitle());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnUpdate.setOnClickListener(v -> {
            String updatedTitle = editTaskInput.getText().toString().trim();
            if (!updatedTitle.isEmpty()) {
                task.setTitle(updatedTitle);
                dbHelper.updateTask(task);
                adapter.notifyItemChanged(position);
                dialog.dismiss();
            } else {
                Toast.makeText(Home.this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


}