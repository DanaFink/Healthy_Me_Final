package com.dana_f.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dana_f.model.Meal;
import com.dana_f.model.MealPlanResponse;
import com.dana_f.repository.MealPlanRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MealPlanViewModel extends ViewModel {

    private final MealPlanRepository repository = new MealPlanRepository();

    private final MutableLiveData<MealPlanResponse> mealPlanLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private MealPlanResponse currentMealPlan;  // 🔹 Holds the most recent plan


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
                    currentMealPlan = repository.parseMealPlanResponse(json);
                    mealPlanLiveData.postValue(currentMealPlan);
                    for (Meal meal : currentMealPlan.getMeals()) {
                        fetchMealCalories(meal.getId());
                    }
                } else {
                    errorLiveData.postValue("Error: Unable to load meal plan (code " + response.code() + ")");
                }
            }
        });
    }
    public void fetchMealCalories(int mealId) {
        repository.getMealInformation(mealId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                errorLiveData.postValue("Failed to fetch meal info: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    Log.d("MealCaloriesResponse", json);

                    try {
                        JSONObject root = new JSONObject(json);

                        if (root.has("summary")) {
                            String summary = root.getString("summary");

                            // Extract "123 calories" from summary using regex
                            Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*calories", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(summary);

                            if (matcher.find()) {
                                double calories = Double.parseDouble(matcher.group(1));

                                //  Update the corresponding Meal object
                                if (currentMealPlan != null) {
                                    for (Meal m : currentMealPlan.getMeals()) {
                                        if (m.getId() == mealId) {
                                            m.setCalories(calories);
                                            mealPlanLiveData.postValue(currentMealPlan);
                                            break;
                                        }
                                    }
                                }
                                return;
                            } else {
                                errorLiveData.postValue("Calories not found in summary.");
                            }
                        } else {
                            errorLiveData.postValue("No summary found.");
                        }

                    } catch (JSONException e) {
                        Log.e("MealCaloriesError", "JSON Parsing error", e);
                        errorLiveData.postValue("Invalid meal response format.");
                    }
                } else {
                    errorLiveData.postValue("Meal info error: " + response.code());
                }
            }


        });
    }
}
