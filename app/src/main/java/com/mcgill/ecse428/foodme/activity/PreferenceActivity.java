package com.mcgill.ecse428.foodme.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.adapters.PreferenceAdapter;
import com.mcgill.ecse428.foodme.model.Preference;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

public class PreferenceActivity extends AppCompatActivity {
    private List<Preference> preferenceList = new ArrayList<>();
    private RecyclerView preferenceRecyclerView;
    private PreferenceAdapter preferenceAdapter;

    SharedPreferences prefs;

    private final static String KEY_USER_ID = "userID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        preferenceRecyclerView = findViewById(R.id.recyclerPreferences);
        prefs = PreferenceManager.getDefaultSharedPreferences(PreferenceActivity.this);
        preferenceAdapter = new PreferenceAdapter(preferenceList);

        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(PreferenceActivity.this);
        preferenceRecyclerView.setLayoutManager(upcomingLayoutManager);
        preferenceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        preferenceRecyclerView.addItemDecoration(new DividerItemDecoration(PreferenceActivity.this, LinearLayoutManager.VERTICAL));
        preferenceRecyclerView.setAdapter(preferenceAdapter);

        displayPreferences();

        Button addPreference = (Button) findViewById(R.id.addPreference);
        addPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPreference();
            }
        });
    }

    public void createPreference() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceActivity.this);
        builder.setTitle("Add Search Preference");

        LinearLayout layout = new LinearLayout(PreferenceActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final Spinner cuisineSpinner = new Spinner(PreferenceActivity.this);
        List<String> cuisineList = Arrays.asList("Mexican", "Chinese", "Indian", "FastFood");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PreferenceActivity.this,
                android.R.layout.simple_spinner_item, cuisineList);
        cuisineSpinner.setAdapter(dataAdapter);
        cuisineSpinner.setSelection(0);
        layout.addView(cuisineSpinner);

        final Spinner priceSpinner = new Spinner(PreferenceActivity.this);
        List<String> priceList = Arrays.asList("$", "$$", "$$$", "$$$$");
        ArrayAdapter<String> dataAdapterP = new ArrayAdapter<String>(PreferenceActivity.this,
                android.R.layout.simple_spinner_item, priceList);
        priceSpinner.setAdapter(dataAdapterP);
        priceSpinner.setSelection(0);
        layout.addView(priceSpinner);

        final Spinner sortBySpinner = new Spinner(PreferenceActivity.this);
        List<String> sortList = Arrays.asList("Rating", "Distance");
        ArrayAdapter<String> dataAdapterS = new ArrayAdapter<String>(PreferenceActivity.this,
                android.R.layout.simple_spinner_item, sortList);
        sortBySpinner.setAdapter(dataAdapterS);
        sortBySpinner.setSelection(0);
        layout.addView(sortBySpinner);

        final EditText locationText = new EditText(PreferenceActivity.this);
        layout.addView(locationText);


        builder.setView(layout);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String cuisine = String.valueOf(cuisineSpinner.getSelectedItem());
                String price = String.valueOf(priceSpinner.getSelectedItem());
                String sortBy = String.valueOf(sortBySpinner.getSelectedItem());
                String location = String.valueOf(locationText.getText());

                addPreference(location, cuisine, price, sortBy);
            }
        });

        builder.show();
    }


    /**
     * Add a new preference
     */
    public void addPreference(String location, String cuisine, String price, String sortBy) {
        String username = prefs.getString(KEY_USER_ID, null);
        HttpUtils.post("preferences/" + username + "/add/?location=" + location + "&cuisine=" + cuisine + "&price=" + price
                + "&sortBy=" + sortBy, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    preferenceList.add(new Preference((Integer) response.get("pid"), location, cuisine, price, sortBy));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                preferenceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(PreferenceActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error
                Intent I = new Intent(PreferenceActivity.this, MainActivity.class);
                startActivity(I);
                finish();
            }
        });
    }

    /**
     * Displays a list of user's preferences
     */
    public void displayPreferences() {
        String username = prefs.getString(KEY_USER_ID, null);
        HttpUtils.get("preferences/" + username + "/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {
                    preferenceList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        JSONArray preference = (JSONArray) response.get(i);
                        System.out.println(preference);

                        preferenceList.add(new Preference((Integer) preference.get(0), (String) preference.get(2), (String) preference.get(1), (String) preference.get(3), (String) preference.get(4)));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                preferenceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(PreferenceActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error
                Intent I = new Intent(PreferenceActivity.this, MainActivity.class);
                startActivity(I);
                finish();
            }
        });
    }
}
