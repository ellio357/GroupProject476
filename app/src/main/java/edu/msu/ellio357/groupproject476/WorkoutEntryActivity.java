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
    private EditText mWorkoutName, mWorkoutType, mWorkoutLength;
//    Test database until we get it working
//    private SQLDatabaseClass mTestdatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_entry);

        mWorkoutName = findViewById(R.id.addWorkoutName);
        mWorkoutType = findViewById(R.id.addWorkoutCals);
        mWorkoutLength = findViewById(R.id.addWorkoutLength);

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

        Button recordButton = findViewById(R.id.recordButton);

        recordButton.setOnClickListener(v -> recordWorkout());

    }

    protected void recordWorkout()
    {
        String name = mWorkoutName.getText().toString().trim();
        String type = mWorkoutType.getText().toString().trim();
        String lengthText = mWorkoutLength.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || lengthText.isEmpty()) {
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
//        Try adding to database
//        boolean success = Testdatabase.insertWorkout(name, type, len);
        boolean success = true;
        if (success) {
            Toast.makeText(this, "Workout recorded!", Toast.LENGTH_SHORT).show();
            // Optionally clear inputs
            mWorkoutName.setText("");
            mWorkoutType.setText("");
            mWorkoutLength.setText("");
        } else {
            Toast.makeText(this, "Error saving workout", Toast.LENGTH_SHORT).show();
        }

    }
}
