package com.mcgill.ecse428.foodme.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.mcgill.ecse428.foodme.R;

import java.io.IOException;
import java.util.List;

/**
 * This is to make a fragment that contains a google map with a specified location pinned
 */
public class MapFragment extends Fragment {
    //the address we will be putting on the map
    private String address;

    //Required empty public constructor
    public MapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map,container,false);

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);


        //generate the map Asynchronously
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //object for searching google location data
                Geocoder geocoder = new Geocoder(getContext());

                try {
                    //search for the address
                    List<Address> searchResult =
                            geocoder.getFromLocationName(address, 1);

                    //make sure we actually got a result
                    if (searchResult.size() > 0) {
                        //parse the result
                        LatLng coords = new LatLng(searchResult.get(0).getLatitude(),
                                searchResult.get(0).getLongitude());

                        //get rid of old data
                        googleMap.clear();

                        //move maps and add marker
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 16));
                        googleMap.addMarker(new MarkerOptions().position(coords).title("MARKER").snippet("Snippet"));
                    }
                    //else{//TODO something should go here when the geocoder fails}
                } catch (IOException e){
                    //TODO something should probably go here
                }
            }
        });
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setArguments(@Nullable Bundle args){
        this.address = args.getString("ADDRESS");
    }
}
