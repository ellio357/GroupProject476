package edu.msu.ellio357.groupproject476;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WorkoutLibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_library);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button historyButton = findViewById(R.id.history_button);
        Button logoutButton = findViewById(R.id.logout_button);
        Button logButton = findViewById(R.id.log_button);


        /**
         * Menu buttons
         */
        logButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutEntryActivity.class));
        });
        historyButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutHistoryActivity.class));
        });
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
        });
    }
}