package com.mcgill.ecse428.foodme.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcgill.ecse428.foodme.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Displays the user's trips and allows them to open an individual trip as an instance of TripActivity
 */
public class RestaurantFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);


        return rootView;
    }

}