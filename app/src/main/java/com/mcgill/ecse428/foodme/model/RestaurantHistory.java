
package com.mcgill.ecse428.foodme.model;



/**
 * Constructor for the restaurant history recycler view
 */
public class RestaurantHistory  {

    private String name, date, rating, cuisine;
    //Should be length 2
    //["address", "city state zip"]
    //i.e: ["123 St-Catherine", "Montreal, Qc X1X1X1"]
    //kept separate for formatting
    private String[] address;

    public RestaurantHistory(String name, String cuisine, String date, String rating ) {
        this.name = name;
        this.date = date;
        this.cuisine = cuisine;
        this.rating = rating;
    }
    public RestaurantHistory(String name, String cuisine, String date, String rating, String[] address){
        this.name = name;
        this.date = date;
        this.cuisine = cuisine;
        this.rating = rating;
        this.address = address;
    }
    public void setAddress(String[] address){ this.address = address;}

    public String[] getAddress(){ return this.address;}

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


