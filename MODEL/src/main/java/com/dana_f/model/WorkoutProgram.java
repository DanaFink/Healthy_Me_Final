package com.dana_f.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class WorkoutProgram implements Serializable {
    public Type type;
    public String title;
    public List<Exercise> exercises;
    private String imgBase64;



    public WorkoutProgram() {
        this.type = null;
        this.title = null;
        this.exercises = null;
    }


    public WorkoutProgram(String title) {
        this.type = null;
        this.title = title;
        this.exercises = null;
    }

    public WorkoutProgram(String title, List<Exercise> exercises) {
        this.type = null;
        this.title = title;
        this.exercises = exercises;
    }

    public WorkoutProgram(Type type, String title, List<Exercise> exercises) {
        this.type = type;
        this.title = title;
        this.exercises = exercises;
    }
    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutProgram that = (WorkoutProgram) o;
        return type == that.type && Objects.equals(title, that.title) && Objects.equals(exercises, that.exercises);
    }

    public enum Type implements Serializable {
        CORE, BACK_CABLE, LEGS_BODYWEIGHT,
        CHEST_MACHINE, ASSISTED_ABS, LATS_MACHINE,
        ARMS_BARBELL, FULL_BODY_BARBELL, CUSTOM
    }

}
