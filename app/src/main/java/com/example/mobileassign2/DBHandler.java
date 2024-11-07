package com.example.mobileassign2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper{

    private static final String DB_NAME = "location_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_LOCATIONS = "locations";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ADD= "address";
    private static final String COLUMN_LAT = "latitude";
    private static final String COLUMN_LON = "longitude";

    public DBHandler (Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    //Creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ADD + " TEXT, " +
                COLUMN_LAT + " REAL, " +  // Latitude as REAL
                COLUMN_LON + " REAL)";    // Longitude as REAL
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(db);
    }


    // Method to add a new location to the database
    public long addLocation(String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ADD, address);
        values.put(COLUMN_LAT, latitude);
        values.put(COLUMN_LON, longitude);

        // Insert the new row into the locations table
        long result = db.insert(TABLE_LOCATIONS, null, values);

        db.close();
        return result;
    }

    public Cursor getLocation() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_LOCATIONS + " ORDER BY " + COLUMN_ID + " DESC";
        return db.rawQuery(query, null);
    }

    public void updateLocation(Location location) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ADD, location.getAddress());
        values.put(COLUMN_LON, location.getLongitude());
        values.put(COLUMN_LAT, location.getLatitude());


        Log.d("DatabaseHelper", "Updating note: " + location.getId());

        db.update(TABLE_LOCATIONS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(location.getId())});
        db.close();
    }

    public void deleteLocation(int locationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(locationId)});
        db.close();
    }



    public Location getLocationById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOCATIONS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADD));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LON));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LAT));


            cursor.close();

            return new Location(id, address,longitude,latitude);
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }













}
