package com.mcgill.ecse428.foodme.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.model.Restaurant;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
import androidx.fragment.app.FragmentTransaction;

/**
 * Class to make a fragment to display info about a single restaurant
 */
public class IndividualRestaurantFragment extends Fragment {
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 811;

    private String restaurantName, restaurantDistance, restaurantPrice, restaurantCuisine,
            restaurantAddress1, restaurantAddress2;
    private Button mapButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_individual_restaurant, container,
                false);

        //find the page items
        TextView name = (TextView) rootView.findViewById(R.id.rName);
        TextView cuisine = (TextView) rootView.findViewById(R.id.rCuisine);
        TextView price = (TextView) rootView.findViewById(R.id.rPrice);
        TextView distance = (TextView) rootView.findViewById(R.id.rDistance);
        TextView address1 = (TextView) rootView.findViewById(R.id.rAddress1);
        TextView address2 = (TextView) rootView.findViewById(R.id.rAddress2);
        mapButton = (Button)rootView.findViewById(R.id.MapBtn);

        //assign the values
        name.setText(restaurantName);
        address1.setText(restaurantAddress1);
        address2.setText(restaurantAddress2);
        cuisine.setText("Cuisine: " + restaurantCuisine);
        price.setText("Price Range: " + restaurantPrice);
        distance.setText("Distance: " + restaurantDistance);

        //add listener for the map button that switches fragments
        mapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MapFragment mf = new MapFragment();

                //this is how we will query for the location looks like:
                // "123 St-Catherine, Montreal, QC X1X1X1"
                String fullAddress = restaurantAddress1 + ", " + restaurantAddress2;

                //Pass the location to the map fragment and swap
                Bundle args = new Bundle();
                args.putString("ADDRESS", fullAddress);
                mf.setArguments(args);
                switchToMapFragment(mf, false, "mapFragment");
            }
        });

        return rootView;
    }

    /**
     * Standard mathod to pass the information about the individual restaurant via Bundle
     * @param args
     */
    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        restaurantName = args.getString("NAME");
        restaurantCuisine = args.getString("CUISINE");
        restaurantDistance = args.getString("DISTANCE");
        restaurantPrice = args.getString("PRICE");
        restaurantAddress1 = args.getString("ADDRESS1");
        restaurantAddress2 = args.getString("ADDRESS2");
    }

    /**
     * Helper method to switch to a mapFragment
     * @param fragment This is the mapFragment we want to switch to
     * @param addToStack
     * @param tag
     */
    public void switchToMapFragment(Fragment fragment, boolean addToStack, String tag){
        FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(addToStack) ft.addToBackStack(tag);

        Fragment f = fm.findFragmentByTag("IRF");
        int vgID = ((ViewGroup)getView().getParent()).getId();
        ft.replace(R.id.frame_fragmentholder,fragment).commit();

    }

}
