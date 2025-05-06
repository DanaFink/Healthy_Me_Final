package com.dana_f.model;

public class MuscleExercise {
    public String muscleID;
    public String exerciseID;

    public MuscleExercise(String exerciseID,  String muscleID) {
        this.exerciseID = exerciseID;

        this.muscleID = muscleID;
    }

    public String getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(String exerciseID) {
        this.exerciseID = exerciseID;
    }


    public String getMuscleID() {
        return muscleID;
    }

    public void setMuscleID(String muscleID) {
        this.muscleID = muscleID;
    }
}
