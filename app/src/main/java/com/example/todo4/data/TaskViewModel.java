package com.example.todo4.data;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.time.LocalDateTime;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void deleteCompletedTasks() {
        repository.deleteCompletedTasks();
    }

    // Method to handle deadline extension
    public void extendDeadline(Task task) {
        if (task.getExtensionCount() < 3) { // Maximum 3 extensions
            task.setExtensionCount(task.getExtensionCount() + 1);
            task.setEndTime(task.getEndTime().plusMinutes(15)); // 15-minute extension
            repository.update(task);
        }
    }

    // Method to calculate time remaining until end of day
    public long getTimeRemainingUntilEndOfDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);
        return java.time.Duration.between(now, endOfDay).toMinutes();
    }
}