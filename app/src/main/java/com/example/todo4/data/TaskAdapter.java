package com.example.todo4.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.example.todo4.R;
import com.google.android.material.button.MaterialButton;

import android.graphics.Paint;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");  // 12-hour format with AM/PM
    private OnItemClickListener listener;  // Move this here

    // Move interface here
    public interface OnItemClickListener {
        void onItemClick(Task task);
        void onShiftClick(Task task);
        void onDeleteClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);

        // Set task title with larger text
        holder.titleTextView.setText(currentTask.getTitle());

        // Format and display both start and end times in 12-hour format
        String startTime = currentTask.getStartTime().format(TIME_FORMATTER);
        String endTime = currentTask.getEndTime().format(TIME_FORMATTER);
        holder.deadlineTextView.setText("Time: " + startTime + " - " + endTime);

        // Show extension count if any
        if (currentTask.getExtensionCount() > 0) {
            holder.extensionTextView.setVisibility(View.VISIBLE);
            holder.extensionTextView.setText("Extended: " + currentTask.getExtensionCount() + " times");
        } else {
            holder.extensionTextView.setVisibility(View.GONE);
        }

        // Show completed tasks differently
        if (currentTask.isCompleted()) {
            holder.itemView.setAlpha(0.5f);
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView deadlineTextView;
        private TextView extensionTextView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            deadlineTextView = itemView.findViewById(R.id.text_view_deadline);
            extensionTextView = itemView.findViewById(R.id.text_view_extension);

            // Find buttons
            MaterialButton btnShift = itemView.findViewById(R.id.btnShift);
            MaterialButton btnDone = itemView.findViewById(R.id.btnDone);

            // Done button click listener
            btnDone.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(tasks.get(position));
                }
            });

            // Shift button click listener
            btnShift.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onShiftClick(tasks.get(position));
                }
            });

            MaterialButton btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(tasks.get(position));
                }
            });

        }}}