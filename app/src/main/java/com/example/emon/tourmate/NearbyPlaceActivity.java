package com.example.emon.tourmate;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emon.tourmate.Nearby.NearbyResponse;
import com.example.emon.tourmate.Nearby.Result;
import com.example.emon.tourmate.Other_class.Api_Response;
import com.example.emon.tourmate.Other_class.Api_service;
import com.example.emon.tourmate.Other_class.NearbyAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


import java.io.IOException;
import java.util.List;


import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.emon.tourmate.MainActivity.LAT;
import static com.example.emon.tourmate.MainActivity.LON;


public class NearbyPlaceActivity extends AppCompatActivity {
    private Api_service service;

    private int radious = 2000;
    private RecyclerView recyclerView;
    private NearbyAdapter adapter;
    private Spinner spinner;
    private String pageToken;


    List<Result> results;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_place);

        recyclerView = findViewById(R.id.recyclerviewID);
        spinner = findViewById(R.id.spinnerID);
        List<String> placeList= new ArrayList<>();
        placeList.add("Select Search Item");
        placeList.add("restaurant");
        placeList.add("mosque");
        placeList.add("bank");
        placeList.add("atm");
        placeList.add("doctor");
        placeList.add("hospital");
        placeList.add("shopping_mall");
        placeList.add("post_office");
        placeList.add("night_club");
        placeList.add("pharmacy");

        //https://developers.google.com/places/web-service/supported_types

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,placeList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String place_type= parent.getItemAtPosition(position).toString();
                getNearby(place_type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getNearby(String place_type) {
        String url = String.format("place/nearbysearch/json?location=%f,%f&radius=%d&type=%s&key=%s",LAT,LON,radious,place_type,getResources().getString(R.string.place_key));
        service = Api_Response.getRetrofit().create(Api_service.class);
        Call<NearbyResponse> nearbyResponseCall = service.getNearby(url);
        nearbyResponseCall.enqueue(new Callback<NearbyResponse>() {
            @Override
            public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                if (response.code()==200){
                    NearbyResponse nearbyResponse = response.body();
                    if (nearbyResponse.getNextPageToken()!=null){
                        pageToken= nearbyResponse.getNextPageToken();
                    }
                    results = nearbyResponse.getResults();
                    adapter = new NearbyAdapter(results);
                    recyclerView.setLayoutManager(new LinearLayoutManager(NearbyPlaceActivity.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<NearbyResponse> call, Throwable t) {
                Toast.makeText(NearbyPlaceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

