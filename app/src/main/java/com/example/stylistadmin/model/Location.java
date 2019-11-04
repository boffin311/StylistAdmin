package com.example.stylistadmin.model;

public class Location {
    String address;
    String city;
    Double lat;
    Double lng;

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Location(String address, String city, Double lat, Double lng) {
        this.address = address;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
    }
}
