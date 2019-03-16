package com.mcgill.ecse428.foodme.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.activity.EditAccountActivity;
import com.mcgill.ecse428.foodme.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;

public class SettingsFragment extends Fragment {


    private final static String KEY_USER_ID = "userID";
    private final static String KEY_PREFERENCE_THEME = "themePref";

    private Activity mActivity;


    private static int selectedTheme;


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

        TextView appHeader = rootView.findViewById(R.id.settingsAppHeader);
        TextView accountHeader = rootView.findViewById(R.id.settingsAccountHeader);

        switch (selectedTheme) {
            case (0):
                appHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_teal_500));
                accountHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_teal_500));
                break;
            case (1):
                appHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_blue_500));
                accountHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.material_blue_500));
                break;
            case (2):
                appHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
                accountHeader.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
                break;
        }


        noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userID != null && userID.equals("noAccount")) {
                    prefs.edit().remove(KEY_USER_ID).apply();
                    Intent I = new Intent(mActivity, LoginActivity.class);
                    startActivity(I);
                }

                else if (userID != null && !userID.equals("noAccount")) {
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
                prefs.edit().remove(KEY_USER_ID).apply();
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


}
