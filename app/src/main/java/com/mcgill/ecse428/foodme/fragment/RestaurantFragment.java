package com.mcgill.ecse428.foodme.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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
import com.mcgill.ecse428.foodme.adapters.RestaurantHistoryAdapter;
import com.mcgill.ecse428.foodme.model.RestaurantHistory;
import com.mcgill.ecse428.foodme.utils.HttpUtils;
import com.mcgill.ecse428.foodme.utils.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;

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


        if (username != null & username.equals("noAccount")) {
            historyNotice.setText(R.string.history_no_account);
            historyNotice.setVisibility(View.VISIBLE);
        } else {
            displayPastRestaurants(username);
            historyNotice.setText(R.string.history_no_history);
            historyNotice.setVisibility(View.VISIBLE);
        }

        historyRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(mActivity.getApplicationContext(), historyRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent I = new Intent(getActivity(), PastRestaurantActivity.class);
                I.putExtra("historyRestaurantID", historyList.get(position).getID());
                I.putExtra("historyRestaurantName", historyList.get(position).getName());
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

    /**
     * Displays a user's past visited restaurants
     *
     * @param username the user's username
     */
    public void displayPastRestaurants(String username) {

        HttpUtils.get("restaurants/" + username + "/all/visited", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {

                    historyList.clear();



                    for (int i = 0; i < response.length(); i++) {

                        JSONArray mainArray = response.getJSONArray(i);



                        String name = mainArray.getString(1);
                        String id = mainArray.getString(0);


                        historyList.add(new RestaurantHistory(name, id));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



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