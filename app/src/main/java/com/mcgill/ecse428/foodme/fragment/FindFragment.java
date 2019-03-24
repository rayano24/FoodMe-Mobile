package com.mcgill.ecse428.foodme.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
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
import java.util.Locale;

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

    //parameters for filtering
    private boolean[] pricePoints = {false,false,false,false};
    private int distanceRange = 2;
    private boolean filterApplied = false;

    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<String> dislikedList = new ArrayList<>();
    private RecyclerView restaurantRecyclerView;
    private RestaurantAdapter restaurantAdapter;
    private TextView noLocation, noRestaurants, searchLocationButton;
    private Button openFilterMenu;

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
        searchLocationButton = rootView.findViewById(R.id.searchLocation);
        openFilterMenu = rootView.findViewById(R.id.show_pref_filter_menu);

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

        updateDislikeList();

        preferenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String preference = preferenceSpinner.getSelectedItem().toString();
                if (preference.equals("No search preference")) {
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

        //set up and implement the filter button
        openFilterMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();

                //reset previous searches
                for(int i = 0; i<4; i++) pricePoints[i] = false;
                distanceRange = 2;
                filterApplied = false;

                //make the pop-out menu
                PopupMenu filterMenu = new PopupMenu(getActivity(), v);

                filterMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //update data when items are clicked
                        switch (item.getItemId()){
                            case R.id.verycheap:
                                filterApplied = true;
                                item.setChecked(!item.isChecked());
                                pricePoints[0] = item.isChecked();
                                break;
                            case R.id.cheap:
                                filterApplied = true;
                                item.setChecked(!item.isChecked());
                                pricePoints[1] = item.isChecked();
                                break;
                            case R.id.expensive:
                                filterApplied = true;
                                item.setChecked(!item.isChecked());
                                pricePoints[2] = item.isChecked();
                                break;
                            case R.id.veryexpensive:
                                filterApplied = true;
                                item.setChecked(!item.isChecked());
                                pricePoints[3] = item.isChecked();
                                break;
                            case R.id.close:
                                filterApplied = true;
                                item.setChecked(!item.isChecked());
                                distanceRange = 0;
                                break;
                            case R.id.medium:
                                filterApplied = true;
                                item.setChecked(!item.isChecked());
                                distanceRange = 1;
                                break;
                            case R.id.far:
                                filterApplied = true;
                                item.setChecked(!item.isChecked());
                                distanceRange = 2;
                                break;
                            case R.id.submit:   //this commits the changes
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                                displayRestaurants(prefs.getString(KEY_USER_LOCATION_LATITUDE,null),prefs.getString(KEY_USER_LOCATION_LONGITUDE, null));
                                return false;
                            default:
                                return false;
                        }
                        //This code keeps the panel open, if this method hasn't already returned
                        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                        item.setActionView(new View(getContext()));
                        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                            @Override
                            public boolean onMenuItemActionExpand(MenuItem item) {
                                return false;
                            }

                            @Override
                            public boolean onMenuItemActionCollapse(MenuItem item) {
                                return false;
                            }
                        });
                        return false;
                    }
                });
                filterMenu.inflate(R.menu.prefernce_filter);
                filterMenu.show();
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

            searchLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLocationDialog();
                }
            });

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
        updateDislikeList();

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

                        //filter disliked restos
                        String id = obj.getString("id");
                        if(dislikedList.contains(id)) continue;

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
                        String cuisine = cuisineList.getString("title");


                        BigDecimal bd = new BigDecimal(distance);
                        bd = bd.setScale(1, RoundingMode.HALF_UP);

                        //apply filtering
                        Restaurant toAdd = new Restaurant(name, cuisine, price, bd.toString() + " metres", displayLocation, id);
                        if(!filterRestaurant(toAdd) || !filterApplied) restaurantList.add(toAdd);


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

            /**
             * Method to decide whether a restaurant should be filtered
             * @param r The restaurant we want to decide about
             * @return true if the restaurant should be filtered
             */
            public boolean filterRestaurant(Restaurant r){
                //check if the price matches the filter
                if(r.getPrice().equals("$") && pricePoints[0]==false) return true;
                if(r.getPrice().equals("$$") && pricePoints[1] == false) return true;
                if(r.getPrice().equals("$$$") && pricePoints[2] == false) return true;
                if(r.getPrice().equals("$$$$") && pricePoints[3] == false) return true;

                //check if the distance is out of range
                double distance = Double.parseDouble(r.getDistance().replace(" metres",""));
                if(distance > 500.0 && distanceRange == 0) return true;
                if(distance > 1000.0 && distanceRange == 1) return true;
                return false;
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


                        restaurantList.add(new Restaurant(name, cuisine, price, bd.toString() + " metres", displayLocation));


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
        List<String> preferenceList = new ArrayList<>();
        List<Integer> pIDs = new ArrayList<>();


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
                        pIDs.add((Integer) preference.get(0));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dataAdapter.addAll(preferenceList);
                displayDefault(username, pIDs);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void displayDefault(String username, List<Integer> pIDs) {
        HttpUtils.get("users/" + username + "/getdefault", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {

                    JSONArray preference = (JSONArray) response.get(0);


                    int pID = preference.getInt(0);

                    for (int j = 0; j < pIDs.size(); j++) {

                        if (pID == (pIDs.get(j))) {
                            preferenceSpinner.setSelection(j + 1, true);
                            dataAdapter.notifyDataSetChanged();
                            break;

                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * Opens a dialog so that the user can enter their location. Once entered, is committed to preferences.
     */
    private void openLocationDialog() {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final AutoCompleteTextView input = new AutoCompleteTextView(mActivity);


        alert.setTitle("Location");
        alert.setMessage("Enter a search location");

        alert.setView(input);
        input.setThreshold(3);
        input.setAdapter(new AutoCompleteAdapter(mActivity, android.R.layout.simple_list_item_1));


        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(input.getAdapter()==null){

                }
                else {
                    int count = input.getAdapter().getCount();

                    // the user is forced to select a result from the geocode api to ensure precision

                    if (count != 0) {

                        for (int i = 0; i < count; i++) {
                            if (input.getAdapter().getItem(i).equals(input.getText().toString())) {
                                try {
                                    Geocoder geo = new Geocoder(mActivity);
                                    List<Address> addresses = geo.getFromLocationName(input.getText().toString(), 1);
                                    for (int address = 0; address < addresses.size(); address++) {
                                        String lat = Double.toString(addresses.get(address).getLatitude());
                                        String lng = Double.toString(addresses.get(address).getLongitude());
                                        prefs.edit().putString(KEY_USER_LOCATION_LATITUDE, lat).apply();
                                        prefs.edit().putString(KEY_USER_LOCATION_LONGITUDE, lng).apply();
                                        prefs.edit().putString(KEY_USER_LOCATION, input.getText().toString()).apply();
                                        searchLocationButton.setText(input.getText().toString().replaceAll("_", " "));
                                        displayRestaurants(lat, lng);
                                        break;

                                    }
                                } catch (IOException e) {
                                    Toast.makeText(mActivity, "There was an error setting your location, please try again", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();

                                }
                            } else {
                                // geocode technically only gives 1 result as it is designed for unambiguous results, however, added in case
                                if (i == (count - 1)) {
                                    Toast.makeText(mActivity, "Invalid location. Please suggest a suggested location.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } else {
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
                                                searchLocationButton.setText("Search Another Location");
                                                displayRestaurants(Double.toString(lat), Double.toString(lng));
                                            }
                                        }
                                    });
                        }
                    }
                }

            }

        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    /**
     * Used to display suggestions in the autocomplete textview within the locations dialog
     */
    class AutoCompleteAdapter extends ArrayAdapter implements Filterable {


        private ArrayList<String> resultList;

        public AutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            if(resultList==null) return 0;
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = getAddressInfo(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

    }
    /**
     * Performs the geocoder function to retrieve the adequate location name based on user input
     *
     * @param locationName any location name or address including landmarks
     * @return the location address
     */
    private ArrayList<String> getAddressInfo(String locationName) {
        ArrayList<String> list = new ArrayList<>();
        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
        try {
            List<Address> a = geocoder.getFromLocationName(locationName, 5);
            for (int i = 0; i < a.size(); i++) {
                //String city = a.get(i).getLocality();
                // String country = a.get(i).getCountryName();
                String address = a.get(i).getAddressLine(0);
                //  + "--" + (lat!=null? "," + lat:"") + "--" + (lng!=null? ", " + lng:"")
                list.add(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void updateDislikeList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = "restaurants/"+prefs.getString("userID", null)+"/all/disliked";
        HttpUtils.get(url , new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                boolean emptySet = false;
                try{ emptySet = response.getBoolean(0); }
                catch (Exception e){}
                try {
                    if(!emptySet) {
                        //make a new array
                        List<String> newList = new ArrayList<String>();

                        for(int i = 0; i < response.length(); i++){
                            JSONArray obj = (JSONArray) response.get(i);
                            newList.add(obj.getString(0));
                        }
                        dislikedList = newList;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {

            }
        });
    }
}