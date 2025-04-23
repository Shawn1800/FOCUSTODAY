
package com.example.todo4.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDateTime;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private LocalDateTime endTime;
    private LocalDateTime startTime;
    private boolean isCompleted;
    private int extensionCount;
    private boolean hasAlerted=false;
    // Add this field


    // Add getter and setter





    // Constructor
    public Task(String title, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.endTime = endTime;
        this.startTime = startTime;
        this.isCompleted = false;
        this.extensionCount = 0;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }


    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public int getExtensionCount() { return extensionCount; }
    public void setExtensionCount(int extensionCount) { this.extensionCount = extensionCount; }

    public boolean hasAlerted() { return hasAlerted; }
    public void setHasAlerted(boolean hasAlerted) { this.hasAlerted = hasAlerted; }

}

