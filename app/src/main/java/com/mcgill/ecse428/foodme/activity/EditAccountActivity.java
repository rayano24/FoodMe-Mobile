package com.mcgill.ecse428.foodme.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.fragment.SettingsFragment;
import com.mcgill.ecse428.foodme.fragment.FindFragment;
import com.mcgill.ecse428.foodme.fragment.RestaurantFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;



public class EditAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
    }
}
