
package com.mcgill.ecse428.foodme.model;



/**
 * Constructor for the restaurant history recycler view
 */
public class RestaurantHistory  {

    private String name, date, rating, cuisine;


    public RestaurantHistory(String name, String cuisine, String date, String rating ) {
        this.name = name;
        this.date = date;
        this.cuisine = cuisine;
        this.rating = rating;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }













}


