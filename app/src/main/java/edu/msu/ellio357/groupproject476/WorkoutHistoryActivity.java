package edu.msu.ellio357.groupproject476;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {

    private ListView listViewWorkouts;
    private List<String> workoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        listViewWorkouts = findViewById(R.id.listViewWorkouts);

        // Fake data (Replace with actual workout log later)
        workoutList = new ArrayList<>();
        workoutList.add("Leg Day - March 15, 2025");
        workoutList.add("Push Workout - March 14, 2025");
        workoutList.add("Pull Workout - March 13, 2025");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, workoutList);
        listViewWorkouts.setAdapter(adapter);


        Button logButton = findViewById(R.id.log_button);
        Button logoutButton = findViewById(R.id.logout_button);
        Button libraryButton = findViewById(R.id.library_button);

        /**
         * Menu buttons
         */
        libraryButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutLibraryActivity.class));
        });
        logButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutEntryActivity.class));
        });
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
        });
    }
}
