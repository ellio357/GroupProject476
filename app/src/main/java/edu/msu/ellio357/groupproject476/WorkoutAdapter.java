package edu.msu.ellio357.groupproject476;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    public interface OnWorkoutClickListener {
        void onWorkoutClick(WorkoutDatabase.Workout workout);
        void onDeleteClick(WorkoutDatabase.Workout workout);
    }

    private List<WorkoutDatabase.Workout> workouts;
    private OnWorkoutClickListener listener;

    public WorkoutAdapter(List<WorkoutDatabase.Workout> workouts, OnWorkoutClickListener listener) {
        this.workouts = workouts;
        this.listener = listener;
    }

    public void updateWorkoutList(List<WorkoutDatabase.Workout> newList) {
        workouts = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutDatabase.Workout workout = workouts.get(position);
        holder.bind(workout, listener);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView workoutName, workoutInfo;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workoutName);
            workoutInfo = itemView.findViewById(R.id.workoutInfo);
        }

        public void bind(WorkoutDatabase.Workout workout, OnWorkoutClickListener listener) {
            workoutName.setText(workout.getName());
            workoutInfo.setText("Length: " + workout.getLength() + " min | Calories: " + workout.getCalories());

            itemView.setOnClickListener(v -> listener.onWorkoutClick(workout));

            Button deleteButton = itemView.findViewById(R.id.deleteWorkoutBtn);
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(workout));
        }
    }
}
