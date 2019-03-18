package com.mcgill.ecse428.foodme.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.activity.EditAccountActivity;
import com.mcgill.ecse428.foodme.activity.LoginActivity;
import com.mcgill.ecse428.foodme.activity.MainActivity;
import com.mcgill.ecse428.foodme.activity.PreferenceActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;

public class SettingsFragment extends Fragment {


    private final static String KEY_USER_ID = "userID";
    private final static String KEY_PREFERENCE_THEME = "themePref";

    private FusedLocationProviderClient mFusedLocationClient;

    private Activity mActivity;

    private TextView locationButton;


    private static int selectedTheme;
    private final static String KEY_USER_LOCATION = "userLocation";
    private final static String KEY_USER_LOCATION_LONGITUDE = "latitude";
    private final static String KEY_USER_LOCATION_LATITUDE = "longitude";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        final String userID = prefs.getString(KEY_USER_ID, null);
        selectedTheme = prefs.getInt(KEY_PREFERENCE_THEME, 0);


        if (userID != null && !userID.equals("noAccount")) {
            // TODO if a user is signed in, we can change the purpose of this to a "manage account" button
        }


        TextView signOut = rootView.findViewById(R.id.signOutButton);
        TextView noAccountButton = rootView.findViewById(R.id.noAccountButton);
        TextView themeButton = rootView.findViewById(R.id.themeButton);
        locationButton = rootView.findViewById(R.id.locationButton);
        Button edit_preferences =  rootView.findViewById(R.id.preference_button);

        TextView appHeader = rootView.findViewById(R.id.settingsAppHeader);
        TextView locationHeader = rootView.findViewById(R.id.settingsLocationHeader);
        TextView accountHeader = rootView.findViewById(R.id.settingsAccountHeader);

        switch (selectedTheme) {
            case (0):
                appHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_teal_500));
                accountHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_teal_500));
                locationHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_teal_500));

                break;
            case (1):
                appHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_blue_500));
                accountHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_blue_500));
                locationHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_blue_500));

                break;
            case (2):
                appHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
                accountHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
                locationHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));

                break;
        }


        noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userID != null && userID.equals("noAccount")) {
                    prefs.edit().remove(KEY_USER_ID).apply();
                    Intent I = new Intent(mActivity, LoginActivity.class);
                    startActivity(I);
                } else if (userID != null && !userID.equals("noAccount")) {
                    // prefs.edit().remove(KEY_USER_ID).apply();
                    Intent I = new Intent(mActivity, EditAccountActivity.class);
                    startActivity(I);
                    mActivity.finish();
                }
            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // removing user data and revert to log in screen
                prefs.edit().clear().apply();
                Intent I = new Intent(mActivity, LoginActivity.class);
                startActivity(I);
                mActivity.finish();
            }
        });


        themeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog for theme selection

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.theme_dialog_title);

                builder.setSingleChoiceItems(R.array.theme_list, selectedTheme, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedTheme = which;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.edit().putInt(KEY_PREFERENCE_THEME, selectedTheme).apply();

                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        String location = prefs.getString(KEY_USER_LOCATION, null);

        if (location != null) {
            locationButton.setText(location);
        }


        // the purpose of this is to disable the location setting option if the user has already accepted location permissions (it makes manually setting location redundant)
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationButton.setEnabled(false);

            Geocoder gc = new Geocoder(mActivity);

            String lat = prefs.getString(KEY_USER_LOCATION_LATITUDE, null);
            String lng = prefs.getString(KEY_USER_LOCATION_LONGITUDE, null);

            try {
                List<Address> name = gc.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
                locationButton.setText(name.get(0).getLocality());
            } catch (IOException e) {
                e.printStackTrace();

            }


        }


        // otherwise, we have a dialog set up for location
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationDialog();

                edit_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(mActivity, PreferenceActivity.class);
                startActivity(I);
                mActivity.finish();
            }
        });


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

    /**
     * Opens a dialog so that the user can enter their location. Once entered, is committed to preferences.
     */
    private void openLocationDialog() {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final AutoCompleteTextView input = new AutoCompleteTextView(mActivity);


        alert.setTitle("Location");
        alert.setMessage("Update your current location");

        alert.setView(input);
        input.setThreshold(3);
        input.setAdapter(new AutoCompleteAdapter(mActivity, android.R.layout.simple_list_item_1));


        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                int count = input.getAdapter().getCount();

                // the user is forced to select a result from the geocode api to ensure precision

                if (count != 0) {

                    for (int i = 0; i < count; i++) {
                        if (input.getAdapter().getItem(i).equals(input.getText().toString())) {
                            try {
                                Geocoder geo = new Geocoder(mActivity);
                                List<Address> addresses = geo.getFromLocationName(input.getText().toString(), 1);
                                for (int address = 0; address < addresses.size(); address++) {
                                    String lat = Double.toString(addresses.get(address).getLatitude());
                                    String lng = Double.toString(addresses.get(address).getLongitude());
                                    prefs.edit().putString(KEY_USER_LOCATION_LATITUDE, lat).apply();
                                    prefs.edit().putString(KEY_USER_LOCATION_LONGITUDE, lng).apply();
                                    prefs.edit().putString(KEY_USER_LOCATION, input.getText().toString()).apply();
                                    locationButton.setText(input.getText().toString().replaceAll("_", " "));

                                }
                            } catch (IOException e) {
                                Toast.makeText(mActivity, "There was an error setting your location, please try again", Toast.LENGTH_LONG).show();
                                e.printStackTrace();

                            }
                        } else {
                            // geocode technically only gives 1 result as it is designed for unambiguous results, however, added in case
                            if (i == (count - 1)) {
                                Toast.makeText(mActivity, "Invalid location. Please suggest a suggested location.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }


            }

        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    /**
     * Performs the geocoder function to retrieve the adequate location name based on user input
     *
     * @param locationName any location name or address including landmarks
     * @return the location address
     */
    private ArrayList<String> getAddressInfo(String locationName) {
        ArrayList<String> list = new ArrayList<>();
        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
        try {
            List<Address> a = geocoder.getFromLocationName(locationName, 5);
            for (int i = 0; i < a.size(); i++) {
                //String city = a.get(i).getLocality();
                // String country = a.get(i).getCountryName();
                String address = a.get(i).getAddressLine(0);
                //  + "--" + (lat!=null? "," + lat:"") + "--" + (lng!=null? ", " + lng:"")
                list.add(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * Used to display suggestions in the autocomplete textview within the locations dialog
     */
    class AutoCompleteAdapter extends ArrayAdapter implements Filterable {


        private ArrayList<String> resultList;

        public AutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = getAddressInfo(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

    }


}
