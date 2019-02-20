
package com.mcgill.ecse428.foodme.model;


/**
 * Constructor for the restaurant recycler view
 */
public class Restaurant {

    private String name, distance, price, cuisine;


    public Restaurant(String name, String cuisine, String price, String distance) {
        this.name = name;
        this.distance = distance;
        this.cuisine = cuisine;
        this.price = price;
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


}


