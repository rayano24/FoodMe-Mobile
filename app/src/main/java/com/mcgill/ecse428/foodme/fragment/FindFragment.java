package com.mcgill.ecse428.foodme.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcgill.ecse428.foodme.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;



public class FindFragment extends Fragment {





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);

        return rootView;
    }


}