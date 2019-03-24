package com.mcgill.ecse428.foodme.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.fragment.IndividualRestaurantFragment;
import com.mcgill.ecse428.foodme.model.Restaurant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


//Recycler view adapter for restaurant list

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private List<Restaurant> restaurantList;
    //holds our parent's context
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

            //add the click listeners
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

        //Create the click listeners
        holder.setClickListener(new ItemClickListener(){
            @Override
            public void onClick(View view, int position, boolean isLongClick){
                List<Restaurant> rList = restaurantList;
                if(isLongClick){
                    //for now, no additional functionality here
                    onClick(view, position, false);
                }
                else{
                    IndividualRestaurantFragment irf = new IndividualRestaurantFragment();

                    //prepare arguments
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", rList.get(position).getName());
                    bundle.putString("PRICE", rList.get(position).getPrice());
                    bundle.putString("CUISINE",rList.get(position).getCuisine());
                    bundle.putString("DISTANCE",rList.get(position).getDistance());
                    String[] address = rList.get(position).getAddress();
                    bundle.putString("ADDRESS1",address[0]);
                    bundle.putString("ADDRESS2",address[1]);
                    bundle.putString("id", rList.get(position).getId());
                    irf.setArguments(bundle);

                    //swap fragments
                    FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.frame_fragmentholder, irf, "IRF").commit();
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