package com.mcgill.ecse428.foodme.model;

public class Preference {
    private int pID;
    private String location;
    private String cuisine;
    private String price;
    private String sortBy;

    public Preference(int pID, String location, String cuisine, String price, String sortBy) {
        this.pID = pID;
        this.location = location;
        this.cuisine = cuisine;
        this.price = price;
        this.sortBy = sortBy;
    }

    public int getpID(){ return pID; }

    public void setPid(int pID) { this.pID = pID; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
