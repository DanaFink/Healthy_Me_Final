package com.dana_f.tashtit.ADPTERS;

import com.dana_f.tashtit.ADPTERS.BASE.GenericAdapter;

import java.util.List;

public class BodyTypeAdapter extends GenericAdapter<String> {
    public BodyTypeAdapter(List<String> items, int layoutId, InitializeViewHolder initializeViewHolder, BindViewHolder<String> bindViewHolder) {
        super(items, layoutId, initializeViewHolder, bindViewHolder);
    }

}
