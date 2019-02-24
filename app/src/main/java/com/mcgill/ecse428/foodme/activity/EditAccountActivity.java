package com.mcgill.ecse428.foodme.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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



public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener {

    Button save_changes;
    EditText etFirstName, etLastName,etPassword;
//    TextView noAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        save_changes = (Button) findViewById(R.id.save_changes);
//        noAccountButton = (TextView) findViewById(R.id.noAccountButton);

        save_changes.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.save_changes:
                break;

//                case R.id.noAccountButton:
//                    startActivity(new Intent(this, EditAccountActivity.class));
//                    break;
        }
    }
}