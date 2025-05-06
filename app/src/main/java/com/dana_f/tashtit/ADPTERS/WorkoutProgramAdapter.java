package com.dana_f.tashtit.ADPTERS;

import com.dana_f.model.WorkoutProgram;
import com.dana_f.tashtit.ADPTERS.BASE.GenericAdapter;

import java.util.List;

public class WorkoutProgramAdapter extends GenericAdapter<WorkoutProgram> {
    public WorkoutProgramAdapter(List<WorkoutProgram> items, int layoutId, InitializeViewHolder initializeViewHolder, BindViewHolder<WorkoutProgram> bindViewHolder) {
        super(items, layoutId, initializeViewHolder, bindViewHolder);
    }
}
