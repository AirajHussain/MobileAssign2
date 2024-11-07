package com.example.mobileassign2;

public class Location {
    private int id;
    private String address;
    private double longitude;
    private double latitude;

    public Location(int id, String address, double longitude,double latitutde){

        this.id=id;
        this.address=address;
        this.longitude=longitude;
        this.latitude=latitutde;

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude=latitude;
    }



}



