package com.mcgill.ecse428.foodme.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mcgill.ecse428.foodme.fragment.IndividualRestaurantFragment;
import com.mcgill.ecse428.foodme.model.Restaurant;
import com.mcgill.ecse428.foodme.model.RestaurantHistory;
import com.mcgill.ecse428.foodme.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


//Recycler view adapter for restaurant history

public class RestaurantHistoryAdapter extends RecyclerView.Adapter<RestaurantHistoryAdapter.MyViewHolder> {

    private List<RestaurantHistory> historyList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        private RestaurantAdapter.ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.historyRestaurantName);

        }


    }

    public RestaurantHistoryAdapter(List<RestaurantHistory> historyList) {
        this.historyList = historyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_restaurant_history, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RestaurantHistory history = historyList.get(position);
        holder.name.setText(history.getName());


    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }


}