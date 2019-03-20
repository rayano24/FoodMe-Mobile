package com.mcgill.ecse428.foodme.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.activity.PastRestaurantActivity;
import com.mcgill.ecse428.foodme.activity.PreferenceActivity;
import com.mcgill.ecse428.foodme.adapters.RestaurantHistoryAdapter;
import com.mcgill.ecse428.foodme.model.Preference;
import com.mcgill.ecse428.foodme.model.Restaurant;
import com.mcgill.ecse428.foodme.model.RestaurantHistory;
import com.mcgill.ecse428.foodme.utils.HttpUtils;
import com.mcgill.ecse428.foodme.utils.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

/**
 * Displays the user's restaurant history and allows them to rate past restaurants
 */
public class RestaurantFragment extends Fragment {


    private List<RestaurantHistory> historyList = new ArrayList<>();
    private RecyclerView historyRecyclerView;
    private RestaurantHistoryAdapter historyAdapter;
    private TextView historyNotice;

    private Activity mActivity;

    private final static String KEY_USER_ID = "userID";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        String username = prefs.getString(KEY_USER_ID, "noAccount");


        historyRecyclerView = rootView.findViewById(R.id.historyRecyclerView);


        historyNotice = rootView.findViewById(R.id.historyNotice);

        historyAdapter = new RestaurantHistoryAdapter(historyList);

        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(mActivity);
        historyRecyclerView.setLayoutManager(upcomingLayoutManager);
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        historyRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        historyRecyclerView.setAdapter(historyAdapter);


        if (username != null & !username.equals("noAccount")) {
            historyNotice.setText(R.string.history_no_account);
            historyNotice.setVisibility(View.VISIBLE);
        } else {
            displayPastRestaurants(username);
            historyNotice.setText(R.string.history_no_history);
        }

        historyRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(mActivity.getApplicationContext(), historyRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent I = new Intent(getActivity(), PastRestaurantActivity.class);
                I.putExtra("historyRestaurantID", historyList.get(position).getID());
                startActivity(I);

            }

            // Handling warm ups
            @Override
            public void onLongClick(View view, int position) {



            }
        }));



        return rootView;
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


    // TODO fill with restaurant ID's, create on click
    /**
     * Displays a user's past visited restaurants
     *
     * @param username the user's username
     */
    public void displayPastRestaurants(String username) {

        HttpUtils.get(username + "/all/visited", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

             /*   try {

                    historyList.clear();

                    JSONArray mainArray = response.getJSONArray("businesses");


                    for (int i = 0; i < mainArray.length(); i++) {

                        JSONObject obj = mainArray.getJSONObject(i);
                        String name = obj.getString("name");


                        JSONArray categories = obj.getJSONArray("categories");
                        JSONObject cuisineList = categories.getJSONObject(0);
                        String cuisine = cuisineList.getString("title");


                        String date = "12/7/2018";


                        historyList.add(new RestaurantHistory(name, cuisine, date));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */


                if (historyList.isEmpty())
                    historyNotice.setVisibility(View.VISIBLE);
                else {
                    historyNotice.setVisibility(View.GONE);

                }

                historyAdapter.notifyDataSetChanged();

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(mActivity, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

            }


        });


    }


}