package com.mcgill.ecse428.foodme.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.adapters.PreferenceAdapter;
import com.mcgill.ecse428.foodme.adapters.RestaurantAdapter;
import com.mcgill.ecse428.foodme.adapters.RestaurantHistoryAdapter;
import com.mcgill.ecse428.foodme.model.Restaurant;
import com.mcgill.ecse428.foodme.model.RestaurantHistory;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
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
    private TextView noLocation, noRestaurants;

    private Activity mActivity;

    private final static String KEY_USER_LOCATION_LONGITUDE = "latitude";
    private final static String KEY_USER_LOCATION_LATITUDE = "longitude";
    private final static String KEY_USER_LOCATION = "userLocation";

    SharedPreferences prefs;
    private final static String KEY_USER_ID = "userID";
    private Spinner preferenceSpinner;
    private ArrayAdapter<String> dataAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        String storedLat = prefs.getString(KEY_USER_LOCATION_LATITUDE, null);
        String storedLng = prefs.getString(KEY_USER_LOCATION_LONGITUDE, null);
        String storedLocation = prefs.getString(KEY_USER_LOCATION, null);


        restaurantRecyclerView = rootView.findViewById(R.id.recyclerFindRestaurant);

        noLocation = rootView.findViewById(R.id.noLocation);
        noRestaurants = rootView.findViewById(R.id.noRestaurants);


        restaurantAdapter = new RestaurantAdapter(restaurantList);

        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(mActivity);
        restaurantRecyclerView.setLayoutManager(upcomingLayoutManager);
        restaurantRecyclerView.setItemAnimator(new DefaultItemAnimator());
        restaurantRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        restaurantRecyclerView.setAdapter(restaurantAdapter);

        preferenceSpinner = rootView.findViewById(R.id.preference_spinner);
        List<String> preferenceList = new ArrayList<>();
        dataAdapter = new ArrayAdapter<String>(mActivity,
                android.R.layout.simple_spinner_item, preferenceList);
        preferenceSpinner.setAdapter(dataAdapter);
        int spinnerPosition = 0;
        preferenceSpinner.setSelection(spinnerPosition);
        preferenceSpinner.setVisibility(View.GONE);

        getPreferences();

        preferenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String preference = preferenceSpinner.getSelectedItem().toString();
                if(preference.equals("No search preference")){
                    displayRestaurants(storedLat, storedLng);
                    return;
                }
                String[] values = preference.split(",");
                searchWithPreference(values);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


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
                                prefs.edit().putString(KEY_USER_LOCATION_LATITUDE, Double.toString(lat)).apply();
                                prefs.edit().putString(KEY_USER_LOCATION_LONGITUDE, Double.toString(lng)).apply();
                                prefs.edit().remove(KEY_USER_LOCATION).apply();
                                displayRestaurants(Double.toString(lat), Double.toString(lng));
                            }
                        }
                    });
            preferenceSpinner.setVisibility(View.VISIBLE);

        }

        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && storedLocation != null && storedLat != null && storedLng != null) {

            displayRestaurants(storedLat, storedLng);
            noLocation.setVisibility(View.GONE);


        }


        return rootView;
    }

    // requests location permissions
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.getLastLocation()
                                .addOnSuccessListener(mActivity, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
                                            double lat = location.getLatitude();
                                            double lng = location.getLongitude();
                                            prefs.edit().putString(KEY_USER_LOCATION_LATITUDE, Double.toString(lat)).apply();
                                            prefs.edit().putString(KEY_USER_LOCATION_LONGITUDE, Double.toString(lng)).apply();
                                            // the user location pref is used to indicate that the user has manually set preferences
                                            prefs.edit().remove(KEY_USER_LOCATION).apply();
                                            displayRestaurants(Double.toString(lat), Double.toString(lng));
                                        }
                                    }
                                });
                    }


                } else {
                    // permission denied, show notice
                    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
                    String storedLocation = prefs.getString(KEY_USER_LOCATION, null);

                    if (storedLocation == null) {
                        noLocation.setVisibility(View.VISIBLE);

                    } else {
                        String storedLat = prefs.getString(KEY_USER_LOCATION_LATITUDE, null);
                        String storedLng = prefs.getString(KEY_USER_LOCATION_LONGITUDE, null);
                        if (storedLat != null && storedLng != null) {
                            displayRestaurants(storedLat, storedLng);
                        }
                    }

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


        HttpUtils.get("search/distance/" + 0 + "/?longitude=" + lng + "&latitude=" + lat, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {

                    restaurantList.clear();

                    JSONArray mainArray = response.getJSONArray("businesses");


                    for (int i = 0; i < mainArray.length(); i++) {

                        JSONObject obj = mainArray.getJSONObject(i);
                        String name = obj.getString("name");
                        String price = "n/a";
                        if (obj.has("price")) {
                            price = obj.getString("price");
                        }
                        String distance = obj.getString("distance");

                        //get the restaurant's address
                        JSONObject locObj = obj.getJSONObject("location");
                        JSONArray locArr = locObj.getJSONArray("display_address");
                        String[] displayLocation = {locArr.getString(0), locArr.getString(1)};

                        String id = obj.getString("id");

                        JSONArray categories = obj.getJSONArray("categories");
                        JSONObject cuisineList = categories.getJSONObject(0);
                        String cuisine = cuisineList.getString("title");


                        BigDecimal bd = new BigDecimal(distance);
                        bd = bd.setScale(1, RoundingMode.HALF_UP);


                        restaurantList.add(new Restaurant(name, cuisine, price, bd.toString() + " miles", displayLocation, id));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (restaurantList.isEmpty())
                    noRestaurants.setVisibility(View.VISIBLE);
                else {
                    noRestaurants.setVisibility(View.GONE);

                }

                restaurantAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(mActivity, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

            }


        });


    }

    private void searchWithPreference(String[] values) {

        String radius = HttpUtils.convertToMeters(values[0]);
        String cuisine = values[1];
        String price = HttpUtils.convertToPrice(values[2]);
        String sortBy = values[3];
        //String storedLocation = prefs.getString(KEY_USER_LOCATION, null);
        String storedLat = prefs.getString(KEY_USER_LOCATION_LATITUDE, null);
        String storedLong = prefs.getString(KEY_USER_LOCATION_LONGITUDE, null);
        Geocoder geo = new Geocoder(mActivity);
        List<Address> location = null;
        try {
            location = geo.getFromLocation(Double.valueOf(storedLat), Double.valueOf(storedLong), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpUtils.get("search/filter/?location=" + "montreal" + "&radius=" + radius + "&price=" + price + "&cuisine=" + cuisine + "&sortby=" + sortBy, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {

                    restaurantList.clear();

                    JSONArray mainArray = response.getJSONArray("businesses");


                    for (int i = 0; i < mainArray.length(); i++) {

                        JSONObject obj = mainArray.getJSONObject(i);
                        String name = obj.getString("name");
                        String price = "n/a";
                        if (obj.has("price")) {
                            price = obj.getString("price");
                        }
                        String distance = obj.getString("distance");

                        //get the restaurant's address
                        JSONObject locObj = obj.getJSONObject("location");
                        JSONArray locArr = locObj.getJSONArray("display_address");
                        String[] displayLocation = {locArr.getString(0), locArr.getString(1)};

                        JSONArray categories = obj.getJSONArray("categories");
                        JSONObject cuisineList = categories.getJSONObject(0);
                        //String cuisine = cuisineList.getString("title");


                        BigDecimal bd = new BigDecimal(distance);
                        bd = bd.setScale(1, RoundingMode.HALF_UP);


                        restaurantList.add(new Restaurant(name, cuisine, price, bd.toString() + " miles", displayLocation));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (restaurantList.isEmpty())
                    noRestaurants.setVisibility(View.VISIBLE);
                else {
                    noRestaurants.setVisibility(View.GONE);

                }

                restaurantAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(mActivity, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

            }


        });
    }

    public void getPreferences() {
        List<String> preferenceList = new ArrayList<String>();
        preferenceList.add("No search preference");
        String username = prefs.getString(KEY_USER_ID, null);
        HttpUtils.get("preferences/" + username + "/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {
                    dataAdapter.clear();


                    for (int i = 0; i < response.length(); i++) {
                        JSONArray preference = (JSONArray) response.get(i);
                        System.out.println(preference);

                        preferenceList.add(preference.get(2) + "," + preference.get(1) + "," + preference.get(3) + "," + (String) preference.get(4));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dataAdapter.addAll(preferenceList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


}