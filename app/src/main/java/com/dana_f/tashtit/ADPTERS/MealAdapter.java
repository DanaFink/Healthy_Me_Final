package com.dana_f.tashtit.ADPTERS;

import com.dana_f.model.Meal;
import com.dana_f.tashtit.ADPTERS.BASE.GenericAdapter;

import java.util.List;

public class MealAdapter extends GenericAdapter<Meal> {

    public MealAdapter(List<Meal> items, int layoutId, InitializeViewHolder initializeViewHolder, BindViewHolder<Meal> bindViewHolder) {
        super(items, layoutId, initializeViewHolder, bindViewHolder);
    }
}
