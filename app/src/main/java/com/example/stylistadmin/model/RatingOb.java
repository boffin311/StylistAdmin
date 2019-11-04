package com.example.stylistadmin.model;

public class RatingOb {
    int stars;
    int users;

    public void setUsers(int users) {
        this.users = users;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }

    public int getUsers() {
        return users;
    }

    public RatingOb(int stars, int users) {
        this.stars = stars;
        this.users = users;
    }
}
