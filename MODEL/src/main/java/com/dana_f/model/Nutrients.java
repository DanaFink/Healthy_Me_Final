package com.dana_f.model;

import com.dana_f.model.BASE.BaseEntity;

import java.io.Serializable;

public class Nutrients extends BaseEntity implements Serializable {

    private double calories;
    private double protein;
    private double fat;
    private double carbohydrates;

    public Nutrients() {

    }

    public Nutrients(double calories, double carbohydrates, double fat, double protein) {
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }
}
