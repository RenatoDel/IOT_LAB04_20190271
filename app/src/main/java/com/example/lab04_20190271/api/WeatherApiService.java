package com.example.lab04_20190271.api;


import com.example.lab04_20190271.models.ForecastResponse;
import com.example.lab04_20190271.models.FutureResponse;
import com.example.lab04_20190271.models.Location;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("search.json")
    Call<List<Location>> searchLocations(
            @Query("key") String apiKey,
            @Query("q") String query
    );

    @GET("forecast.json")
    Call<ForecastResponse> getForecast(
            @Query("key") String apiKey,
            @Query("q") String locationId,
            @Query("days") int days
    );

    @GET("future.json")
    Call<FutureResponse> getFutureForecast(
            @Query("key") String apiKey,
            @Query("q") String locationId,
            @Query("dt") String date
    );
}
