package com.example.mobileassign2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int EDIT_LOCATION_REQEUST_CODE = 2;
    private LocationsAdapter locationsAdapter;
    private int locationId;

    DBHandler dbHandler;
    RecyclerView locationRecyclerView;
    List<Location> locationList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHandler = new DBHandler(this);

        locationRecyclerView = findViewById(R.id.locationRecyclerView);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadLocations();

        SearchView searchView = findViewById(R.id.searching );
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                locationsAdapter.filter(newText);
                return true;
            }
        });

        Button newLocationButton = findViewById(R.id.newLocationButton);
        newLocationButton.setOnClickListener(v -> openNewLocation());





    }



    private void loadLocations() {
        locationList = new ArrayList<>();
        Cursor cursor = dbHandler.getLocation();
        TextView noLocationMessage = findViewById(R.id.noLocation);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));


                locationList.add(new Location(id, address, longitude,latitude)); // Convert Bitmap to byte[] when adding
            } while (cursor.moveToNext());
            noLocationMessage.setVisibility(View.GONE);
        } else {
            noLocationMessage.setVisibility(View.VISIBLE);
        }

        if (cursor != null) {
            cursor.close();
        }


        locationsAdapter = new LocationsAdapter(this, locationList);
        locationRecyclerView.setAdapter(locationsAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_LOCATION_REQEUST_CODE && resultCode == RESULT_OK && data != null) {
            int noteId = data.getIntExtra("noteId", -1);
            if (noteId != -1) {

                String address = data.getStringExtra("address");
                double latitude = getIntent().getDoubleExtra("latitude", 0.0); // Default value is 0.0 if not found
                double longitude = getIntent().getDoubleExtra("longitude", 0.0);
                // Retrieve bitmap directly

                // Convert Bitmap to byte array

                for (Location location : locationList) {
                    if (location.getId() == locationId) {
                        location.setAddress(address);
                        location.setLatitude(latitude);
                        location.setLongitude(longitude);
                        break;
                    }
                }
                locationsAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
            } else {
                locationList.removeIf(note -> note.getId() == noteId);
                locationsAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void onLocationAdded(){
        loadLocations();
    }

    private void openNewLocation(){
        Intent intent = new Intent(MainActivity.this, NewLocation.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocations();
    }






}