package com.mcgill.ecse428.foodme.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mcgill.ecse428.foodme.activity.MainActivity;
import com.mcgill.ecse428.foodme.activity.PreferenceActivity;
import com.mcgill.ecse428.foodme.model.Preference;
import com.mcgill.ecse428.foodme.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

public class PreferenceAdapter extends RecyclerView.Adapter<PreferenceAdapter.MyViewHolder> {
    private List<Preference> preferenceList;
    //holds our parent's context
    private Context mContext;
    private final static String KEY_USER_ID = "userID";

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        protected TextView location, cuisine, price, sortBy;
        protected Button edit;
        private PreferenceAdapter.ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            location = view.findViewById(R.id.preferenceLocation);
            cuisine = view.findViewById(R.id.preferenceCuisine);
            price = view.findViewById(R.id.preferencePrice);
            sortBy = view.findViewById(R.id.preferenceSortBy);

            //add the click listeners
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(PreferenceAdapter.ItemClickListener itemClickListener){
            this.clickListener = itemClickListener;
        }

        public void onClick(View view){
            clickListener.onClick(view, getLayoutPosition(),false);
        }
        public boolean onLongClick(View view){
            clickListener.onClick(view, getLayoutPosition(),true);
            return true;
        }
    }


    public PreferenceAdapter(List<Preference> preferenceList) {
        this.preferenceList = preferenceList;
    }

    @Override
    public PreferenceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_preferences, parent, false);


        return new PreferenceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceAdapter.MyViewHolder holder, int position) {
        Preference preference = preferenceList.get(position);
        holder.location.setText(preference.getLocation());
        holder.price.setText(preference.getPrice());
        holder.cuisine.setText(preference.getCuisine());
        holder.sortBy.setText(preference.getSortBy());

        //Create the click listeners
        holder.setClickListener(new PreferenceAdapter.ItemClickListener(){
            @Override
            public void onClick(View view, int position, boolean isLongClick){
                if(isLongClick){
                    //for now, no additional functionality here
                    onClick(view, position, false);
                }
                else{
                    modifyPreference(preference, holder, position);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return preferenceList.size();
    }

    public interface ItemClickListener{
        void onClick (View view, int position, boolean isLongClick);
    }

    private void modifyPreference(Preference preference, MyViewHolder holder, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.location.getContext());
        builder.setTitle("Edit Preference");

        LinearLayout layout = new LinearLayout(holder.location.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final Spinner cuisineSpinner = new Spinner(holder.location.getContext());
        List<String> cuisineList = Arrays.asList("Mexican", "Chinese", "Indian", "FastFood");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(holder.location.getContext(),
                android.R.layout.simple_spinner_item, cuisineList);
        cuisineSpinner.setAdapter(dataAdapter);
        int spinnerPosition = dataAdapter.getPosition(preference.getCuisine());
        cuisineSpinner.setSelection(spinnerPosition);
        layout.addView(cuisineSpinner);

        final Spinner priceSpinner = new Spinner(holder.location.getContext());
        List<String> priceList = Arrays.asList("$", "$$", "$$$", "$$$$");
        ArrayAdapter<String> dataAdapterP = new ArrayAdapter<String>(holder.location.getContext(),
                android.R.layout.simple_spinner_item, priceList);
        priceSpinner.setAdapter(dataAdapterP);
        int spinnerPositionP = dataAdapter.getPosition(preference.getPrice());
        priceSpinner.setSelection(spinnerPositionP);
        layout.addView(priceSpinner);

        final Spinner sortBySpinner = new Spinner(holder.location.getContext());
        List<String> sortList = Arrays.asList("Rating", "Distance");
        ArrayAdapter<String> dataAdapterS = new ArrayAdapter<String>(holder.location.getContext(),
                android.R.layout.simple_spinner_item, sortList);
        sortBySpinner.setAdapter(dataAdapterS);
        int spinnerPositionS = dataAdapter.getPosition(preference.getSortBy());
        sortBySpinner.setSelection(spinnerPositionS);
        layout.addView(sortBySpinner);

        final EditText locationText = new EditText(holder.location.getContext());
        locationText.setText(preference.getLocation());
        layout.addView(locationText);


        builder.setView(layout);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String cuisine = String.valueOf(cuisineSpinner.getSelectedItem());
                String price = String.valueOf(priceSpinner.getSelectedItem());
                String sortBy = String.valueOf(sortBySpinner.getSelectedItem());
                String location = String.valueOf(locationText.getText());

                editPreference(preference, holder, cuisine, price, sortBy, location);
            }
        });

        builder.show();
    }

    private void editPreference(Preference preference, MyViewHolder holder, String cuisine, String price, String sortBy, String location){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(holder.location.getContext());
        String username = prefs.getString(KEY_USER_ID, null);
        HttpUtils.post("preferences/" + username + "/edit/" + preference.getpID() + "?location=" + location + "&cuisine=" + cuisine + "&price=" + price
                + "&sortBy=" + sortBy, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                notifyDataSetChanged();
                int pid = preference.getpID();
                preferenceList.remove(preference);
                preferenceList.add(new Preference(pid, location, cuisine, price, sortBy));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
