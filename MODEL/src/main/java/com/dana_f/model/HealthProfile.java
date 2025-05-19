package com.dana_f.model;

import com.dana_f.model.BASE.BaseEntity;

import java.io.Serializable;

public class HealthProfile extends BaseEntity implements Serializable {
    private double weightKg;
    private double heightCm;
    private int age;
    private String gender;
    private int dailyCalorieGoal;

    public HealthProfile() {
    }

    public HealthProfile(double weightKg, double heightCm, int age, String gender, int dailyCalorieGoal) {
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.age = age;
        this.gender = gender;
        this.dailyCalorieGoal = dailyCalorieGoal;
    }

    // Getters and Setters

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(double heightCm) {
        this.heightCm = heightCm;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getDailyCalorieGoal() {
        return dailyCalorieGoal;
    }

    public void setDailyCalorieGoal(int dailyCalorieGoal) {
        this.dailyCalorieGoal = dailyCalorieGoal;
    }

    public int calculateBMI() {
        double heightM = heightCm / 100.0;
        return (int) (weightKg / (heightM * heightM));
    }

    public int calculateBMR() {
        if ("Male".equalsIgnoreCase(gender)) {
            return (int) (66 + (13.7 * weightKg) + (5 * heightCm) - (6.8 * age));
        } else {
            return (int) (655 + (9.6 * weightKg) + (1.8 * heightCm) - (4.7 * age));
        }
    }
}
