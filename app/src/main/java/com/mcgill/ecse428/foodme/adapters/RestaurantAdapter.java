package com.mcgill.ecse428.foodme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.model.Restaurant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


//Recycler view adapter for restaurant list

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private List<Restaurant> restaurantList;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        protected TextView name, cuisine, price, distance;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.findRestaurantName);
            cuisine = view.findViewById(R.id.findRestaurantCuisine);
            distance = view.findViewById(R.id.findRestaurantDistance);
            price = view.findViewById(R.id.findRestaurantPrice);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener){
            this.clickListener = itemClickListener;
        }

        public void onClick(View view){
            clickListener.onClick(view, getLayoutPosition(),false);
        }
        public boolean onLongClick(View view){
            clickListener.onClick(view, getLayoutPosition(),true);
            return true;
        }
    }


    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_restaurant, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.name.setText(restaurant.getName());
        holder.price.setText(restaurant.getPrice());
        holder.cuisine.setText(restaurant.getCuisine());
        holder.distance.setText(restaurant.getDistance());

        holder.setClickListener(new ItemClickListener(){
           @Override
           public void onClick(View view, int position, boolean isLongClick){
               List<Restaurant> rList = restaurantList;
               if(isLongClick){
                   Toast.makeText(mContext, "#" + position +" - "+rList.get(position).getName()
                        +" (Long click)", Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(mContext,"#"+position+" - "+rList.get(position).getName(),Toast.LENGTH_SHORT).show();
               }
           }
        });

    }


    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public interface ItemClickListener{
        void onClick (View view, int position, boolean isLongClick);
    }

}