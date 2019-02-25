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
import androidx.fragment.app.Fragment;

public class IndividualRestaurantFragment extends Fragment {
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 811;

    private String restaurantName, restaurantDistance, restaurantPrice, restaurantCuisine;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_individual_restaurant, container,
                false);

        Bundle bundle = getArguments();

        restaurantName =  bundle.getString("Name");
        restaurantDistance = bundle.getString("Distance");
        restaurantPrice = bundle.getString("Price");
        restaurantCuisine = bundle.getString("Cuisine");

        TextView nameText = (TextView) rootView.findViewById(R.id.rName);
        TextView distanceText = (TextView) rootView.findViewById(R.id.rDistance);
        TextView priceText = (TextView) rootView.findViewById(R.id.rPrice);
        TextView cuisineText = (TextView) rootView.findViewById(R.id.rCuisine);

        nameText.setText(restaurantName);
        distanceText.setText(restaurantDistance);
        priceText.setText(restaurantPrice);
        cuisineText.setText(restaurantCuisine);


        return rootView;
    }

}
