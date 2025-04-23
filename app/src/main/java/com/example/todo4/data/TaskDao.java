package com.example.todo4.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.time.LocalDateTime;
import java.util.List;




@Dao

public interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY startTime ASC")
    LiveData<List<Task>> getAllTasks();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks WHERE isCompleted = 1")
    void deleteCompletedTasks();
}