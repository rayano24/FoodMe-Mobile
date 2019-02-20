package com.mcgill.ecse428.foodme.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.adapters.RestaurantHistoryAdapter;
import com.mcgill.ecse428.foodme.model.RestaurantHistory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Displays the user's restaurant history and allows them to rate past restaurants
 */
public class RestaurantFragment extends Fragment {


    private List<RestaurantHistory> historyList = new ArrayList<>();
    private RecyclerView historyRecyclerView;
    private RestaurantHistoryAdapter historyAdapter;
    private TextView noHistory;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);


        historyRecyclerView = rootView.findViewById(R.id.historyRecyclerView);


        noHistory = rootView.findViewById(R.id.noHistory);

        historyAdapter = new RestaurantHistoryAdapter(historyList);

        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(getActivity());
        historyRecyclerView.setLayoutManager(upcomingLayoutManager);
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        historyRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        historyRecyclerView.setAdapter(historyAdapter);



        return rootView;
    }

}