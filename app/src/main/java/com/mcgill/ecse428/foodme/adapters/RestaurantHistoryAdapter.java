package com.mcgill.ecse428.foodme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcgill.ecse428.foodme.model.RestaurantHistory;
import com.mcgill.ecse428.foodme.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


//Recycler view adapter for restaurant history

public class RestaurantHistoryAdapter extends RecyclerView.Adapter<RestaurantHistoryAdapter.MyViewHolder> {

    private List<RestaurantHistory> historyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView name, cuisine, date, rating;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.historyRestaurantDate);
            cuisine = view.findViewById(R.id.historyRestaurantCuisine);
            date = view.findViewById(R.id.historyRestaurantDate);
            rating = view.findViewById(R.id.historyRestaurantRating);


        }
    }


    public RestaurantHistoryAdapter(List<RestaurantHistory> historyList) {
        this.historyList = historyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_restaurant_history, parent, false);
        itemView.setClickable(false);
        itemView.setFocusable(false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RestaurantHistory history = historyList.get(position);
        holder.name.setText(history.getName());
        holder.date.setText(history.getDate());
        holder.cuisine.setText(history.getCuisine());
        holder.rating.setText(history.getRating());


    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }


}