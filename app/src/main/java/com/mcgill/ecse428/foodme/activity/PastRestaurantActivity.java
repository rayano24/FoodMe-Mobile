package com.mcgill.ecse428.foodme.activity;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.model.Restaurant;
import com.mcgill.ecse428.foodme.model.RestaurantHistory;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PastRestaurantActivity extends AppCompatActivity {


    private static TextView restaurantName, restaurantRating, restaurantAddress, restauratCuisine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_restaurant);



        restaurantName = findViewById(R.id.individualHistoryRestaurantName);
        restaurantAddress = findViewById(R.id.individualHistoryRestaurantAddress);
        restauratCuisine = findViewById(R.id.individualHistoryCuisine);
        restaurantRating = findViewById(R.id.individualHistoryRating);

        Bundle b = getIntent().getExtras();

       // String name = b.getString("historyRestaurantName");
        String ID = b.getString("historyRestaurantID");


        loadRestaurantInfo(ID);



    }

    /**
     * loads specific restaurant info into the activity
     *
     * @param ID the restaurant ID
     */
    public void loadRestaurantInfo(String ID) {

        HttpUtils.get("search/businesses/?id=" + ID , new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {

                    String name = response.getString("name");
                    JSONObject locationObject = response.getJSONObject("location");
                    String address = locationObject.getString("address1") + " " + locationObject.getString("city");
                    String cuisine = response.getString("price");
                    int rating = response.getInt("rating");


                    restaurantName.setText(name);
                    restaurantAddress.setText(address);
                    restauratCuisine.setText(cuisine);
                    restaurantRating.setText(rating);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    Toast.makeText(PastRestaurantActivity.this, errorResponse.getString("message"), Toast.LENGTH_LONG).show();
                }
                catch(JSONException e) {
                    Toast.makeText(PastRestaurantActivity.this, "There was a network error", Toast.LENGTH_LONG).show();
                }


            }

        });



    }
}
