package com.example.emon.tourmate.Other_class;


import com.example.emon.tourmate.Nearby.NearbyResponse;
import com.example.emon.tourmate.WeatherClass.weather.WeatherApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api_service {
    @GET()
    Call<WeatherApi> getCurrentweather(@Url String url);

    @GET()
    Call<NearbyResponse> getNearby(@Url String url);
}
