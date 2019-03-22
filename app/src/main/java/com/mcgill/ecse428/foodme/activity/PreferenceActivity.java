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
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.adapters.PreferenceAdapter;
import com.mcgill.ecse428.foodme.model.Preference;
import com.mcgill.ecse428.foodme.model.Restaurant;
import com.mcgill.ecse428.foodme.utils.HttpUtils;
import com.mcgill.ecse428.foodme.utils.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;

public class PreferenceActivity extends AppCompatActivity {
    private List<Preference> preferenceList = new ArrayList<>();
    private RecyclerView preferenceRecyclerView;
    private PreferenceAdapter preferenceAdapter;


    private final static String KEY_USER_ID = "userID";
    private final static String KEY_PREFERENCE_THEME = "themePref";
    private static int themeSelected = 0;


    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            // listener implementation

            if (key.equals(KEY_PREFERENCE_THEME)) {
                themeSelected = prefs.getInt(KEY_PREFERENCE_THEME, 0);
                recreate(); //restarts activity to apply change

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(listener);
        themeSelected = prefs.getInt(KEY_PREFERENCE_THEME, 0);


        switch (themeSelected) {
            case (0):
                setTheme(R.style.AppTheme);
                break;
            case (1):
                setTheme(R.style.AppTheme_Alternate);
                break;
            case (2):
                setTheme(R.style.AppTheme_Dark);
                break;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);


        preferenceRecyclerView = findViewById(R.id.recyclerPreferences);


        preferenceAdapter = new PreferenceAdapter(preferenceList);


        RecyclerView.LayoutManager upcomingLayoutManager = new LinearLayoutManager(PreferenceActivity.this);
        preferenceRecyclerView.setLayoutManager(upcomingLayoutManager);
        preferenceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        preferenceRecyclerView.addItemDecoration(new DividerItemDecoration(PreferenceActivity.this, LinearLayoutManager.VERTICAL));
        preferenceRecyclerView.setAdapter(preferenceAdapter);

        preferenceRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(PreferenceActivity.this.getApplicationContext(), preferenceRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            // Handling warm ups
            @Override
            public void onLongClick(View view, int position) {

                Preference preference = preferenceList.get(position);


                AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceActivity.this);

                builder.setTitle("Set as default preference?");
                builder.setMessage("This will set the configuration as your default preferencce ");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        setDefaultPreference(preference.getpID());

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        }));


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

        final TextView cuisineText = new TextView(PreferenceActivity.this);
        cuisineText.setText("Cuisine: ");
        final Spinner cuisineSpinner = new Spinner(PreferenceActivity.this);
        List<String> cuisineList = Arrays.asList("bakeries", "bars", "bistros", "burgers", "chinese", "coffee", "desserts", "diners", "foodtrucks", "french", "greek", "halal", "indian", "italian", "japanese", "korean", "lebanese", "mexican", "pakistani", "persian", "portuguese", "sandwiches", "salad", "seafood", "spanish", "syrian", "tacos", "vegetarian", "vietnamese");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PreferenceActivity.this,
                android.R.layout.simple_spinner_item, cuisineList);
        cuisineSpinner.setAdapter(dataAdapter);
        cuisineSpinner.setSelection(0);
        layout.addView(cuisineText);
        layout.addView(cuisineSpinner);

        final TextView priceText = new TextView(PreferenceActivity.this);
        priceText.setText("Price range: ");
        final Spinner priceSpinner = new Spinner(PreferenceActivity.this);
        List<String> priceList = Arrays.asList("$", "$$", "$$$", "$$$$");
        ArrayAdapter<String> dataAdapterP = new ArrayAdapter<String>(PreferenceActivity.this,
                android.R.layout.simple_spinner_item, priceList);
        priceSpinner.setAdapter(dataAdapterP);
        priceSpinner.setSelection(0);
        layout.addView(priceText);
        layout.addView(priceSpinner);

        final TextView sortText = new TextView(PreferenceActivity.this);
        sortText.setText("Sort results by: ");
        final Spinner sortBySpinner = new Spinner(PreferenceActivity.this);
        List<String> sortList = Arrays.asList("rating", "distance", "best_match", "review_count");
        ArrayAdapter<String> dataAdapterS = new ArrayAdapter<String>(PreferenceActivity.this,
                android.R.layout.simple_spinner_item, sortList);
        sortBySpinner.setAdapter(dataAdapterS);
        sortBySpinner.setSelection(0);
        layout.addView(sortText);
        layout.addView(sortBySpinner);

        final TextView radiusText = new TextView(PreferenceActivity.this);
        radiusText.setText("Max distance: ");
        final Spinner locationSpinner = new Spinner(PreferenceActivity.this);
        List<String> locationList = Arrays.asList("100m", "500m", "1km", "5km", "15km", "40km");
        ArrayAdapter<String> dataAdapterL = new ArrayAdapter<String>(PreferenceActivity.this,
                android.R.layout.simple_spinner_item, locationList);
        locationSpinner.setAdapter(dataAdapterL);
        locationSpinner.setSelection(0);
        layout.addView(radiusText);
        layout.addView(locationSpinner);


        builder.setView(layout);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String cuisine = String.valueOf(cuisineSpinner.getSelectedItem());
                String price = String.valueOf(priceSpinner.getSelectedItem());
                String sortBy = String.valueOf(sortBySpinner.getSelectedItem());
                String location = String.valueOf(locationSpinner.getSelectedItem());

                addPreference(location, cuisine, price, sortBy);
            }
        });

        builder.show();
    }


    public void setDefaultPreference(int pID) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PreferenceActivity.this);
        String username = prefs.getString(KEY_USER_ID, null);

        HttpUtils.post(username + "/setdefault/" + pID, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {
                    if (response.getBoolean("response")) {
                        Toast.makeText(PreferenceActivity.this, response.getString("response"), Toast.LENGTH_LONG).show(); // generic network error
                    } else {
                        Toast.makeText(PreferenceActivity.this, response.getString("error"), Toast.LENGTH_LONG).show(); // generic network error
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String
                    responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(PreferenceActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

            }


        });


    }


    /**
     * Add a new preference
     */
    public void addPreference(String location, String cuisine, String price, String sortBy) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PreferenceActivity.this);
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
                recreate();
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PreferenceActivity.this);
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
