package com.mcgill.ecse428.foodme.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;


public class EditAccountActivity extends AppCompatActivity {

    Button modifyButton;
    EditText etFirstName, etLastName;

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
        setContentView(R.layout.activity_edit_account);

        String username = prefs.getString("userID", null);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        modifyButton = findViewById(R.id.save_changes);


        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFirstName.getText().toString().isEmpty()) {
                    modifyName(username, "changeFirstName", "newFName");

                }

                if (!etLastName.getText().toString().isEmpty()) {
                    modifyName(username, "changeLastName", "newLName");

                }            }
        });


    }





    /**
     * Changes either the user's first name or last name
     *
     * @param username   the user ID
     * @param typeFirst  set the url mode if its first name or last name (changeFirstName, changeLastName)
     * @param typeSecond set url mode ifi its first name or last name (newLName or newFNName)
     */
    private void modifyName(String username, String typeFirst, String typeSecond) {

        HttpUtils.post("users/" + typeFirst + "/" + username + "/" + typeSecond, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (!response.getBoolean("response")) {
                        //search for the id in the data
                        Toast.makeText(EditAccountActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EditAccountActivity.this, "Data successfully modified!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                try {
                    Toast.makeText(EditAccountActivity.this, errorResponse.getString("message"), Toast.LENGTH_LONG).show();

                } catch (NullPointerException | JSONException e) {
                    Toast.makeText(EditAccountActivity.this, "Network error", Toast.LENGTH_LONG).show();
                }


            }

        });

    }
}
