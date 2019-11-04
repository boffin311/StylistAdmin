package com.example.stylistadmin.model;

public class SubCatInfo {
    String describe;
    String name;
    int price;
    int duration;

    public String getDescribe() {
        return describe;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public SubCatInfo(String describe, String name, int price, int duration) {
        this.describe = describe;
        this.name = name;
        this.price = price;
        this.duration = duration;
    }
}
