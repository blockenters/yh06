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

public class MapActivity extends AppCompatActivity {

    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        place = (Place) getIntent().getSerializableExtra("place");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // 위에서 받아온 place 객체에 저장되어 있는 위도, 경도 꺼내서
                // 1. 지도의 위치를 이 위도,경도를 중심으로 해서 이동시킨다.
                LatLng latLng = new LatLng(place.geometry.location.lat, place.geometry.location.lng);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                // 2. 마커로 표시한다.
                MarkerOptions markerOptions = new MarkerOptions();
                if(place.name == null){
                    markerOptions.position(latLng).title( "상점명 없음" );
                }else {
                    markerOptions.position(latLng).title(place.name);
                }
                googleMap.addMarker(markerOptions);

            }
        });

    }
}






