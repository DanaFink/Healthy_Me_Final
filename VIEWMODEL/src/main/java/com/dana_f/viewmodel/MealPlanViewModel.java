package com.dana_f.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dana_f.model.MealPlanResponse;
import com.dana_f.repository.MealPlanRepository;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MealPlanViewModel extends ViewModel {

    private final MealPlanRepository repository = new MealPlanRepository();

    private final MutableLiveData<MealPlanResponse> mealPlanLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<MealPlanResponse> getMealPlanLiveData() {
        return mealPlanLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchMealPlan(int calories, String diet, String exclude) {
        repository.getMealPlan(calories, diet, exclude, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                errorLiveData.postValue("Failed to fetch meal plan: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    MealPlanResponse mealPlan = repository.parseMealPlanResponse(json);
                    mealPlanLiveData.postValue(mealPlan);
                } else {
                    errorLiveData.postValue("Error: Unable to load meal plan (code " + response.code() + ")");
                }
            }
        });
    }
}
