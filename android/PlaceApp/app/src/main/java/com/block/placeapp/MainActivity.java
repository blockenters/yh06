package com.block.placeapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.block.placeapp.adapter.PlaceAdapter;
import com.block.placeapp.api.NetworkClient;
import com.block.placeapp.api.PlaceApi;
import com.block.placeapp.config.Config;
import com.block.placeapp.model.Place;
import com.block.placeapp.model.PlaceList;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import dalvik.annotation.optimization.FastNative;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    EditText editKeyword;
    ImageView imgSearch;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<Place> placeArrayList = new ArrayList<>();
    PlaceAdapter adapter;


    LocationManager locationManager;
    LocationListener locationListener;

    // 현재 나의 위치를 나타내는 위도, 경도 멤버변수
    double lat;
    double lng;

    boolean isGPSReady = false;

    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("PLACE MAIN", "위도 : " + lat + " 경도 : "+lng);


        editKeyword = findViewById(R.id.editKeyword);
        imgSearch = findViewById(R.id.imgSearch);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // 현재 폰의 위치를 가져오는 코드 작성.
        // 1. 로케이션 매니저를 가져온다.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 2. 위치가 바뀔때마다 위치정보를 가져오는 코드 작성
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();

                isGPSReady = true;

                Log.i("PLACES MAIN", "위도 : " + lat + " , 경도 : " + lng);

            }
        };

        // 3. 로케이션 메니저에 위의 리스너를 적용.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },
                    100);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000,
                -1,
                locationListener);

        progressBar.setVisibility(View.GONE);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isGPSReady == false){
                    Snackbar.make(imgSearch,
                            "GPS로 위치를 찾고 있습니다. 잠시후에 검색해주세요",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                keyword = editKeyword.getText().toString().trim();

                if(keyword.isEmpty()){
                    return;
                }

                getNetworkData();

            }
        });


    }

    private void getNetworkData() {

        progressBar.setVisibility(View.VISIBLE);

        placeArrayList.clear();

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);

        PlaceApi api = retrofit.create(PlaceApi.class);

        Call<PlaceList> call = api.getPlaceList(
                                "ko",
                                lat+","+lng,
                                2000,
                                Config.PLACE_API_KEY,
                                keyword);
        call.enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){
                    PlaceList placeList = response.body();
                    placeArrayList.addAll( placeList.results );

                    adapter = new PlaceAdapter(MainActivity.this, placeArrayList);
                    recyclerView.setAdapter(adapter);

                }else{

                }
            }

            @Override
            public void onFailure(Call<PlaceList> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        "이 앱은 위치 허용이 필수입니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,
                    -1,
                    locationListener);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == R.id.menuPlace){

            Intent intent = new Intent(MainActivity.this, PlaceActivity.class);
            intent.putExtra("placeArrayList", placeArrayList);

            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}






