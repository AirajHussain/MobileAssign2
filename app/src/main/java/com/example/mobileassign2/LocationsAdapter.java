package com.example.mobileassign2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

    public TextView locationLongitude,locationLatitude;
    private List<Location> locationList;
    private List<Location> filteredLocation;
    private Context context;

    DBHandler dbHandler;

    public LocationsAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;
        this.filteredLocation = new ArrayList<>(locationList);
        this.dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(view);
    }

    public interface OnLocationDeletedListener {
        void onLocationDeleted(int locationId);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {

        Location location = filteredLocation.get(position);

        holder.address.setText(location.getAddress());

        holder.longitude.setText(String.valueOf(location.getLongitude()));
        holder.latitude.setText(String.valueOf(location.getLatitude()));




        holder.itemView.setBackgroundColor(Color.WHITE);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditLocation.class);
            intent.putExtra("locationId", location.getId());
            context.startActivity(intent);
        });

        holder.deleteLocationButton.setOnClickListener(v -> {
            int locationId = location.getId();

            new AlertDialog.Builder(context)
                    .setTitle("Delete Location")
                    .setMessage("Are you sure you want to delete this location?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        dbHandler.deleteLocation(locationId);
                        filteredLocation.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Location deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return filteredLocation.size();
    }

    public void filter(String query) {
        query = query.toLowerCase();
        filteredLocation.clear();

        if (query.isEmpty()) {
            filteredLocation.addAll(locationList);
        } else {
            for (Location location : locationList) {
                if (location.getAddress().toLowerCase().contains(query)) {
                    filteredLocation.add(location);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        public TextView address,longitude,latitude;
        public ImageView deleteLocationButton;


        public LocationViewHolder(@NonNull View itemView) {

            super(itemView);
            address = itemView.findViewById(R.id.locationAddress);
            longitude = itemView.findViewById(R.id.locationLongitude);
            latitude = itemView.findViewById(R.id.locationLatitude);
            deleteLocationButton = itemView.findViewById(R.id.deleteLocationButton);


        }
    }
}
