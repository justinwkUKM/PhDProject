package com.example.myxlab.gdktry.Google;

import com.google.android.glass.app.Card;

import java.io.Serializable;

/**
 * Created by MyXLab on 17/1/2018.
 */

public class POI implements Serializable{

    private int id;

    private String place_name;
    private String place_id;
    private String place_vicinity;
    private double place_rating, lat, lng;
    private boolean place_open;

    private Card.ImageLayout imgLayout;
    private int[] images;

    public POI() {
    }

    public POI(String place_name, String place_vicinity, Card.ImageLayout imgLayout, int[] images) {
        this.place_name = place_name;
        this.place_vicinity = place_vicinity;
        this.imgLayout = imgLayout;
        this.images = images;
    }

    public POI( String place_name, String place_id, String place_vicinity, double place_rating, double lat, double lng, boolean place_open, Card.ImageLayout imgLayout, int[] images) {
        this.place_name = place_name;
        this.place_id = place_id;
        this.place_vicinity = place_vicinity;
        this.place_rating = place_rating;
        this.lat = lat;
        this.lng = lng;
        this.place_open = place_open;
        this.imgLayout = imgLayout;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_vicinity() {
        return place_vicinity;
    }

    public void setPlace_vicinity(String place_vicinity) {
        this.place_vicinity = place_vicinity;
    }

    public double getPlace_rating() {
        return place_rating;
    }

    public void setPlace_rating(double place_rating) {
        this.place_rating = place_rating;
    }

    public boolean isPlace_open() {
        return place_open;
    }

    public void setPlace_open(boolean place_open) {
        this.place_open = place_open;
    }

    public Card.ImageLayout getImgLayout() {
        return imgLayout;
    }

    public void setImgLayout(Card.ImageLayout imgLayout) {
        this.imgLayout = imgLayout;
    }

    public int[] getImages() {
        return images;
    }

    public void setImages(int[] images) {
        this.images = images;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
