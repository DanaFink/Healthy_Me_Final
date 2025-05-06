package com.dana_f.repository;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class ApiClientNutrition {
    public static final String RAPID_API_URL = "https://nutrition-api6.p.rapidapi.com/analysis";  //Bse url of the ExerciseDN api
    public static final String RAPID_API_SINGLE_ITEM = "https://nutrition-api6.p.rapidapi.com/info";
    private static OkHttpClient client;
    private final Gson gson = new Gson();

    private static OkHttpClient getApiService() {  //getApiServise is a lazy initaliztion means the object client isnt created until its actually needed, is client has been initalized it creates a new one else reuturns the existing one
        if (client == null) {
            client = new OkHttpClient().newBuilder()
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return client;
    }

    private static Request.Builder createBaseRequest() {
        return new Request.Builder()
                .addHeader("X-RapidAPI-Key", "cc9604cd22mshd6d6c331182821fp1f22a9jsn4eee6f9da0db")  //my api key for Rapid
                .addHeader("X-RapidAPI-Host", "nutrition-api6.p.rapidapi.com")  //Host name of the API
                .addHeader("Content-Type", "application/json");  //Tells the server to expect json
    }

    public void getNutration(List<String> ingredients, Callback callback) {
        // 1. Convert ingredient list into JSON: {"ingr": ["150g grilled chicken breast"]}
        String json = gson.toJson(Collections.singletonMap("ingr", ingredients));

        // 2. Create a request body with JSON content

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        // 3. Build the HTTP POST request with headers and body

        Request request = createBaseRequest()
                .url(RAPID_API_URL)
                .post(body)
                .build();
        // 4. Send the request asynchronously (on a background thread)

        getApiService().newCall(request).enqueue(callback);
    }

    public void getBasicNutrition(String foodName, Callback callback) {
        HttpUrl url = HttpUrl.parse("https://nutrition-api6.p.rapidapi.com/info")
                .newBuilder()
                .addQueryParameter("food", foodName)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", "cc9604cd22mshd6d6c331182821fp1f22a9jsn4eee6f9da0db")
                .addHeader("x-rapidapi-host", "nutrition-api6.p.rapidapi.com")
                .build();

        getApiService().newCall(request).enqueue(callback);
    }
}
