package com.dana_f.repository;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SpoonacularApiClient {

    public static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/";
    public static final String MEAL_PLAN_URL = BASE_URL + "recipes/mealplans/generate";

    private static OkHttpClient client;

    private static OkHttpClient getApiService() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .build();
        }
        return client;
    }

    private static Request.Builder createBaseRequest() {
        return new Request.Builder()
                .addHeader("x-rapidapi-key", "8cc51390e1mshbd145b7a8a4d03cp1bfbc3jsn035e30238942")
                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .addHeader("Content-Type", "application/json");
    }

    public void getDailyMealPlan(int targetCalories, String diet, String exclude, Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(MEAL_PLAN_URL).newBuilder()
                .addQueryParameter("timeFrame", "day")
                .addQueryParameter("targetCalories", String.valueOf(targetCalories));

        if (diet != null && !diet.isEmpty()) {
            urlBuilder.addQueryParameter("diet", diet);
        }

        if (exclude != null && !exclude.isEmpty()) {
            urlBuilder.addQueryParameter("exclude", exclude);
        }

        Request request = createBaseRequest()
                .url(urlBuilder.build())
                .get()
                .build();

        getApiService().newCall(request).enqueue(callback);
    }

    public void getMealInformation(int mealId, Callback callback) {
        String url = BASE_URL + "recipes/" + mealId + "/information";

        Request request = createBaseRequest()
                .url(url)
                .get()
                .build();

        getApiService().newCall(request).enqueue(callback);
    }

}
