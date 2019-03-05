package com.example.emon.tourmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emon.tourmate.Other_class.Api_Response;
import com.example.emon.tourmate.Other_class.Api_service;
import com.example.emon.tourmate.WeatherClass.weather.WeatherApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.emon.tourmate.MainActivity.LAT;
import static com.example.emon.tourmate.MainActivity.LON;


public class WeatherActivity extends AppCompatActivity {
    private TextView temp,city,descriptiontv,maxtv,mintv,humudityTv,cloudsTv,windspeedTv,windDerectionTv,atmPtv,seaPtv,grndPtv;
    private Api_service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initialization();

        String url=String.format("weather?lat=%f&lon=%f&units=metric&appid=37cfea37fbcd0d05700fed0a97f01c75",LAT,LON);
        service= Api_Response.getWeather().create(Api_service.class);
        Call<WeatherApi> weatherApiCall = service.getCurrentweather(url);
        weatherApiCall.enqueue(new Callback<WeatherApi>() {
            @Override
            public void onResponse(Call<WeatherApi> call, Response<WeatherApi> response) {
                if (response.code()==200){

                    WeatherApi weatherResponse=response.body();
                    Toast.makeText(WeatherActivity.this,weatherResponse.getName() , Toast.LENGTH_SHORT).show();
                   temp.setText(weatherResponse.getMain().getTemp().toString()+"°C");
                   city.setText(weatherResponse.getName()+", "+weatherResponse.getSys().getCountry());

                   descriptiontv.setText(weatherResponse.getWeather().get(0).getDescription());
                   maxtv.setText(weatherResponse.getMain().getTempMax().toString()+"°C");
                   mintv.setText(weatherResponse.getMain().getTempMin().toString()+"°C");
                   humudityTv.setText("Hunidity: "+weatherResponse.getMain().getHumidity().toString()+"%");
                   cloudsTv.setText("Clouds: "+weatherResponse.getClouds().getAll().toString()+"%");
                   windspeedTv.setText("Speed: "+weatherResponse.getWind().getSpeed().toString()+"meter/sec");
                   windDerectionTv.setText("Direction: "+weatherResponse.getWind().getDeg().toString()+"degrees");
                   atmPtv.setText("Atm Pressure: "+String.valueOf(weatherResponse.getMain().getPressure())+"hPa");
                   seaPtv.setText("Sea_lavel: "+String.valueOf(weatherResponse.getMain().getSeaLevel())+"hPa");
                   grndPtv.setText("Ground_lavel: "+String.valueOf(weatherResponse.getMain().getGrndLevel())+"hPa");

                    //city,country,
                    //sunrise,sunset
                    //description of sky,wind,humidity
                    //temparature
                }
            }

            @Override
            public void onFailure(Call<WeatherApi> call, Throwable t) {

                Toast.makeText(WeatherActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void initialization() {
        temp=findViewById(R.id.temptvId);
        city=findViewById(R.id.citytvid);
        descriptiontv=findViewById(R.id.destvid);
        maxtv=findViewById(R.id.maxtemptvid);
        mintv=findViewById(R.id.mintemptvid);
        humudityTv=findViewById(R.id.humidityTvid);
        cloudsTv=findViewById(R.id.cloudsTvid);
        windspeedTv=findViewById(R.id.windspeedtvid);
        windDerectionTv=findViewById(R.id.windderectiontvid);
        atmPtv=findViewById(R.id.atmpressuretvid);
        seaPtv=findViewById(R.id.sealavepresstvid);
        grndPtv=findViewById(R.id.grnpresstvid);

    }
}
