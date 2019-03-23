package com.mcgill.ecse428.foodme.activity;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.model.RestaurantHistory;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

public class PastRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_restaurant);


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

        HttpUtils.get("restaurants/" + ID + "/all/visited", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                /*
                try {


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                */
            }





            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(PastRestaurantActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

            }


        });


    }
}
