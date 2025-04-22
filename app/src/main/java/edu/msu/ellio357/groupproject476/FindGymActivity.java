package edu.msu.ellio357.groupproject476;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Locale;

public class FindGymActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private double latitude = 0;
    private double longitude = 0;
    private boolean hasLocation = false;

    private TextView textLocationInfo;
    private EditText editTextLocationInput;
    private Button buttonShowRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_gym);

        textLocationInfo = findViewById(R.id.textDistanceToGym);
        editTextLocationInput = findViewById(R.id.editTextLocationInput);
        buttonShowRoute = findViewById(R.id.buttonShowRoute);

        RadioGroup gymRadioGroup = findViewById(R.id.gymRadioGroup);
        EditText editTextLocationInput = findViewById(R.id.editTextLocationInput);

        gymRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioPlanetFitness) {
                editTextLocationInput.setText(R.string.planet_fitness);
            } else if (checkedId == R.id.radioLifetimeFitness) {
                editTextLocationInput.setText(R.string.lifetime_fitness);
            } else if (checkedId == R.id.radioPowerhouse) {
                editTextLocationInput.setText(R.string.powerhouse_gym);
            }
        });


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        requestUserLocation();

        buttonShowRoute.setOnClickListener(v -> {
            String destination = editTextLocationInput.getText().toString().trim();

            if (!hasLocation) {
                Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
            } else if (destination.isEmpty()) {
                Toast.makeText(this, "Please enter a destination", Toast.LENGTH_SHORT).show();
            } else {
                String uri = String.format(Locale.US,
                        "https://www.google.com/maps/dir/?api=1&origin=%f,%f&destination=%s&travelmode=driving",
                        latitude, longitude, Uri.encode(destination));

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "Open route with..."));
            }
        });
    }

    private void requestUserLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (bestProvider != null) {
            locationManager.requestSingleUpdate(bestProvider, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    hasLocation = true;
                    textLocationInfo.setText(String.format(Locale.US,
                            "Current Location:\nLatitude: %.5f, Longitude: %.5f", latitude, longitude));
                }

                @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override public void onProviderEnabled(String provider) {}
                @Override public void onProviderDisabled(String provider) {}
            }, null);
        }
    }
}
