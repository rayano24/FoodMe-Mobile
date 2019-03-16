package com.mcgill.ecse428.foodme.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
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
import com.mcgill.ecse428.foodme.fragment.FindFragment;
import com.mcgill.ecse428.foodme.model.Preference;
import com.mcgill.ecse428.foodme.model.Restaurant;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

public class PreferenceActivity extends AppCompatActivity {
    private List<Preference> preferenceList = new ArrayList<>();
    private RecyclerView preferenceRecyclerView;
    private PreferenceAdapter preferenceAdapter;
    private TextView noLocation, noRestaurants;
    //final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        preferenceRecyclerView = findViewById(R.id.recyclerPreferences);

        preferenceAdapter = new PreferenceAdapter(preferenceList);

        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(mActivity);
        preferenceRecyclerView.setLayoutManager(upcomingLayoutManager);
        preferenceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        preferenceRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        preferenceRecyclerView.setAdapter(preferenceAdapter);

        displayPreferences();
    }

    /**
     * Displays a list of user's preferences
     */
    public void displayPreferences() {

        HttpUtils.get("preferences/" + user + "/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {
                    preferenceList.clear();
                    JSONArray mainArray = response.getJSONArray("Array[1]");

                    for (int i = 0; i < mainArray.length(); i++) {

                        JSONObject obj = mainArray.getJSONObject(i);
                        String location = obj.getString("name");
                        String price = "n/a";
                        if(obj.has("price")) {
                            price = obj.getString("price");
                        }
                        String distance = obj.getString("distance");

                        //get the restaurant's address
                        JSONObject locObj = obj.getJSONObject("location");
                        JSONArray locArr = locObj.getJSONArray("display_address");
                        String[] displayLocation = {locArr.getString(0),locArr.getString(1)};

                        JSONArray categories = obj.getJSONArray("categories");
                        JSONObject cuisineList = categories.getJSONObject(0);
                        String cuisine = cuisineList.getString("title");


                        BigDecimal bd = new BigDecimal(distance);
                        bd = bd.setScale(1, RoundingMode.HALF_UP);


                        preferenceList.add(new Preference(location, cuisine, price, price));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                preferenceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(mActivity, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error
            }
        });
    }
}
