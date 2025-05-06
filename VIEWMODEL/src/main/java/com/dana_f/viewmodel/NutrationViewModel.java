package com.dana_f.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dana_f.model.BasicNutritionResult;
import com.dana_f.model.DetailedNutritionResult;
import com.dana_f.repository.NutrationRepository;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class NutrationViewModel extends ViewModel {

    public final NutrationRepository repository = new NutrationRepository(); //how viewModel get acces to the data
    public final MutableLiveData<BasicNutritionResult> basicNutrition = new MutableLiveData<>();
    public final MutableLiveData<DetailedNutritionResult> detailedNutrition = new MutableLiveData<>();

    public LiveData<BasicNutritionResult> getBasicNutrition() {
        return basicNutrition;
    }

    public LiveData<DetailedNutritionResult> getDetailedNutrition() {
        return detailedNutrition;
    }

    public void fetchBasicNutrition(String foodName) {
        Log.d("NutritionViewModel", "Sending request for food: " + foodName);

        repository.fetchBasicNutrition(foodName.trim().toLowerCase(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("NutritionViewModel", "API call failed: " + e.getMessage());
                basicNutrition.postValue(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("NutritionViewModel", "Response code: " + response.code());

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.d("NutritionViewModel", "Raw JSON: " + json);

                    BasicNutritionResult result = new Gson().fromJson(json, BasicNutritionResult.class);
                    basicNutrition.postValue(result);
                } else {
                    Log.w("NutritionViewModel", "Unsuccessful response: " + response.message());
                    basicNutrition.postValue(null);
                }
            }
        });
    }


    public void fetchDetailedNutrition(List<String> ingredients) {
        repository.fetchDetailedNutrition(ingredients, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                detailedNutrition.postValue(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    DetailedNutritionResult result = new Gson().fromJson(response.body().string(), DetailedNutritionResult.class);
                    detailedNutrition.postValue(result);
                } else {
                    detailedNutrition.postValue(null);
                }
            }
        });
    }
}
