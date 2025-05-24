package com.dana_f.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dana_f.model.Exercise;
import com.dana_f.model.WorkoutProgram;
import com.dana_f.repository.ExerciseRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ExerciseViewModel extends ViewModel {
    private final ExerciseRepository repository = new ExerciseRepository(); //how viewModel get acces to the data
    private final MutableLiveData<List<String>> bodyPartsLiveData = new MutableLiveData<>();  //list of bodypart
    private final MutableLiveData<List<Exercise>> exercisesLiveData = new MutableLiveData<>();  //list of exercises
    private final MutableLiveData<Map<String, List<Exercise>>> groupedExercisesLiveData = new MutableLiveData<>();  //map bodypart->exercises
    private final MutableLiveData<List<WorkoutProgram>> programsLiveData = new MutableLiveData<>(); //holds a list of default programs
    private final List<Exercise> allExercises = new java.util.ArrayList<>();


    private final String currentSelectedBodyPart = "";   // Save the current selected body part
    private final boolean currentEquipmentFilter = false;  // Save if equipment filter is active

    public LiveData<List<WorkoutProgram>> getPrograms() {
        return programsLiveData;
    }


    public MutableLiveData<List<String>> getBodyParts() {
        return bodyPartsLiveData;
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercisesLiveData;
    }

    public LiveData<Map<String, List<Exercise>>> getGroupedExercises() { //name for instanse :back -> gives list of backexercises
        return groupedExercisesLiveData;
    }

    public void filterByBodyPartAndEquipment(String bodyPart, boolean equipmentOnly) {
        List<Exercise> filtered = new ArrayList<>();

        for (Exercise ex : allExercises) {
            boolean matchesBodyPart = TextUtils.isEmpty(bodyPart) || ex.getBodyPart().equalsIgnoreCase(bodyPart);
            boolean matchesEquipment = !equipmentOnly || !"body weight".equalsIgnoreCase(ex.getEquipment());

            if (matchesBodyPart && matchesEquipment) {
                filtered.add(ex);
            }
        }

        exercisesLiveData.postValue(filtered);
    }


    public void filterByEquipmentOnly() {
        List<Exercise> filtered = new ArrayList<>();
        for (Exercise ex : allExercises) {
            if (!"body weight".equalsIgnoreCase(ex.getEquipment())) {
                filtered.add(ex);
            }
        }
        exercisesLiveData.postValue(filtered);
    }

    public void filterByName(String query) {
        if (TextUtils.isEmpty(query)) {
            // If search is empty -> show all
            exercisesLiveData.postValue(allExercises);
        } else {
            // Otherwise filter
            List<Exercise> filtered = new ArrayList<>();
            for (Exercise ex : allExercises) {
                if (ex.getName().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(ex);
                }
            }
            exercisesLiveData.postValue(filtered);
        }
    }


    public void fetchBodyParts() {
        repository.getBodyPartList(new Callback() {   //calling the repositort to start the network requst and pass a callback to handle result
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle error, e.g., log or update an error LiveData
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { //checks is the respons is ok - 200
                if (response.isSuccessful()) {
                    String json = response.body().string(); //get the Json resposnse as string
                    Type listType = new TypeToken<List<String>>() { //convert the Json into list of string using GSON
                    }.getType();
                    List<String> bodyParts = new Gson().fromJson(json, listType);
                    bodyPartsLiveData.postValue(bodyParts);
                }
            }

        });
    }


    private List<WorkoutProgram> buildDefaultPrograms(List<Exercise> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        List<Exercise> core = new ArrayList<>();
        List<Exercise> backCable = new ArrayList<>();
        List<Exercise> legsBW = new ArrayList<>();
        List<Exercise> chestMachine = new ArrayList<>();
        List<Exercise> assistedAbs = new ArrayList<>();
        List<Exercise> latsMachine = new ArrayList<>();
        List<Exercise> barbellArms = new ArrayList<>();
        List<Exercise> fullBarbell = new ArrayList<>();

        for (Exercise e : list) {
            if (!isValidGifUrl(e.getGifUrl())) continue;

            if ("waist".equals(e.getBodyPart()) && core.size() < 5) {
                core.add(e);
            }
            if ("back".equals(e.getBodyPart()) && "cable".equals(e.getEquipment()) && backCable.size() < 5) {
                backCable.add(e);
            }
            if ("upper legs".equals(e.getBodyPart()) && "body weight".equals(e.getEquipment()) && legsBW.size() < 5) {
                legsBW.add(e);
            }
            if ("chest".equals(e.getBodyPart()) && "leverage machine".equals(e.getEquipment()) && chestMachine.size() < 5) {
                chestMachine.add(e);
            }
            if ("waist".equals(e.getBodyPart()) && "assisted".equals(e.getEquipment()) && assistedAbs.size() < 5) {
                assistedAbs.add(e);
            }
            if ("back".equals(e.getBodyPart()) && "leverage machine".equals(e.getEquipment()) && latsMachine.size() < 5) {
                latsMachine.add(e);
            }
            if ("upper arms".equals(e.getBodyPart()) && "barbell".equals(e.getEquipment()) && barbellArms.size() < 5) {
                barbellArms.add(e);
            }
            if ("barbell".equals(e.getEquipment()) &&
                    (e.getBodyPart().equals("glutes") || e.getBodyPart().equals("quads") || e.getBodyPart().equals("back")) &&
                    fullBarbell.size() < 5) {
                fullBarbell.add(e);
            }
        }

        List<WorkoutProgram> out = new ArrayList<>();
        out.add(new WorkoutProgram(WorkoutProgram.Type.CORE, "Core Blast", core));
        out.add(new WorkoutProgram(WorkoutProgram.Type.BACK_CABLE, "Back & Bi Cable", backCable));
        out.add(new WorkoutProgram(WorkoutProgram.Type.LEGS_BODYWEIGHT, "Leg Burn", legsBW));
        out.add(new WorkoutProgram(WorkoutProgram.Type.CHEST_MACHINE, "Chest Crusher", chestMachine));
        out.add(new WorkoutProgram(WorkoutProgram.Type.ASSISTED_ABS, "Assisted Core", assistedAbs));
        out.add(new WorkoutProgram(WorkoutProgram.Type.LATS_MACHINE, "Lat Ladder", latsMachine));
        out.add(new WorkoutProgram(WorkoutProgram.Type.ARMS_BARBELL, "Barbell Arms", barbellArms));
        out.add(new WorkoutProgram(WorkoutProgram.Type.FULL_BODY_BARBELL, "Barbell Burn", fullBarbell));

        return out;
    }

    // 🔹 Utility method
    private boolean isValidGifUrl(String url) {
        return url != null && url.startsWith("https://v2.exercisedb.io/image/") && url.length() > 30;
    }



    public void fetchExercises() {
        repository.getAllExercises(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ExerciseVM", "Failed to fetch exercises", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Type listType = new TypeToken<List<Exercise>>() {
                    }.getType();
                    List<Exercise> exerciseList = new Gson().fromJson(json, listType);

                    allExercises.clear();
                    allExercises.addAll(exerciseList); // <-- save original list here

                    exercisesLiveData.postValue(exerciseList);

                    // Optional: also post grouped data
                    Map<String, List<Exercise>> grouped = repository.groupExercisesByBodyPart(exerciseList);
                    groupedExercisesLiveData.postValue(grouped);

                    programsLiveData.postValue(buildDefaultPrograms(exerciseList));
                } else {
                    Log.e("ExerciseVM", "Response not successful: " + response.code());
                }
            }
        });
    }
}

