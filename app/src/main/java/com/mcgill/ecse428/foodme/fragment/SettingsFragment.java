package com.mcgill.ecse428.foodme.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcgill.ecse428.foodme.R;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;

public class SettingsFragment extends Fragment {





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);



        return rootView;
    }




}
