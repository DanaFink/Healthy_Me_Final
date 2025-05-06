package com.dana_f.model;

import com.dana_f.model.BASE.BaseEntity;

import java.io.Serializable;

public class CalorieEntry extends BaseEntity implements Serializable {
    public String foodName;
    public double calories;
    public long timestamp;

    public CalorieEntry() {
    } // Required for Firebase

    public CalorieEntry(String foodName, double calories, long timestamp) {
        this.foodName = foodName;
        this.calories = calories;
        this.timestamp = timestamp;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
