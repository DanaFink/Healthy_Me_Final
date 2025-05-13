package com.dana_f.model;

import com.dana_f.model.BASE.BaseEntity;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Meal extends BaseEntity implements Serializable {



    private int id;

    private String title;
    private int readyInMinutes;
    private int servings;
    private String sourceUrl;
    private String image;
    private String imageType;

    private double calories;


    private boolean eaten;

    public Meal() {
    }

    public Meal(String image, String imageType, int readyInMinutes, int servings, String sourceUrl, String title) {

        this.image = image;
        this.imageType = imageType;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.sourceUrl = sourceUrl;
        this.title = title;
    }

    public boolean isEaten() {
        return eaten;
    }

    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }
    // Use this to get the ID from the API


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCalories() {
        return calories;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Override BaseEntity's ID to avoid Gson conflict
}
