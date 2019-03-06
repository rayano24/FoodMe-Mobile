package com.mcgill.ecse428.foodme.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.model.Restaurant;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IndividualRestaurantFragment extends Fragment {
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 811;

    private String restaurantName, restaurantDistance, restaurantPrice, restaurantCuisine;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_individual_restaurant, container,
                false);

        //find the page items
        TextView name = (TextView) rootView.findViewById(R.id.rName);
        TextView cuisine = (TextView) rootView.findViewById(R.id.rCuisine);
        TextView price = (TextView) rootView.findViewById(R.id.rPrice);
        TextView distance = (TextView) rootView.findViewById(R.id.rDistance);

        //assign the values
        name.setText(restaurantName);
        cuisine.setText("Cuisine: " + restaurantCuisine);
        price.setText("Price Range: " + restaurantPrice);
        distance.setText("Distance: " + restaurantDistance);

        return rootView;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        restaurantName = args.getString("NAME");
        restaurantCuisine = args.getString("CUISINE");
        restaurantDistance = args.getString("DISTANCE");
        restaurantPrice = args.getString("PRICE");
    }
}
