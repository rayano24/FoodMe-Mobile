package com.mcgill.ecse428.foodme.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.activity.EditAccountActivity;
import com.mcgill.ecse428.foodme.activity.LoginActivity;
import com.mcgill.ecse428.foodme.activity.PreferenceActivity;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;

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


        TextView signOut = rootView.findViewById(R.id.signOutButton);
        TextView noAccountButton = rootView.findViewById(R.id.noAccountButton);
        TextView themeButton = rootView.findViewById(R.id.themeButton);
        locationButton = rootView.findViewById(R.id.locationButton);
        TextView editPreferences = rootView.findViewById(R.id.preference_button);

        TextView appHeader = rootView.findViewById(R.id.settingsAppHeader);
        TextView locationHeader = rootView.findViewById(R.id.settingsLocationHeader);
        TextView accountHeader = rootView.findViewById(R.id.settingsAccountHeader);
        TextView preferenceHeader = rootView.findViewById(R.id.settingsPreferenceHeader);


        TextView changePasswordButton = rootView.findViewById(R.id.changePassword);
        EditText oldPassword = rootView.findViewById(R.id.oldPassword);
        EditText newPassword = rootView.findViewById(R.id.newPassword);
        Button submitChangePasswordButton = rootView.findViewById(R.id.changePasswordButton);

        TextView deleteAccount = rootView.findViewById(R.id.deleteAccountButton);

        if (userID == null || userID.equals("noAccount")) {
            editPreferences.setVisibility(View.GONE);
            preferenceHeader.setVisibility(View.GONE);
            signOut.setVisibility(View.GONE);
        } else {
            noAccountButton.setText("Change your first or last name");
        }

        switch (selectedTheme) {
            case (0):
                appHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_teal_500));
                accountHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_teal_500));
                locationHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_teal_500));
                preferenceHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_teal_500));

                break;
            case (1):
                appHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_blue_500));
                accountHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_blue_500));
                locationHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_blue_500));
                preferenceHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_blue_500));

                break;
            case (2):
                appHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
                accountHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
                locationHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
                preferenceHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));

                break;
        }


        noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userID != null && userID.equals("noAccount")) {
                    prefs.edit().remove(KEY_USER_ID).apply();
                    Intent I = new Intent(mActivity, LoginActivity.class);
                    startActivity(I);
                    mActivity.finish();

                } else if (userID != null && !userID.equals("noAccount")) {
                    // prefs.edit().remove(KEY_USER_ID).apply();
                    Intent I = new Intent(mActivity, EditAccountActivity.class);
                    startActivity(I);
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


        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog();
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
            } catch (IOException | NullPointerException   e) {
                e.printStackTrace();

            }


        }


        // otherwise, we have a dialog set up for location
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationDialog();

            }
        });


        editPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(mActivity, PreferenceActivity.class);
                startActivity(I);
                //mActivity.finish();
            }
        });

        //setup tab for changing passwords
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if we click this, and the tab is visible, hide it
                if (oldPassword.getVisibility() == View.VISIBLE) {
                    oldPassword.setVisibility(View.GONE);
                    newPassword.setVisibility(View.GONE);
                    submitChangePasswordButton.setVisibility(View.GONE);
                } else {   //otherwise show it
                    oldPassword.setVisibility(View.VISIBLE);
                    newPassword.setVisibility(View.VISIBLE);
                    submitChangePasswordButton.setVisibility(View.VISIBLE);
                }
            }
        });

        //setup the submit button
        submitChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the data the user typed
                String oldP = oldPassword.getText().toString();
                String newP = newPassword.getText().toString();

                //build the url
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String username = prefs.getString("userID", null);

                String url = "users/changePassword/" + username + "/" + oldP + "/" + newP;

                AlertDialog.Builder confirm = new AlertDialog.Builder(getActivity());
                confirm.setTitle("Confirm password change");
                confirm.setMessage("Are you sure you want to change your password?");
                confirm.setPositiveButton("Yes, update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        HttpUtils.post(url, new RequestParams(), new JsonHttpResponseHandler() {
                            @Override
                            public void onFinish() {
                            }

                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                                Log.d("CHANGED", "PASSWORD");
                                Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                try {
                                    Toast.makeText(getContext(), errorResponse.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "Failed to change password", Toast.LENGTH_LONG).show();
                                }

                            }
                        });


                    }

                });

                confirm.setNegativeButton("No, cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                confirm.show();
                oldPassword.setVisibility(View.GONE);
                oldPassword.setText("");
                newPassword.setVisibility(View.GONE);
                newPassword.setText("");
                submitChangePasswordButton.setVisibility(View.GONE);
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
                                    break;

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

    /**
     * Opens a dialog to verify if a user wants to delete their account
     */
    private void openDeleteDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete account?");
        builder.setMessage("Are you sure that you want to delete your account? It cannot be recovered once deleted.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAccount();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Makes an HTTP request to delete the user account
     */
    private void deleteAccount() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String username = prefs.getString(KEY_USER_ID, "noAccount");

        HttpUtils.get("users/delete/" + username, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("response")) {
                        prefs.edit().clear().apply();
                        Intent I = new Intent(mActivity, LoginActivity.class);
                        startActivity(I);
                        mActivity.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                Toast.makeText(getContext(), "Failed to delete account", Toast.LENGTH_LONG).show();
            }
        });
    }


}
