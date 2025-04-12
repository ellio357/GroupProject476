package edu.msu.ellio357.groupproject476;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private WorkoutDatabase workoutDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        workoutDatabase = new WorkoutDatabase();
        recyclerView = findViewById(R.id.recyclerViewWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button logButton = findViewById(R.id.log_button);
        Button logoutButton = findViewById(R.id.logout_button);
        Button libraryButton = findViewById(R.id.library_button);

        logButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutEntryActivity.class));
        });
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
        });
        libraryButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutLibraryActivity.class));
        });

        adapter = new WorkoutAdapter(new ArrayList<>(), new WorkoutAdapter.OnWorkoutClickListener() {
            @Override
            public void onWorkoutClick(WorkoutDatabase.Workout workout) {
                Intent intent = new Intent(WorkoutHistoryActivity.this, WorkoutDetailActivity.class);
                intent.putExtra("workoutName", workout.getName());
                intent.putExtra("workoutLength", workout.getLength());
                intent.putExtra("workoutCalories", workout.getCalories());
                intent.putExtra("workoutCreated", workout.getCreatedAt().toDate().toString());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(WorkoutDatabase.Workout workout) {
                deleteWorkoutFromFirestore(workout);
            }
        });


        recyclerView.setAdapter(adapter);
        loadWorkouts();
    }
    private void deleteWorkoutFromFirestore(WorkoutDatabase.Workout workout) {
        FirebaseFirestore.getInstance()
                .collection("workouts")
                .whereEqualTo("userId", workout.getUserId())
                .whereEqualTo("name", workout.getName())
                .whereEqualTo("length", workout.getLength())
                .whereEqualTo("calories", workout.getCalories())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        doc.getReference().delete();
                    }
                    Toast.makeText(this, "Workout deleted", Toast.LENGTH_SHORT).show();
                    loadWorkouts(); // Refresh list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error deleting workout: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadWorkouts() {
        workoutDatabase.getWorkoutsForCurrentUser()
                .addOnSuccessListener(querySnapshot -> {
                    List<WorkoutDatabase.Workout> workouts = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        WorkoutDatabase.Workout workout = doc.toObject(WorkoutDatabase.Workout.class);
                        if (workout != null) {
                            workouts.add(workout);
                        }
                    }
                    adapter.updateWorkoutList(workouts);
                });
    }

}
