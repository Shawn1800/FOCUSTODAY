package com.example.todo4;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo4.data.Task;
import com.example.todo4.data.TaskAdapter;
import com.example.todo4.data.TaskViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.LocalDateTime;
import java.util.List;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TimePicker;


public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private final Handler deadlineCheckHandler = new Handler();
    private TaskViewModel taskViewModel;
    private TextView timeRemainingTextView;
    private TaskAdapter adapter;  // Make adapter a class field

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initializeMediaPlayer();

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Set up adapter
        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Observe tasks
        taskViewModel.getAllTasks().observe(this, adapter::setTasks);

        // Set up time remaining display
        timeRemainingTextView = findViewById(R.id.text_view_time_remaining);
        updateTimeRemaining();

        // Set up FAB for adding tasks
        ExtendedFloatingActionButton buttonAddTask = findViewById(R.id.button_add_task);
        buttonAddTask.setOnClickListener(v -> showAddTaskDialog());

        // Set up click listeners
        setupClickListeners();


    }
    private void initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
    }


    private void setupClickListeners() {
        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                task.setCompleted(!task.isCompleted());
                if (task.isCompleted()) {
                    stopAlarm();  // Stop alarm when marked as done
                    task.setHasAlerted(true);
                }
                taskViewModel.update(task);
            }
            @Override
            public void onShiftClick(Task task) {
                if (task.getExtensionCount() < 3) {  // Limit to 3 extensions
                    LocalDateTime newDeadline = task.getEndTime().plusHours(1);
                    task.setEndTime(newDeadline);
                    task.setExtensionCount(task.getExtensionCount() + 1);
                    taskViewModel.update(task);

                    Toast.makeText(MainActivity.this,
                            "Deadline extended by 1 hour",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Maximum extensions reached",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onDeleteClick(Task task) {
                taskViewModel.delete(task);
            }
        });


    }

    private void updateTimeRemaining() {
        long minutesRemaining = taskViewModel.getTimeRemainingUntilEndOfDay();
        String timeText = String.format("Time remaining today: %d hours %d minutes",
                minutesRemaining / 60, minutesRemaining % 60);
        timeRemainingTextView.setText(timeText);
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);

        EditText editTextTitle = dialogView.findViewById(R.id.edit_text_task_title);
        TimePicker startTimePicker = dialogView.findViewById(R.id.start_time_picker);
        TimePicker endTimePicker = dialogView.findViewById(R.id.end_time_picker);

        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

        builder.setView(dialogView)
                .setTitle("Add New Task")
                .setPositiveButton("Add", (dialog, which) -> {
                    String taskTitle = editTextTitle.getText().toString().trim();
                    if (!taskTitle.isEmpty()) {
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime startTime = now.withHour(startTimePicker.getHour())
                                .withMinute(startTimePicker.getMinute());
                        LocalDateTime endTime = now.withHour(endTimePicker.getHour())
                                .withMinute(endTimePicker.getMinute());

                        // Validate times
                        if (startTime.isAfter(endTime)) {
                            Toast.makeText(this, "Start time must be before end time",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Task newTask = new Task(taskTitle, startTime, endTime);
                        taskViewModel.insert(newTask);
                    }
                })
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }
    private Handler timeUpdateHandler = new Handler();
    private Runnable timeUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeRemaining();
            timeUpdateHandler.postDelayed(this, 60000); // Update every minute
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        timeUpdateHandler.post(timeUpdateRunnable);
        deadlineCheckHandler.post(deadlineChecker);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeUpdateHandler.removeCallbacks(timeUpdateRunnable);
        deadlineCheckHandler.removeCallbacks(deadlineChecker);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



    private final Runnable deadlineChecker = new Runnable() {
        @Override
        public void run() {
            checkDeadlines();
            deadlineCheckHandler.postDelayed(this, 60000); // Check every minute
        }
    };


    private void checkDeadlines() {
        List<Task> tasks = taskViewModel.getAllTasks().getValue();
        if (tasks != null) {
            LocalDateTime now = LocalDateTime.now();
            for (Task task : tasks) {
                // Only check if task is not completed, hasn't alerted, and is past deadline
                if (!task.isCompleted() && !task.hasAlerted() && task.getEndTime().isBefore(now)) {
                    playDeadlineAlarm();
                    Toast.makeText(this,
                            "Task deadline reached: " + task.getTitle(),
                            Toast.LENGTH_LONG).show();

                    task.setHasAlerted(true);
                    taskViewModel.update(task);

                    // Stop checking this task once it's marked as completed
                    if (task.isCompleted()) {
                        task.setHasAlerted(true);
                        taskViewModel.update(task);
                    }
                }
            }
        }
    }

    private void playDeadlineAlarm() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // Stop after 5 seconds
            new Handler().postDelayed(() -> {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            }, 10000); // 5000 milliseconds = 5 seconds
        }
    }

    private void stopAlarm() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }





}


