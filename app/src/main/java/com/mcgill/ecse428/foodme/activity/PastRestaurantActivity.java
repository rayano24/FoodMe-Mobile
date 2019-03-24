package com.mcgill.ecse428.foodme.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cz.msebera.android.httpclient.Header;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


    private static TextView restaurantName, restaurantRating, restaurantAddress, restaurantCuisine, restaurantRatingMessage;
    private static Button rateButton;
    private final static String KEY_USER_ID = "userID";
    private static String restoID, restoName;

    private static Boolean alreadyLiked, alreadyDisliked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_restaurant);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PastRestaurantActivity.this);

        String username = prefs.getString(KEY_USER_ID, "noAccount");


        restaurantName = findViewById(R.id.individualHistoryRestaurantName);
        restaurantAddress = findViewById(R.id.individualHistoryRestaurantAddress);
        restaurantCuisine = findViewById(R.id.individualHistoryCuisine);
        restaurantRating = findViewById(R.id.individualHistoryRating);
        restaurantRatingMessage = findViewById(R.id.restaurantRatingMessage);

        rateButton = findViewById(R.id.historyRateButton);


        Bundle b = getIntent().getExtras();

        // String name = b.getString("historyRestaurantName");
        restoID = b.getString("historyRestaurantID");
        restoName = b.getString("historyRestaurantName");


        loadRestaurantInfo();

        alreadyLiked = false;
        alreadyDisliked = false;

        checkLiked(username);
        checkDisliked(username);


        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(username);
            }
        });


    }

    /**
     * Loads information about the restaurant into the activity including its name, address, cuisine and YELP rating
     */
    public void loadRestaurantInfo() {

        HttpUtils.get("search/businesses/?id=" + restoID, new RequestParams(), new JsonHttpResponseHandler() {

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
                    restaurantCuisine.setText(cuisine);
                    restaurantRating.setText(Integer.toString(rating) + "/5");

                    restaurantName.setVisibility(View.VISIBLE);
                    restaurantAddress.setVisibility(View.VISIBLE);
                    restaurantCuisine.setVisibility(View.VISIBLE);
                    restaurantRating.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(PastRestaurantActivity.this, "There was a network error", Toast.LENGTH_LONG).show();


            }

        });


    }


    /**
     * Creates a dialog where the user can either like a restaurant, dislike it, remove a like or remove a dislike
     *
     * @param username the user ID
     */
    private void createDialog(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PastRestaurantActivity.this);

        String positiveButton, negativeButton;

        if (alreadyDisliked) {
            builder.setTitle("Update rating");
            builder.setMessage("You previously disliked this restaurant.");
            positiveButton = "Remove Dislike";
            negativeButton = "Cancel";

        } else if (alreadyLiked) {
            builder.setTitle("Update rating");
            builder.setMessage("You previously liked this restaurant.");
            positiveButton = "Remove Like";
            negativeButton = "Cancel";
        } else {
            builder.setTitle("Set rating");
            builder.setMessage("How was your experience?");
            positiveButton = "Like";
            negativeButton = "Dislike";


        }
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (alreadyDisliked) {
                    alreadyDisliked = false;
                    ratingAction(username, 3);
                } else if (alreadyLiked) {
                    alreadyLiked = false;
                    ratingAction(username, 2);
                } else {
                    ratingAction(username, 0);

                }

            }
        });

        builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (alreadyDisliked || alreadyLiked) {
                    dialog.dismiss();

                } else {
                    ratingAction(username, 1);

                }
            }
        });

        builder.show();

    }


    /**
     * Method to like dislike remove like or remove dislike from a restaurant and update the ui
     *
     * @param username username
     * @param mode     0: add a like
     *                 1: add a dislike
     *                 2: remove a like
     *                 3: remove a dislike
     */
    private void ratingAction(String username, int mode) {
        //check input
        if (mode < 0 || mode > 3) return;

        //generate the query
        String[] MODES = {"/addliked/", "/adddisliked/", "/removeliked/", "/removedisliked/"};
        String url = "restaurants/" + username + MODES[mode] + restoID;
        if (mode < 2) {
            url = url + "/" + restoName;
        }

        //get data from db. upon completion update ui
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                restaurantRatingMessage.setVisibility(View.INVISIBLE);
                rateButton.setText("RATE");
                checkDisliked(username);
                checkLiked(username);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                restaurantRatingMessage.setVisibility(View.INVISIBLE);
                rateButton.setText("RATE");
                checkDisliked(username);
                checkLiked(username);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                restaurantRatingMessage.setVisibility(View.INVISIBLE);
                rateButton.setText("RATE");
                checkDisliked(username);
                checkLiked(username);
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    Toast.makeText(PastRestaurantActivity.this, errorResponse.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(PastRestaurantActivity.this, "There was a network error", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    /**
     * Checks if the user already likes this restaurant and updates the ui
     *
     * @param username
     */
    public void checkLiked(String username) {

        HttpUtils.get("restaurants/" + username + "/all/liked", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //check if the query returned empty

                try {
                    if (response.length() > 0) {
                        //search the data for the id
                        for (int i = 0; i < response.length(); i++) {
                            JSONArray array = (JSONArray) response.get(i);

                            //if we find the value, update the ui and return
                            if (array.get(0).toString().equals(restoID)) {
                                alreadyLiked = true;
                                restaurantRatingMessage.setVisibility((View.VISIBLE));
                                restaurantRatingMessage.setText("You previously liked this restaurant.");
                                rateButton.setText("MODIFY RATING");
                            }
                        }
                    }
                } catch (Exception e) {//JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
            }
        });
    }

    /**
     * Method to check whether a user already dislikes this restaurant and updates ui
     *
     * @param username
     */
    public void checkDisliked(String username) {
        HttpUtils.get("restaurants/" + username + "/all/disliked", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    if (response.length() > 0) {
                        //search for the id in the data
                        for (int i = 0; i < response.length(); i++) {
                            JSONArray array = (JSONArray) response.get(i);

                            if (array.get(0).toString().equals(restoID)) {
                                alreadyDisliked = true;
                                restaurantRatingMessage.setVisibility((View.VISIBLE));
                                restaurantRatingMessage.setText("You previously disliked this restaurant.");
                                rateButton.setText("MODIFY RATING");

                            }
                        }
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
