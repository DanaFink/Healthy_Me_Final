package com.dana_f.repository;

import com.dana_f.model.MealPlanResponse;
import com.google.gson.Gson;

import okhttp3.Callback;

public class MealPlanRepository {

    private final SpoonacularApiClient apiClient = new SpoonacularApiClient();
    private final Gson gson = new Gson();

    // This method hits the Spoonacular API and returns the raw JSON string through the callback
    public void getMealPlan(int targetCalories, String diet, String exclude, Callback callback) {
        apiClient.getDailyMealPlan(targetCalories, diet, exclude, callback);
    }

    public void getMealInformation(int mealId, Callback callback) {
        apiClient.getMealInformation(mealId, callback);
    }

    public MealPlanResponse parseMealPlanResponse(String json) {
        return gson.fromJson(json, MealPlanResponse.class);
    }

}
