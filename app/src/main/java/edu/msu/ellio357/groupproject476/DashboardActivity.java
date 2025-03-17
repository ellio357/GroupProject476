package edu.msu.ellio357.groupproject476;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        TextView welcomeTextView = findViewById(R.id.textViewWelcome);
        Button logoutButton = findViewById(R.id.buttonLogout);

        Button logWorkout = findViewById(R.id.log_button);

        if (user != null) {
            welcomeTextView.setText("Welcome, " + user.getEmail());
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        logWorkout.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutEntryActivity.class));

        });
    }
}
