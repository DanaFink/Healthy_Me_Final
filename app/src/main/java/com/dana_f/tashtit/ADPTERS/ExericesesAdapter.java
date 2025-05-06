package com.dana_f.tashtit.ADPTERS;

import com.dana_f.model.Exercise;
import com.dana_f.tashtit.ADPTERS.BASE.GenericAdapter;

import java.util.List;

public class ExericesesAdapter extends GenericAdapter<Exercise> {


    public ExericesesAdapter(List<Exercise> items, int layoutId, InitializeViewHolder initializeViewHolder, BindViewHolder<Exercise> bindViewHolder) {
        super(items, layoutId, initializeViewHolder, bindViewHolder);
    }

}
