package com.dana_f.model;

public class WorkExercise {

    public String workID;
    public String exerciseID;

    public WorkExercise(String exerciseID, String workID) {
        this.exerciseID = exerciseID;

        this.workID = workID;
    }

    public String getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(String exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }
}
