package com.example.emon.tourmate.Other_class;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api_Response {
    private static Retrofit retrofit;
    private static String BASE_URL="https://api.openweathermap.org/data/2.5/";
    private static String base_url="https://maps.googleapis.com/maps/api/";


    public static Retrofit getWeather(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }
    public static Retrofit getRetrofit(){

        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }

}
