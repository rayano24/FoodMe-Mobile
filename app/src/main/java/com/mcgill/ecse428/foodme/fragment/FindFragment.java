package com.mcgill.ecse428.foodme.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.adapters.RestaurantAdapter;
import com.mcgill.ecse428.foodme.adapters.RestaurantHistoryAdapter;
import com.mcgill.ecse428.foodme.model.Restaurant;
import com.mcgill.ecse428.foodme.model.RestaurantHistory;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;


public class FindFragment extends Fragment {


    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 811;

    private List<Restaurant> restaurantList = new ArrayList<>();
    private RecyclerView restaurantRecyclerView;
    private RestaurantAdapter restaurantAdapter;
    private TextView noLocation;

    private Button randomRecommandation;

    private Activity mActivity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);


        restaurantRecyclerView = rootView.findViewById(R.id.recyclerFindRestaurant);

        noLocation = rootView.findViewById(R.id.noLocation);

        randomRecommandation = rootView.findViewById(R.id.button);

        restaurantAdapter = new RestaurantAdapter(restaurantList);

        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(mActivity);
        restaurantRecyclerView.setLayoutManager(upcomingLayoutManager);
        restaurantRecyclerView.setItemAnimator(new DefaultItemAnimator());
        restaurantRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        restaurantRecyclerView.setAdapter(restaurantAdapter);


        // note for anyone looking at this, I elected to put the location stuff here rather than the main activity so we can avoid using GSON and so we can modify the view here
        // LOCATION RELATED

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);

        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted

            // PERMISSIONS_REQUEST_FINE_LOCATION is an app-defined int constant. The callback method gets the result of the request.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);


        } else {
            // Permission is granted, get location
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(mActivity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                double lat = location.getLatitude();
                                double lng = location.getLongitude();
                                // TODO BACKEND METHOD
                            }
                        }
                    });

        }


        return rootView;
    }

    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.button:
                //TODO Backend method call
                break;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    // TODO BACKEND METHOD

                } else {
                    // permission denied, show notice
                    noLocation.setVisibility(View.VISIBLE);

                }
                return;
            }

        }
    }


    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mActivity = (Activity) context;

    }

    // deprecated below API 23
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.mActivity = activity;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //  displayRestaurants();

    }


    /**
     * Displays a list of restaurants sorted by location
     *
     * @param lat latitude of current user position
     * @param lng longitude of current user position
     */
    public void displayRestaurants(String lat, String lng) {

        HttpUtils.get("/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    restaurantList.clear();


                    if (response.isNull("test")) {
                        response.getString("e");


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

              /*  if (restaurantList.isEmpty())
                    noRestos.setVisibility(View.VISIBLE);
                else {
                    noRestos.setVisibility(View.GONE);

                } */

                restaurantAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(mActivity, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

            }


        });


    }


}