package com.dana_f.model;

public class WaterDairy {
    public String idfs;
    public int customerID;
    public long date;
    public int amount;

    public WaterDairy(int amount, int customerID, long date, String idfs) {
        this.amount = amount;
        this.customerID = customerID;
        this.date = date;
        this.idfs = idfs;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getIdfs() {
        return idfs;
    }

    public void setIdfs(String idfs) {
        this.idfs = idfs;
    }
}
