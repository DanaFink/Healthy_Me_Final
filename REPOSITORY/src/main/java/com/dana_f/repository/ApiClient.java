package com.dana_f.repository;

import com.google.gson.Gson;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ApiClient {

    public static final String RAPID_API_URL = "https://exercisedb.p.rapidapi.com/";  //Bse url of the ExerciseDN api
    public static final String EXCERCISES_URL = RAPID_API_URL + "exercises"; //appened exericeses to the base URL to acces the Exercise data
    public static final String IMAGE_URL = RAPID_API_URL + "image"; //getting the GIF of the exercises
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
                .addHeader("X-RapidAPI-Key", "8cc51390e1mshbd145b7a8a4d03cp1bfbc3jsn035e30238942")  //my api key for Rapid
                .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")  //Host name of the API
                .addHeader("Content-Type", "application/json");  //Tells the server to expect json
    }

    public void getBodyPartList(Callback callback) { //why using callback? u cant do network requestes on the main thread or else it will throw error NetWorkONMainThreadExpection -> it could freez the app
        getApiService().newCall(createBaseRequest()  //call back runs call in the background and returns the result
                .url(EXCERCISES_URL + "/bodyPartList")
                .get()
                .build()).enqueue(callback);
    }

    public void getAllExercises(Callback callback) {
        getApiService().newCall(createBaseRequest()
                .url(HttpUrl.parse(EXCERCISES_URL)
                        .newBuilder()
                        .addQueryParameter("limit", "50")

//                        .addQueryParameter("limit", "0") // how much to fetch
//                        .addQueryParameter("offset", "5") // where to start from
                        .build())
                .get()
                .build()).enqueue(callback);
    }

    public void getImageByExerciseId(String exerciseId, Callback callback) {
        getApiService().newCall(createBaseRequest()
                .url(IMAGE_URL + "/" + exerciseId)
                .get()
                .build()).enqueue(callback);
    }

}
