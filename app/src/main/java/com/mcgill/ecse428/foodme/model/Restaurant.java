
package com.mcgill.ecse428.foodme.model;


/**
 * Constructor for the restaurant recycler view
 */
public class Restaurant {

    private String name, distance, price, cuisine;
    //Should be length 2
    //["address", "city state zip"]
    //i.e: ["123 St-Catherine", "Montreal, Qc X1X1X1"]
    //kept separate for formatting
    private String[] address;
    private String id;


    public Restaurant(String name, String cuisine, String price, String distance) {
        this.name = name;
        this.distance = distance;
        this.cuisine = cuisine;
        this.price = price;
    }
    public Restaurant(String name, String cuisine, String price, String distance, String[] address){
        this.name = name;
        this.distance = distance;
        this.cuisine = cuisine;
        this.price = price;
        this.address = address;
    }
    public Restaurant(String name, String cuisine, String price, String distance, String[] address, String id){
        this.name = name;
        this.distance = distance;
        this.cuisine = cuisine;
        this.price = price;
        this.address = address;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String rating) {
        this.price = price;
    }

    public void setAddress(String[] address){ this.address = address;}

    public String[] getAddress(){ return this.address;}

    public String getId(){ return this.id; }
}


