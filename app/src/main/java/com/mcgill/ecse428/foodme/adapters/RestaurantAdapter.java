package com.mcgill.ecse428.foodme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.model.Restaurant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


//Recycler view adapter for restaurant list

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private List<Restaurant> restaurantList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView name, cuisine, price, distance;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.findRestaurantName);
            cuisine = view.findViewById(R.id.findRestaurantCuisine);
            distance = view.findViewById(R.id.findRestaurantDistance);
            price = view.findViewById(R.id.findRestaurantPrice);


        }
    }


    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_restaurant, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.name.setText(restaurant.getName());
        holder.price.setText(restaurant.getPrice());
        holder.cuisine.setText(restaurant.getCuisine());
        holder.distance.setText(restaurant.getDistance());


    }


    @Override
    public int getItemCount() {
        return restaurantList.size();
    }


}