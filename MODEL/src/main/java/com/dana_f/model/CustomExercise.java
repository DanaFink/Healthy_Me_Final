package com.dana_f.model;


public class CustomExercise { // Represents a custom exercise inside a lesson
    private String exerciseId;
    private String name;
    private int sets;
    private int reps;
    private String bodyPart;
    private String equipment;
    private String gifUrl;

    public CustomExercise() {
    }

    public CustomExercise(String bodyPart, String equipment, String exerciseId, String gifUrl, String name, int reps, int sets) {
        this.bodyPart = bodyPart;
        this.equipment = equipment;
        this.exerciseId = exerciseId;
        this.gifUrl = gifUrl;
        this.name = name;
        this.reps = reps;
        this.sets = sets;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }
}
