package com.mcgill.ecse428.foodme.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.fragment.SettingsFragment;
import com.mcgill.ecse428.foodme.fragment.FindFragment;
import com.mcgill.ecse428.foodme.fragment.RestaurantFragment;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;


public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener {

    Button save_changes, delete_account;
    EditText etFirstName, etLastName,etPassword, etOldPassword;
//    TextView noAccountButton;

    private final static String KEY_USER_ID = "userID";

    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        save_changes = (Button) findViewById(R.id.save_changes);
//        noAccountButton = (TextView) findViewById(R.id.noAccountButton);
        delete_account = (Button) findViewById(R.id.deleteAccount);

        save_changes.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
            final String userID = prefs.getString(KEY_USER_ID, null);

            switch(v.getId()) {
                case R.id.save_changes:
                    if(userID!=null && !userID.equals("noAccount")) {
                        if (etPassword.getText().toString() != "" && etOldPassword.getText().toString() != "")
                            userChangePassword(userID, etPassword.getText().toString(), etOldPassword.getText().toString());
                    }
                break;

                case R.id.deleteAccount:
                    if(userID!=null && !userID.equals("noAccount"))
                        userDeleteAccount(userID);

//                case R.id.noAccountButton:
//                    startActivity(new Intent(this, EditAccountActivity.class));
//                    break;
        }
    }

    /**
     * Attempts to change password
     * @param oldPassword old password
     * @param username username
     * @param newPassword new password
     */
    public void userChangePassword(String username, String oldPassword, String newPassword) {

        final TextView tNewPass = (TextView) findViewById(R.id.etPassword);
        final TextView tOldPass = (TextView) findViewById(R.id.etOldPassword);

        HttpUtils.post("/users/changePassword/" + username +  "/?old=" + tOldPass.getText().toString() + "&new=" + tNewPass.getText().toString(), new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                tNewPass.setText("");
                tOldPass.setText("");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(EditAccountActivity.this, "There was an error, try again later.", Toast.LENGTH_LONG).show(); // generic network error



            }
        });
    }

    /**
     * Attempts to delete account
     * @param username username
     */
    public void userDeleteAccount(String username) {


        HttpUtils.post("/users/delete/" + username , new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

                prefs.edit().remove(KEY_USER_ID).apply();
                Intent I = new Intent(mActivity, LoginActivity.class);
                startActivity(I);
                mActivity.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(EditAccountActivity.this, "There was an error, try again later.", Toast.LENGTH_LONG).show(); // generic network error



            }
        });
    }
}
