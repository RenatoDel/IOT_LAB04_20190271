package com.example.lab04_20190271.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiClient {

    private static final String BASE_URL = "https://api.weatherapi.com/v1/";
    private static final String API_KEY = "ec24b1c6dd8a4d528c1205500250305";

    private static WeatherApiClient instance;
    private WeatherApiService apiService;

    private WeatherApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(WeatherApiService.class);
    }

    public static synchronized WeatherApiClient getInstance() {
        if (instance == null) {
            instance = new WeatherApiClient();
        }
        return instance;
    }

    public WeatherApiService getApiService() {
        return apiService;
    }

    public String getApiKey() {
        return API_KEY;
    }
}
