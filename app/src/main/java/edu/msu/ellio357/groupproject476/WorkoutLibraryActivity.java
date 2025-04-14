package edu.msu.ellio357.groupproject476;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

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

        WebView workoutVideo = findViewById(R.id.workoutVideoView);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String message;
        String videoUrl;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                message = getString(R.string.mon);
                videoUrl = getString(R.string.video_mon);
                break;
            case Calendar.TUESDAY:
                message = getString(R.string.tues);
                videoUrl = getString(R.string.video_tues);
                break;
            case Calendar.WEDNESDAY:
                message = getString(R.string.wed);
                videoUrl = getString(R.string.video_wed);
                break;
            case Calendar.THURSDAY:
                message = getString(R.string.thurs);
                videoUrl = getString(R.string.video_thurs);
                break;
            case Calendar.FRIDAY:
                message = getString(R.string.fri);
                videoUrl = getString(R.string.video_fri);
                break;
            case Calendar.SATURDAY:
                message = getString(R.string.sat);
                videoUrl = getString(R.string.video_sat);
                break;
            case Calendar.SUNDAY:
                message = getString(R.string.sun);
                videoUrl = getString(R.string.video_sun);
                break;
            default:
                message = getString(R.string.defaultRec);
                videoUrl = getString(R.string.video_default);
        }

        TextView workoutText = findViewById(R.id.workoutText);
        workoutText.setText(message);

        workoutVideo.getSettings().setJavaScriptEnabled(true);
        workoutVideo.getSettings().setDomStorageEnabled(true); // Add this
        workoutVideo.getSettings().setLoadWithOverviewMode(true);
        workoutVideo.getSettings().setUseWideViewPort(true);
        workoutVideo.setWebChromeClient(new WebChromeClient()); // Handle full-screen, etc.

        String html = "<html><body style=\"margin:0;\"><iframe width=\"100%\" height=\"100%\" src=\"" +
                videoUrl + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        workoutVideo.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);


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