package com.mcgill.ecse428.foodme.fragment;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.model.Restaurant;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
import androidx.fragment.app.FragmentTransaction;

import cz.msebera.android.httpclient.Header;

/**
 * Class to make a fragment to display info about a single restaurant
 */
public class IndividualRestaurantFragment extends Fragment {
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 811;
    String username = "noAccount";
    private String restaurantName, restaurantDistance, restaurantPrice, restaurantCuisine,
            restaurantAddress1, restaurantAddress2, restaurantID;
    private Button mapButton, likeButton, dislikeButton, unlikeButton, undislikeButton;
    private TableRow likeDislikeRow, unlikeRow, undislikeRow;
    private TableLayout likeBtnTable;
    private TableLayout rclosingTable;
    private TextView rclosing2;
    private ImageView rclosing;
    private boolean alreadyLiked, alreadyDisliked;
    private boolean rclosingcheck;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_individual_restaurant, container,
                false);

        //find the page items
        TextView name = (TextView) rootView.findViewById(R.id.rName);
        TextView cuisine = (TextView) rootView.findViewById(R.id.rCuisine);
        TextView price = (TextView) rootView.findViewById(R.id.rPrice);
        TextView distance = (TextView) rootView.findViewById(R.id.rDistance);
        TextView address1 = (TextView) rootView.findViewById(R.id.rAddress1);
        TextView address2 = (TextView) rootView.findViewById(R.id.rAddress2);
        likeButton = (Button) rootView.findViewById(R.id.LikeBtn);
        unlikeButton = (Button) rootView.findViewById(R.id.UnlikeBtn);
        dislikeButton = (Button) rootView.findViewById(R.id.DislikeBtn);
        undislikeButton = (Button) rootView.findViewById(R.id.UnDislikeButton);
        likeDislikeRow = (TableRow) rootView.findViewById(R.id.LikeDislikeRow);
        unlikeRow = (TableRow) rootView.findViewById(R.id.UnlikeRow);
        undislikeRow = (TableRow) rootView.findViewById(R.id.UndislikeRow);
        likeBtnTable = (TableLayout) rootView.findViewById(R.id.LikeBtnTable);
        rclosingTable = (TableLayout) rootView.findViewById(R.id.rclosingTable);
        rclosing = (ImageView) rootView.findViewById(R.id.rclosing);
        rclosing2 = (TextView) rootView.findViewById(R.id.rclosing2);
        mapButton = (Button) rootView.findViewById(R.id.MapBtn);
        alreadyLiked = false;
        alreadyDisliked = false;
        rclosingcheck = false;

        //assign the values
        name.setText(restaurantName);
        address1.setText(restaurantAddress1);
        address2.setText(restaurantAddress2);
        cuisine.setText("Cuisine: " + restaurantCuisine);
        price.setText("Price Range: " + restaurantPrice);
        distance.setText("Distance: " + restaurantDistance);

        updateLikeButtonVisibility();
        updateRestaurantClosingVisibility();


        //set listeners for all the buttons
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liking(username, 0);
                dislikeButton.setEnabled(false);
                //updateLikeButtonVisibility();
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liking(username, 1);
                likeButton.setEnabled(false);
                //updateLikeButtonVisibility();
            }
        });

        unlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liking(username, 2);
                dislikeButton.setEnabled(true);
                //updateLikeButtonVisibility();
            }
        });

        undislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liking(username, 3);
                likeButton.setEnabled(true);
                updateLikeButtonVisibility();
            }
        });

        //add listener for the map button that switches fragments
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapFragment mf = new MapFragment();

                //this is how we will query for the location looks like:
                // "123 St-Catherine, Montreal, QC X1X1X1"
                String fullAddress = restaurantAddress1 + ", " + restaurantAddress2;

                addToVisited(username, restaurantName, restaurantID);

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
     * Method to like dislike remove like or remove dislike from a restaurant and update the ui
     *
     * @param username username
     * @param mode     0: add a like
     *                 1: add a dislike
     *                 2: remove a like
     *                 3: remove a dislike
     */
    public void liking(String username, int mode) {
        //check input
        if (mode < 0 || mode > 3) return;

        //generate the query
        String[] MODES = {"/addliked/", "/adddisliked/", "/removeliked/", "/removedisliked/"};
        String url = "restaurants/" + username + MODES[mode] + restaurantID;
        if (mode < 2) url = url + "/" + restaurantName;

        //get data from db. upon completion update ui
        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                updateLikeButtonVisibility();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Toast.makeText(getContext(),"RESTAURANT LIKED!",Toast.LENGTH_SHORT);
                updateLikeButtonVisibility();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    updateLikeButtonVisibility();
                    Toast.makeText(getContext(), errorResponse.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    updateLikeButtonVisibility();
                    Toast.makeText(getContext(), "There was a network error", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /**
     * Standard mathod to pass the information about the individual restaurant via Bundle
     *
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
        restaurantID = args.getString("id");
    }

    /**
     * Helper method to switch to a mapFragment
     *
     * @param fragment   This is the mapFragment we want to switch to
     * @param addToStack
     * @param tag
     */
    public void switchToMapFragment(Fragment fragment, boolean addToStack, String tag) {
        FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (addToStack) ft.addToBackStack(tag);

        Fragment f = fm.findFragmentByTag("IRF");
        int vgID = ((ViewGroup) getView().getParent()).getId();
        ft.replace(R.id.frame_fragmentholder, fragment).commit();

    }

    /**
     * method to update the ui for the cases where the user is either not logged in or neither
     * likes nor dislike the restaurant. Should only be after check if the user likes/dislike a
     * restaurant.
     */
    public void updateLikeButtonVisibility() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = prefs.getString("userID", null);
        //user is not logged in
        if (username.equals("noAccount")) {
            likeBtnTable.setVisibility(View.GONE);
            likeDislikeRow.setVisibility(View.GONE);
            unlikeRow.setVisibility(View.GONE);
            undislikeRow.setVisibility(View.GONE);
            likeButton.setVisibility(View.GONE);
            dislikeButton.setVisibility(View.GONE);
            unlikeButton.setVisibility(View.GONE);
            likeButton.setVisibility(View.GONE);
            undislikeButton.setVisibility(View.GONE);

        } else {
            JsonHttpResponseHandler jhrh = new JsonHttpResponseHandler();

            likeBtnTable.setVisibility(View.VISIBLE);
            likeDislikeRow.setVisibility(View.VISIBLE);
            unlikeRow.setVisibility(View.GONE);
            undislikeRow.setVisibility(View.GONE);
            dislikeButton.setVisibility(View.VISIBLE);
            unlikeButton.setVisibility(View.GONE);
            likeButton.setVisibility(View.VISIBLE);
            undislikeButton.setVisibility(View.GONE);
            alreadyLiked(username);
            alreadyDisLiked(username);


        }
    }

    public void updateRestaurantClosingVisibility() {

        rclosing.setVisibility(View.GONE);
        rclosing2.setVisibility(View.GONE);
        notifyRestaurantclosing(restaurantID);


    }


    /**
     * Checks if the user already likes this restaurant and updates the ui
     *
     * @param username
     * @return true if they already like the restaurant
     */
    public boolean alreadyLiked(String username) {
        String url = "restaurants/" + username + "/all/liked";
        HttpUtils.get(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                boolean emptySet = false;
                //check if the query returned empty
                try {
                    emptySet = response.getBoolean(0);
                } catch (Exception e) {
                }
                try {
                    if (!emptySet) {
                        //search the data for the id
                        for (int i = 0; i < response.length(); i++) {
                            JSONArray array = (JSONArray) response.get(i);

                            //if we find the value, update the ui and return
                            if (array.get(0).toString().equals(restaurantID)) {
                                alreadyLiked = true;
                                likeBtnTable.setVisibility((View.VISIBLE));
                                likeDislikeRow.setVisibility(View.GONE);
                                unlikeRow.setVisibility(View.VISIBLE);
                                undislikeRow.setVisibility(View.GONE);
                                dislikeButton.setVisibility(View.GONE);
                                unlikeButton.setVisibility(View.VISIBLE);
                                likeButton.setVisibility(View.GONE);
                                undislikeButton.setVisibility(View.GONE);
                                return;
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
        return alreadyLiked;
    }

    /**
     * Method to check whether a user already dislikes this restaurant and updates ui
     *
     * @param username
     * @return true if the user already dislikes this restaurant
     */
    public boolean alreadyDisLiked(String username) {
        String url = "restaurants/" + username + "/all/disliked";
        HttpUtils.get(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                boolean emptySet = false;
                try {
                    emptySet = response.getBoolean(0);
                } catch (Exception e) {
                }
                try {
                    if (!emptySet) {
                        //search for the id in the data
                        for (int i = 0; i < response.length(); i++) {
                            JSONArray array = (JSONArray) response.get(i);

                            if (array.get(0).toString().equals(restaurantID)) {
                                alreadyDisliked = true;
                                likeBtnTable.setVisibility((View.VISIBLE));
                                likeDislikeRow.setVisibility(View.GONE);
                                unlikeRow.setVisibility(View.GONE);
                                undislikeRow.setVisibility(View.VISIBLE);
                                dislikeButton.setVisibility(View.GONE);
                                unlikeButton.setVisibility(View.VISIBLE);
                                likeButton.setVisibility(View.GONE);
                                undislikeButton.setVisibility(View.VISIBLE);
                                return;

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
        return alreadyDisliked;
    }

    /**
     * Method to check whether a user already dislikes this restaurant and updates ui
     *
     * @param restoID
     * @return true if restaurant is closing soon
     */

    public boolean notifyRestaurantclosing(String restoID) {
        String url = "search/get/closing/?id=" + restoID;
        HttpUtils.get(url, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                boolean emptySet = false;
                //check if the query returned empty
                try {
                    emptySet = response.getBoolean(0);
                } catch (Exception e) {
                }
                try {
                    if (!emptySet) {
                        // search the data for the id*/
                        for (int i = 0; i < response.length(); i++) {
                            JSONArray array = (JSONArray) response.get(i);

                            // if we find the value, update the ui and return
                            if (array.toString().equals(" true")) {
                                rclosingcheck = true;
                                rclosing.setVisibility(View.VISIBLE);
                                rclosing2.setVisibility(View.VISIBLE);

                                return;
                            }

                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {


            }
        });
        return rclosingcheck;

    }


    /*
     * Once a user selects the map, the restaurant will be added to their history
     * @param username the user ID
     */
    private void addToVisited(String username, String restaurantName, String restaurantID) {

        HttpUtils.post("restaurants/" + username + "/addvisited/" + restaurantID + "/" + restaurantName, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //check if the query returned empty


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {


            }
        });


    }
}
