package com.dana_f.model;

import com.dana_f.model.BASE.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Customer extends BaseEntity implements Serializable {
    //public long   birthDay;
    private String name;
    private String EMAIL;
    private String password;
    private int waterAmount;
    private List<WorkoutProgram> programs;
    private String profileImageUrl;
    private String img;
    private HealthProfile healthProfile;
    private List<CalorieEntry> calorieHistory;
    private boolean bmiCompleted;

    private MealPlanResponse savedMealPlan;


    private List<Meal> savedMeals;
    private Nutrients savedNutrients;

    public Customer() {
    }
    public Customer(String EMAIL, String name, String password, List<WorkoutProgram> programs) {
        //this.birthDay = birthDay;
        this.EMAIL = EMAIL;
        this.name = name;
        this.password = password;
        this.programs = programs;
    }


    public Customer(String EMAIL, String name, String password) {
        //this.birthDay = birthDay;
        this.EMAIL = EMAIL;
        this.name = name;
        this.password = password;
        this.programs = null;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Nutrients getSavedNutrients() {
        return savedNutrients;
    }

    public void setSavedNutrients(Nutrients nutrients) {
        this.savedNutrients = nutrients;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<Meal> getSavedMeals() {
        return savedMeals;
    }

    public void setSavedMeals(List<Meal> savedMeals) {
        this.savedMeals = savedMeals;
    }


    public boolean isBmiCompleted() {
        return bmiCompleted;
    }

    public void setBmiCompleted(boolean bmiCompleted) {
        this.bmiCompleted = bmiCompleted;
    }

    public List<WorkoutProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(List<WorkoutProgram> programs) {
        this.programs = programs;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public HealthProfile getHealthProfile() {
        return healthProfile;
    }

    public void setHealthProfile(HealthProfile healthProfile) {
        this.healthProfile = healthProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount = waterAmount;
    }

    public List<CalorieEntry> getCalorieHistory() {
        return calorieHistory;
    }

    public void setCalorieHistory(List<CalorieEntry> calorieHistory) {
        this.calorieHistory = calorieHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return password == customer.password && waterAmount == customer.waterAmount && Objects.equals(name, customer.name) && Objects.equals(EMAIL, customer.EMAIL);
    }

    public enum gender {Male, Female, other}
}
