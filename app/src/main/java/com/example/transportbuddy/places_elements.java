package com.example.transportbuddy;

import com.google.firebase.firestore.GeoPoint;

public class places_elements {
    private String place_name;
    private String place_image;
    private String place_desc;
    private String place_location;
    private GeoPoint place_points;

    public places_elements() {
    }

    public places_elements(String place_name, String place_image, String place_desc, String place_location,GeoPoint place_points) {
        this.place_name = place_name;
        this.place_image = place_image;
        this.place_desc = place_desc;
        this.place_location = place_location;
        this.place_points = place_points;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getPlace_image() {
        return place_image;
    }

    public String getPlace_desc() {
        return place_desc;
    }

    public String getPlace_location() {
        return place_location;
    }

    public GeoPoint getPlace_points(){return place_points;}

    public double getPlace_longitude(){
        if (place_points != null)
            return place_points.getLongitude();
        else
            return 0.0;
    }
    public double getPlace_latitude(){
        if (place_points != null)
            return place_points.getLatitude();
        else
            return 0.0;
    }
}