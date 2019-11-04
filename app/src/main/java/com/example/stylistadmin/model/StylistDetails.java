package com.example.stylistadmin.model;

public class StylistDetails {
    int experience;
    String name;
    String tagline;
    String description;
    String sex;
    String profile;


    public String getProfile() {
        return profile;
    }

    public StylistDetails(int experience, String name, String tagline, String description, String sex, int age, String email, int followers, int appointments, String dateJoined, String profile) {
        this.experience = experience;
        this.name = name;
        this.tagline = tagline;
        this.description = description;
        this.sex = sex;
        this.profile=profile;
//        this.discount = discount;
//        this.zaloninDiscount = zaloninDiscount;
        this.age = age;
        this.email = email;
        this.followers = followers;
        this.appointments = appointments;
        this.dateJoined = dateJoined;
    }



    int age ;
    String email;
    int followers;
    int appointments;
    String dateJoined;

    public int getExperience() {
        return experience;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getDescription() {
        return description;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public int getFollowers() {
        return followers;
    }

    public int getAppointments() {
        return appointments;
    }

    public String getDateJoined() {
        return dateJoined;
    }

}
