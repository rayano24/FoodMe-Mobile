package com.mcgill.ecse428.foodme.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.adapters.RestaurantHistoryAdapter;
import com.mcgill.ecse428.foodme.model.RestaurantHistory;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView noHistory;

    private Activity mActivity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);


        historyRecyclerView = rootView.findViewById(R.id.historyRecyclerView);


        noHistory = rootView.findViewById(R.id.noHistory);

        historyAdapter = new RestaurantHistoryAdapter(historyList);

        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(mActivity);
        historyRecyclerView.setLayoutManager(upcomingLayoutManager);
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        historyRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        historyRecyclerView.setAdapter(historyAdapter);


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

        HttpUtils.get("/", new RequestParams(), new JsonHttpResponseHandler() {

                   @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    historyList.clear();


                    if (response.isNull("test")) {
                        response.getString("e");


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (historyList.isEmpty())
                    noHistory.setVisibility(View.VISIBLE);
                else {
                    noHistory.setVisibility(View.GONE);

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