package com.dana_f.model;

import com.dana_f.model.BASE.BaseEntity;

import java.io.Serializable;

public class BasicNutritionResult extends BaseEntity implements Serializable {
    private String status;
    private String food;
    private String serving_size;
    private Nutrition nutrition; // ✅ renamed from 'data' to match the JSON

    public BasicNutritionResult() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getServing_size() {
        return serving_size;
    }

    public void setServing_size(String serving_size) {
        this.serving_size = serving_size;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public static class Nutrition implements Serializable {
        private double calories;
        private double protein_g;
        private double carbs_g;
        private double fat_g;
        private double fiber_g;
        private double sugar_g;

        public Nutrition() {
        }

        public double getCalories() {
            return calories;
        }

        public void setCalories(double calories) {
            this.calories = calories;
        }

        public double getProtein_g() {
            return protein_g;
        }

        public void setProtein_g(double protein_g) {
            this.protein_g = protein_g;
        }

        public double getCarbs_g() {
            return carbs_g;
        }

        public void setCarbs_g(double carbs_g) {
            this.carbs_g = carbs_g;
        }

        public double getFat_g() {
            return fat_g;
        }

        public void setFat_g(double fat_g) {
            this.fat_g = fat_g;
        }

        public double getFiber_g() {
            return fiber_g;
        }

        public void setFiber_g(double fiber_g) {
            this.fiber_g = fiber_g;
        }

        public double getSugar_g() {
            return sugar_g;
        }

        public void setSugar_g(double sugar_g) {
            this.sugar_g = sugar_g;
        }
    }
}
