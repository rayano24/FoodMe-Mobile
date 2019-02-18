package com.mcgill.ecse428.foodme.activity;


import android.os.Bundle;
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

/**
 * This class is responsible for handling the bottom navigation view and switching between fragments based on nav selections.
 */
public class MainActivity extends AppCompatActivity {


    //List that holds the fragments
    private final List<Fragment> fragments = new ArrayList<>(3);

    private static final String TAG_FRAGMENT_FIND = "tag_frag_find";
    private static final String TAG_FRAGMENT_RESTAURANTS = "tag_frag_restaurants";
    private static final String TAG_FRAGMENT_SETTINGS = "TAG_FRAGMENT_SETTINGS";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_find:
                    switchFragment(0, TAG_FRAGMENT_FIND);
                    return true;
                case R.id.navigation_restaurants:
                    switchFragment(1, TAG_FRAGMENT_RESTAURANTS);
                    return true;
                case R.id.navigation_settings:
                    switchFragment(2, TAG_FRAGMENT_SETTINGS);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        buildFragmentsList();


        switchFragment(0, TAG_FRAGMENT_FIND);

    }


    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragmentholder, fragments.get(pos), tag)
                .commit();
    }


    private void buildFragmentsList() {
        FindFragment searchFragment = buildFindFragment();
        RestaurantFragment tripsFragment = buildRestaurantFragment();
        SettingsFragment settingsFragment = buildSettingsFragment();

        fragments.add(searchFragment);
        fragments.add(tripsFragment);
        fragments.add(settingsFragment);
    }

    private FindFragment buildFindFragment() {
        FindFragment fragment = new FindFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private RestaurantFragment buildRestaurantFragment() {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private SettingsFragment buildSettingsFragment() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

}
