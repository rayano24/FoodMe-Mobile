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

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        protected TextView name, cuisine, date, rating;
        private RestaurantAdapter.ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.historyRestaurantDate);
            cuisine = view.findViewById(R.id.historyRestaurantCuisine);
            date = view.findViewById(R.id.historyRestaurantDate);
            rating = view.findViewById(R.id.historyRestaurantRating);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(RestaurantAdapter.ItemClickListener itemClickListener){
            this.clickListener = itemClickListener;
        }

        public void onClick(View view){
            clickListener.onClick(view, getLayoutPosition(),false);
        }
        public boolean onLongClick(View view){
            clickListener.onClick(view, getLayoutPosition(), true);
            return true;
        }
    }

    public RestaurantHistoryAdapter(List<RestaurantHistory> historyList) {
        this.historyList = historyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_restaurant_history, parent, false);
;

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RestaurantHistory history = historyList.get(position);
        holder.name.setText(history.getName());
        holder.date.setText(history.getDate());
        holder.cuisine.setText(history.getCuisine());
        holder.rating.setText(history.getRating());

        holder.setClickListener(new RestaurantAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                List<RestaurantHistory> rList = historyList;
                if(isLongClick){
                    //for now, no additional functionality here
                    onClick(view, position, false);
                }
                else{
                    IndividualRestaurantFragment irf = new IndividualRestaurantFragment();

                    //prepare arguments
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", rList.get(position).getName());
                    bundle.putString("DATE", rList.get(position).getDate());
                    bundle.putString("CUISINE",rList.get(position).getCuisine());
                    bundle.putString("RATING",rList.get(position).getRating());
                    String[] address = rList.get(position).getAddress();
                    bundle.putString("ADDRESS1", address[0]);
                    bundle.putString("ADDRESS2", address[1]);
                    irf.setArguments(bundle);

                    //swap fragments
                    FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.frame_fragmentholder, irf).commit();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }


}