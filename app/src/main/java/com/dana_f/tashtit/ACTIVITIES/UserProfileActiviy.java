package com.dana_f.tashtit.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dana_f.helper.inputValidators.EntryValidation;
import com.dana_f.model.HealthProfile;
import com.dana_f.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.dana_f.tashtit.R;
import com.dana_f.viewmodel.CustomerViewModel;

public class UserProfileActiviy extends BaseActivity implements EntryValidation {
    private EditText etAge, etHeight, etWeight, etDailyCalories;
    private Spinner spinnerGender;
    private Button btnSave;

    private CustomerViewModel customerViewModel;
    private ArrayAdapter<String> genderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setRecyclerView();
        fillInfo();
        setListeners();
    }

    @Override
    protected void initializeViews() {
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etDailyCalories = findViewById(R.id.etDailyCalories);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSave = findViewById(R.id.btnSave);
    }

    protected void setRecyclerView() {
        // Set spinner options
        genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Male", "Female", "Other"});
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
    }

    protected void fillInfo() {
        HealthProfile profile = BaseActivity.currentMember.getHealthProfile();
        if (profile != null) {
            etAge.setText(String.valueOf(profile.getAge()));
            etHeight.setText(String.valueOf(profile.getHeightCm()));
            etWeight.setText(String.valueOf(profile.getWeightKg()));
            etDailyCalories.setText(String.valueOf(profile.getDailyCalorieGoal()));
            int genderIndex = genderAdapter.getPosition(profile.getGender());
            spinnerGender.setSelection(Math.max(genderIndex, 0));
        }
    }

    private void saveProfile() {
        int age = Integer.parseInt(etAge.getText().toString().trim());
        double height = Double.parseDouble(etHeight.getText().toString().trim());
        double weight = Double.parseDouble(etWeight.getText().toString().trim());
        String gender = spinnerGender.getSelectedItem().toString();

        HealthProfile profile = new HealthProfile(weight, height, age, gender, 0);
        int dailyGoal;
        if (TextUtils.isEmpty(etDailyCalories.getText().toString())) {
            dailyGoal = profile.calculateBMR(); //  use helper
        } else {
            dailyGoal = Integer.parseInt(etDailyCalories.getText().toString().trim());
        }
        profile.setDailyCalorieGoal(dailyGoal);
        BaseActivity.currentMember.setHealthProfile(profile);

        BaseActivity.currentMember.setBmiCompleted(true);

        // Save to Firebase
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        customerViewModel.update(BaseActivity.currentMember);

        Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
        finish(); // go back
    }

    @Override
    protected void setListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
                startActivity(new Intent(UserProfileActiviy.this, FirstPageActivity.class));
            }
        });
    }

    @Override
    protected void setViewModel() {

    }

    @Override
    public void setValidation() {

    }

    @Override
    public boolean validate() {
        return false;
    }
}