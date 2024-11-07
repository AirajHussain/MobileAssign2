package com.example.mobileassign2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobileassign2.DBHandler;


public class EditLocation extends AppCompatActivity {

    private DBHandler dbHandler;
    private EditText locationAddress, locationLongitude, locationLatitude;
    private Button saveLocationButton, deleteLocationButton, cancelLocation,backButton;

    private View locationContainer;
    private int locationId;

    private View lastSelectedView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_location);

        locationAddress= findViewById(R.id.locationAddress);
        locationLongitude = findViewById(R.id.locationLongitude);
        locationLatitude = findViewById(R.id.locationLatitude);




        dbHandler = new DBHandler(this);

        locationId   = getIntent().getIntExtra("noteId", -1);
        if (locationId != -1) {
            loadLocation();
        }


        saveLocationButton.setOnClickListener(view -> saveNote());
        deleteLocationButton.setOnClickListener(view -> deleteNoteAndFinish());
        cancelLocation.setOnClickListener(view -> finish());
        backButton.setOnClickListener(view -> finish());



    }

    private void loadLocation() {
        Location location= dbHandler.getLocationById(locationId);
        if (location != null) {
            locationAddress.setText(location.getAddress());
            locationLongitude.setText(String.valueOf(location.getLongitude()));
            locationLatitude.setText(String.valueOf(location.getLatitude()));




        } else {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveNote() {

        String address = locationAddress.getText().toString();
        double longitude = Double.parseDouble(locationLongitude.getText().toString());
        double latitude = Double.parseDouble(locationLatitude.getText().toString());





        Location editedLocation = new Location(locationId, address,longitude,latitude);
        dbHandler.updateLocation(editedLocation);
        Toast.makeText(this, "Location Edited", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("locationId", locationId);
        setResult(RESULT_OK, resultIntent);
        finish();



    }

    private void deleteNoteAndFinish() {
        dbHandler.deleteLocation(locationId);
        Toast.makeText(this, "Location Deleted", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("locationId", locationId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
