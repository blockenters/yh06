package com.block.placeapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.block.placeapp.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlaceActivity extends AppCompatActivity {

    ArrayList<Place> placeArrayList;
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        placeArrayList = (ArrayList<Place>) getIntent().getSerializableExtra("placeArrayList");
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                // 1. 지도의 중심은 나!
                LatLng myLocation = new LatLng(lat, lng);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

                // 2. 위에서 받은 어레이리스트에 들어있는 플레이스를
                //     마커로 만들어서 화면에 보이게 한다.
                for( Place place : placeArrayList){
                    LatLng latLng = new LatLng(place.geometry.location.lat, place.geometry.location.lng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng).title(place.name);
                    googleMap.addMarker(markerOptions);
                }

            }
        });
    }
}






