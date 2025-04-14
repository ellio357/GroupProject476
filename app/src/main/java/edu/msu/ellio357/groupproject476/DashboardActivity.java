package edu.msu.ellio357.groupproject476;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.location.Criteria;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private LocationManager locationManager = null;
    private double latitude = 0;
    private double longitude = 0;
    private double toLatitude = 0;
    private double toLongitude = 0;
    private String to = "";
    private SharedPreferences settings = null;
    private final static String TO = "to";
    private final static String TOLAT = "tolat";
    private final static String TOLONG = "tolong";

    private final ActiveListener activeListener = new ActiveListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        TextView welcomeTextView = findViewById(R.id.textViewWelcome);

        TextView recommendationTextView = findViewById(R.id.textViewRecommendation);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String message;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                message = "Start your week strong! Try a full-body workout today.";
                break;
            case Calendar.TUESDAY:
                message = "How about a cardio session today?";
                break;
            case Calendar.WEDNESDAY:
                message = "Today we do push!";
                break;
            case Calendar.THURSDAY:
                message = "Perfect day for strength training!";
                break;
            case Calendar.FRIDAY:
                message = "Almost the weekend, lets do legs!";
                break;
            case Calendar.SATURDAY:
                message = "Try a new outdoor activity today!";
                break;
            case Calendar.SUNDAY:
                message = "Rest and recover. You deserve it!";
                break;
            default:
                message = "Keep moving forward!";
        }

        recommendationTextView.setText(message);

        Button logoutButton = findViewById(R.id.buttonLogout);
        Button workoutHistoryButton = findViewById(R.id.history_button);
        Button libaryButton = findViewById(R.id.library_button);

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
        workoutHistoryButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutHistoryActivity.class));
        });
        libaryButton.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutLibraryActivity.class));
        });

        logWorkout.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutEntryActivity.class));

        });

        settings = getSharedPreferences("edu.msu.ellio357.groupproject476_preferences", Context.MODE_PRIVATE);
        to = settings.getString(TO, "2250 Engineering");
        toLatitude = Double.parseDouble(settings.getString(TOLAT, "42.724303"));
        toLongitude = Double.parseDouble(settings.getString(TOLONG, "-84.480507"));

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        // Get the location manager
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        // Force the screen to say on and bright
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        findViewById(R.id.find_gym_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapWithRoute();
            }
        });
    }


    private void openMapWithRoute() {

        String uri = String.format(Locale.US,
                "https://www.google.com/maps/search/?api=1&query=gym&query_place_id=&center=%f,%f",
                latitude, longitude);

        Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri));
        startActivity(Intent.createChooser(intent, "Find gyms near you with..."));
    }




    @Override
    protected void onResume() {
        super.onResume();

        registerListeners();
    }

    /**
     * Called when this application is no longer the foreground application.
     */
    @Override
    protected void onPause() {
        unregisterListeners();
        super.onPause();
    }


    private void registerListeners() {
        unregisterListeners();
        // Create a Criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        String bestAvailable = locationManager.getBestProvider(criteria, true);
        if(bestAvailable != null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(bestAvailable, 500, 1,
                    activeListener);
            Location location =
                    locationManager.getLastKnownLocation(bestAvailable);
            onLocation(location);
        }
    }
    private void unregisterListeners() {
        locationManager.removeUpdates(activeListener);
    }

    private void onLocation(Location location) {
        if(location == null) {
            return;
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }




    private class ActiveListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            onLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String s) {
            registerListeners();
        }
        @Override
        public void onProviderDisabled(String s) {
            registerListeners();

        }
    };


}
