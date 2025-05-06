package com.dana_f.model;

import com.dana_f.model.BASE.BaseEntity;

import java.io.Serializable;
import java.util.List;

public class MealPlanResponse extends BaseEntity implements Serializable {
    private List<Meal> meals;
    private Nutrients nutrients;

    public MealPlanResponse() {
    }

    public MealPlanResponse(List<Meal> meals, Nutrients nutrients) {
        this.meals = meals;
        this.nutrients = nutrients;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }
}
