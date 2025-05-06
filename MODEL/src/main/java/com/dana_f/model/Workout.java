package com.dana_f.model;

public class Workout {

    public String customerID;
    public String description;
    public enum workType{morning,legs}

    public Workout(String customerID, String description) {
        this.customerID = customerID;
        this.description = description;

    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
