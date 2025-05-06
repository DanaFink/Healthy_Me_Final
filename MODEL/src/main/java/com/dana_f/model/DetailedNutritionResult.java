package com.dana_f.model;

import com.dana_f.model.BASE.BaseEntity;

import java.io.Serializable;

public class DetailedNutritionResult extends BaseEntity implements Serializable {
    public Analysis analysis;

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public static class Analysis implements Serializable {
        public Nutrient calories;
        public Nutrient protein;
        public Nutrient fat;
        public Nutrient saturatedFat;
        public Nutrient transFat;
        public Nutrient carbohydrates;
        public Nutrient sugar;
        public Nutrient fiber;
        public Nutrient cholesterol;
        public Nutrient sodium;
        public Nutrient potassium;
        public Nutrient calcium;
        public Nutrient iron;
        public Nutrient vitaminA;
        public Nutrient vitaminC;
        public Nutrient servingWeight;
        public Nutrient totalWeight;

        public Analysis() {
        } // Required for Firebase and Gson

        public Nutrient getCalcium() {
            return calcium;
        }

        public void setCalcium(Nutrient calcium) {
            this.calcium = calcium;
        }

        public Nutrient getCalories() {
            return calories;
        }

        public void setCalories(Nutrient calories) {
            this.calories = calories;
        }

        public Nutrient getCarbohydrates() {
            return carbohydrates;
        }

        public void setCarbohydrates(Nutrient carbohydrates) {
            this.carbohydrates = carbohydrates;
        }

        public Nutrient getCholesterol() {
            return cholesterol;
        }

        public void setCholesterol(Nutrient cholesterol) {
            this.cholesterol = cholesterol;
        }

        public Nutrient getFat() {
            return fat;
        }

        public void setFat(Nutrient fat) {
            this.fat = fat;
        }

        public Nutrient getFiber() {
            return fiber;
        }

        public void setFiber(Nutrient fiber) {
            this.fiber = fiber;
        }

        public Nutrient getIron() {
            return iron;
        }

        public void setIron(Nutrient iron) {
            this.iron = iron;
        }

        public Nutrient getPotassium() {
            return potassium;
        }

        public void setPotassium(Nutrient potassium) {
            this.potassium = potassium;
        }

        public Nutrient getProtein() {
            return protein;
        }

        public void setProtein(Nutrient protein) {
            this.protein = protein;
        }

        public Nutrient getSaturatedFat() {
            return saturatedFat;
        }

        public void setSaturatedFat(Nutrient saturatedFat) {
            this.saturatedFat = saturatedFat;
        }

        public Nutrient getServingWeight() {
            return servingWeight;
        }

        public void setServingWeight(Nutrient servingWeight) {
            this.servingWeight = servingWeight;
        }

        public Nutrient getSodium() {
            return sodium;
        }

        public void setSodium(Nutrient sodium) {
            this.sodium = sodium;
        }

        public Nutrient getSugar() {
            return sugar;
        }

        public void setSugar(Nutrient sugar) {
            this.sugar = sugar;
        }

        public Nutrient getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(Nutrient totalWeight) {
            this.totalWeight = totalWeight;
        }

        public Nutrient getTransFat() {
            return transFat;
        }

        public void setTransFat(Nutrient transFat) {
            this.transFat = transFat;
        }

        public Nutrient getVitaminA() {
            return vitaminA;
        }

        public void setVitaminA(Nutrient vitaminA) {
            this.vitaminA = vitaminA;
        }

        public Nutrient getVitaminC() {
            return vitaminC;
        }

        public void setVitaminC(Nutrient vitaminC) {
            this.vitaminC = vitaminC;
        }
    }
}
