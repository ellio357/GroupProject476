package edu.msu.ellio357.groupproject476;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WorkoutEntryActivity extends AppCompatActivity
{
    private EditText mWorkoutName, mWorkoutCals, mWorkoutLength;

    private WorkoutDatabase workoutDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_entry);

        mWorkoutName = findViewById(R.id.addWorkoutName);
        mWorkoutCals = findViewById(R.id.addWorkoutCals);
        mWorkoutLength = findViewById(R.id.addWorkoutLength);
        workoutDatabase = new WorkoutDatabase();


        String lengthText = mWorkoutLength.getText().toString().trim();
        if (!lengthText.isEmpty()) {
            try {
                int workoutLengthMinutes = Integer.parseInt(lengthText);
                // Use workoutLengthMinutes as needed
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number for workout length", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Workout length is required", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Workout Entry Screen Loaded", Toast.LENGTH_SHORT).show();

        Button recordButton = findViewById(R.id.recordButton);
        Button historyButton = findViewById(R.id.history_button);
        Button logoutButton = findViewById(R.id.logout_button);
        Button libraryButton = findViewById(R.id.library_button);

        recordButton.setOnClickListener(v -> recordWorkout());
        loadWorkouts();

        /**
         * Menu buttons
         */
        libraryButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutLibraryActivity.class));
        });
        historyButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutHistoryActivity.class));
        });
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
        });
    }

    protected void recordWorkout()
    {
        boolean success = false;
        String name = mWorkoutName.getText().toString().trim();
        String cals = mWorkoutCals.getText().toString().trim();
        Integer calories = Integer.parseInt(cals);
        String lengthText = mWorkoutLength.getText().toString().trim();

        if (name.isEmpty() || cals.isEmpty() || lengthText.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int len;
        try {
            len = Integer.parseInt(lengthText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Workout length must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        workoutDatabase.addWorkout(name, len, calories)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Workout added!", Toast.LENGTH_SHORT).show();
                    loadWorkouts(); // Refresh the list

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        success = true;
        if (success) {
            Toast.makeText(this, "Workout recorded!", Toast.LENGTH_SHORT).show();
            // Optionally clear inputs
            mWorkoutName.setText("");
            mWorkoutCals.setText("");
            mWorkoutLength.setText("");
        } else {
            Toast.makeText(this, "Error saving workout", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadWorkouts() {
        workoutDatabase.getWorkoutsForCurrentUser()
                .addOnSuccessListener(querySnapshot -> {
                    // Process and update UI
                });
    }
}
