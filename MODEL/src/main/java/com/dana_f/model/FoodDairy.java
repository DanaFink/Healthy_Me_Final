package com.dana_f.model;

public class FoodDairy {
    public int customerID;
    public String descrition;
    public long date;
    public String foodImg;

    public FoodDairy(int customerID, long date, String descrition, String foodImg, String idfs) {
        this.customerID = customerID;
        this.date = date;
        this.descrition = descrition;
        this.foodImg = foodImg;
        //this.idfs = idfs;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

//    public String getIdfs() {
//        return idfs;
//    }
//
//    public void setIdfs(String idfs) {
//        this.idfs = idfs;
//    }
}
