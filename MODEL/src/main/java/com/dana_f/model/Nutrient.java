package com.dana_f.model;

import com.dana_f.model.BASE.BaseEntity;

import java.io.Serializable;

public class Nutrient extends BaseEntity implements Serializable {
    public double value;
    public String unit;

    public Nutrient() {
    }
}
