package com.example.mobileassign2;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;

public class NewLocation extends AppCompatActivity {


    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.new_location);

        dbHandler = new DBHandler(this);

        // Apply insets to accommodate system UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button saveLocationButton = findViewById(R.id.saveLocationButton);
        saveLocationButton.setOnClickListener(v -> saveLocation());

        Button cancelLocationButton = findViewById(R.id.cancelLocationButton);
        cancelLocationButton.setOnClickListener(v -> finish());
    }

    private void saveLocation() {
        EditText locationAddress = findViewById(R.id.locationAddress);
        EditText locationLongitude = findViewById(R.id.locationLongitude);
        EditText locationLatitude = findViewById(R.id.locationLatitude);

        String address = locationAddress.getText().toString();
        double longitude = 0.0;
        double latitude = 0.0;


        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter an address.", Toast.LENGTH_SHORT).show();
            return;
        }




        long result = dbHandler.addLocation(address,longitude,latitude);

        if (result != -1) {
            Toast.makeText(this, "Location Saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving location. . .", Toast.LENGTH_SHORT).show();
        }
    }
}
