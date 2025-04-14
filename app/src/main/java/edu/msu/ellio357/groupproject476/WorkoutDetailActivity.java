package edu.msu.ellio357.groupproject476;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WorkoutDetailActivity extends AppCompatActivity {

    private TextView detailName, detailLength, detailCals, detailDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        detailName = findViewById(R.id.detailName);
        detailLength = findViewById(R.id.detailLength);
        detailCals = findViewById(R.id.detailCalories);
        detailDate = findViewById(R.id.detailDate);

        Intent intent = getIntent();
        detailName.setText(intent.getStringExtra("workoutName"));
        detailLength.setText("Length: " + intent.getIntExtra("workoutLength", 0) + " mins");
        detailCals.setText("Calories: " + intent.getIntExtra("workoutCalories", 0));
        detailDate.setText("Created: " + intent.getStringExtra("workoutCreated"));

        Button backButton = findViewById(R.id.backButton);

        /**
         * Menu buttons
         */
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutHistoryActivity.class));
        });
    }
}
