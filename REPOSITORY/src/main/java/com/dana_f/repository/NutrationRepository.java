package com.dana_f.repository;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.Callback;

public class NutrationRepository {


    private final ApiClientNutrition apiClient = new ApiClientNutrition(); //my class helper that hits the web API
    private final Gson gson = new Gson();  //gson used to convert raw json string into java objects


    // Exposes the method to analyze a list of ingredients
    public void fetchDetailedNutrition(List<String> ingredients, Callback callback) {
        apiClient.getNutration(ingredients, callback);
    }

    public void fetchBasicNutrition(String foodName, Callback callback) {
        apiClient.getBasicNutrition(foodName, callback);
    }

}
