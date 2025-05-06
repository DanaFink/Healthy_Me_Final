package com.dana_f.repository;

import com.dana_f.model.Exercise;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;

public class ExerciseRepository {

    private final ApiClient apiClient = new ApiClient(); //my class helper that hits the web API
    private final Gson gson = new Gson();  //gson used to convert raw json string into java objects


    public void getAllExercises(Callback callback) {
        apiClient.getAllExercises(callback);
    }

    public void getBodyPartList(Callback callback) {
        apiClient.getBodyPartList(callback);
    }


    public Map<String, List<Exercise>> groupExercisesByBodyPart(List<Exercise> exerciseList) {   //Takes a list of exercises and organizes them into a map based on their body part
        Map<String, List<Exercise>> groupedMap = new HashMap<>();  //This map holds body part names as keys (e.g., "chest") and a list of exercises for that body part as values.
        for (Exercise exercise : exerciseList) {  //For each exercise, get the body part it belongs to.
            String bodyPart = exercise.getBodyPart();
            groupedMap.computeIfAbsent(bodyPart, k -> new ArrayList<>()).add(exercise);  //If the body part is not yet in the map, create a new list for it
        }
        return groupedMap;
    }

}
