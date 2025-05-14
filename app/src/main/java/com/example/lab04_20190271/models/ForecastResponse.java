package com.example.lab04_20190271.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {

    @SerializedName("location")
    private LocationDetails location;

    @SerializedName("forecast")
    private Forecast forecast;

    // Getters
    public LocationDetails getLocation() {
        return location;
    }

    public Forecast getForecast() {
        return forecast;
    }

    // Clases internas para el modelo de datos
    public static class LocationDetails {
        @SerializedName("id")
        private long id;

        @SerializedName("name")
        private String name;

        @SerializedName("region")
        private String region;

        @SerializedName("country")
        private String country;

        // Getters
        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getRegion() {
            return region;
        }

        public String getCountry() {
            return country;
        }
    }

    public static class Forecast {
        @SerializedName("forecastday")
        private List<ForecastDay> forecastday;

        public List<ForecastDay> getForecastday() {
            return forecastday;
        }
    }

    public static class ForecastDay {
        @SerializedName("date")
        private String date;

        @SerializedName("date_epoch")
        private long dateEpoch;

        @SerializedName("day")
        private Day day;

        // Getters
        public String getDate() {
            return date;
        }

        public long getDateEpoch() {
            return dateEpoch;
        }

        public Day getDay() {
            return day;
        }
    }

    public static class Day {
        @SerializedName("maxtemp_c")
        private double maxtempC;

        @SerializedName("mintemp_c")
        private double mintempC;

        @SerializedName("avgtemp_c")
        private double avgtempC;

        @SerializedName("condition")
        private Condition condition;

        // Getters
        public double getMaxtempC() {
            return maxtempC;
        }

        public double getMintempC() {
            return mintempC;
        }

        public double getAvgtempC() {
            return avgtempC;
        }

        public Condition getCondition() {
            return condition;
        }
    }

    public static class Condition {
        @SerializedName("text")
        private String text;

        @SerializedName("icon")
        private String icon;

        @SerializedName("code")
        private int code;

        // Getters
        public String getText() {
            return text;
        }

        public String getIcon() {
            return icon;
        }

        public int getCode() {
            return code;
        }
    }
}