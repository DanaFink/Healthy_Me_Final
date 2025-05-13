package com.dana_f.tashtit.ACTIVITIES;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dana_f.helper.inputValidators.EntryValidation;
import com.dana_f.model.Customer;
import com.dana_f.model.Customers;
import com.dana_f.model.Meal;
import com.dana_f.model.MealPlanResponse;
import com.dana_f.model.Nutrients;
import com.dana_f.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.dana_f.tashtit.ADPTERS.BASE.GenericAdapter;
import com.dana_f.tashtit.ADPTERS.MealAdapter;
import com.dana_f.tashtit.R;
import com.dana_f.viewmodel.CustomerViewModel;
import com.dana_f.viewmodel.MealPlanViewModel;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class NutritionActivity extends BaseActivity implements EntryValidation {
    private TextView tvMealPlanner;
    private Spinner spinner;
    private EditText etExclude;
    private RecyclerView rvMeals;
    private Button btnSaveMeal;
    private Button btnGenrate;
    private PieChart pieChart;
//    private TextView tvCaloriesGoal;
    private TextView tvCalories;
    private CheckBox checkBox;

    private DonutProgress progressCalories;


    private Nutrients savedNutrients;


    private MealPlanViewModel viewModel;
    private CustomerViewModel customerViewModel;
    private MealAdapter mealAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nutrition);
        //getLayoutInflater().inflate(R.layout.activity_nutrition, findViewById(R.id.main));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setRecycleView();
        loadSavedMeals();
        setViewModel();

        setListeners();
    }

    @Override
    protected int getBottomNavgationMenuItemId() {
        return R.id.nav_nutrition;
    }


    @Override
    protected void initializeViews() {
        tvMealPlanner = findViewById(R.id.tvMealPlanner);
        etExclude = findViewById(R.id.etExclude);
        rvMeals = findViewById(R.id.rvMeals);
        btnSaveMeal = findViewById(R.id.btnSaveMeal);
        btnGenrate = findViewById(R.id.btnGenrate);
        progressCalories = findViewById(R.id.progressCalories);

        pieChart = findViewById(R.id.pieChart);
//        tvCaloriesGoal = findViewById(R.id.tvCaloriesGoal);
//        int calories = BaseActivity.currentMember.getHealthProfile().getDailyCalorieGoal();
////        tvCaloriesGoal.setText("Calorie Goal: " + calories);
//        savedNutrients = BaseActivity.currentMember.getSavedNutrients();
//        if (savedNutrients != null && (int) savedNutrients.getCalories() != calories) {
//            showRecalculateMealPlanDialog();
//        }

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"", "vegetarian", "vegan", "paleo"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }
    private void showRecalculateMealPlanDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Calorie Goal Changed")
                .setMessage("Your calorie goal has changed. Would you like to generate a new meal plan to match?")
                .setPositiveButton("Generate Now", (dialog, which) -> {
                    String selectedDiet = spinner.getSelectedItem().toString();
                    String excludeText = etExclude.getText().toString();
                    viewModel.fetchMealPlan(BaseActivity.currentMember.getHealthProfile().getDailyCalorieGoal(), selectedDiet, excludeText);
                })
                .setNegativeButton("Later", null)
                .show();
        savedNutrients=BaseActivity.currentMember.getSavedNutrients();
    }

    @Override
    protected void setListeners() {
        btnGenrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDiet = spinner.getSelectedItem().toString();
                String excludeText = etExclude.getText().toString();

                // Fetch meal plan using selected values
                viewModel.fetchMealPlan(BaseActivity.currentMember.getHealthProfile().getDailyCalorieGoal(), selectedDiet, excludeText);

            }
        });
        btnSaveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseActivity.currentMember.getSavedMeals() != null && !BaseActivity.currentMember.getSavedMeals().isEmpty()) {
                    if (customerViewModel == null) {
                        customerViewModel = new ViewModelProvider(NutritionActivity.this).get(CustomerViewModel.class);
                    }
                    BaseActivity.currentMember.setSavedNutrients(viewModel.getMealPlanLiveData().getValue().getNutrients());

                    // Save the current member with updated meals to Firebase
                    customerViewModel.update(BaseActivity.currentMember);

                    // Optional: feedback
                    showToast("Meal plan saved!");
                } else {
                    showToast("No meals to save. Please generate first.");
                }
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showNutritionChart(Nutrients nutrients) {
        if (nutrients == null) {
            pieChart.clear();
            pieChart.setNoDataText("No nutrition data available.");
            return;
        }

        List<PieEntry> entries = new ArrayList<>();

        // Only add entries that have meaningful values
        if (nutrients.getFat() > 0) {
            entries.add(new PieEntry((float) nutrients.getFat(), "Fat"));
        }
        if (nutrients.getCarbohydrates() > 0) {
            entries.add(new PieEntry((float) nutrients.getCarbohydrates(), "Carbs"));
        }
        if (nutrients.getProtein() > 0) {
            entries.add(new PieEntry((float) nutrients.getProtein(), "Protein"));
        }

        // If no valid data, show message
        if (entries.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("No nutrient values to show.");
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "Macronutrients");
        dataSet.setColors(Color.parseColor("#FF9800"), Color.parseColor("#4CAF50"), Color.parseColor("#2196F3"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        Description description = new Description();
        description.setText("Nutrient Breakdown");
        description.setTextSize(12f);
        pieChart.setDescription(description);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.animateY(1000);
        pieChart.invalidate(); // Refresh chart
    }
    private void updateCalorieProgress(List<Meal> meals) {
        if (meals == null) return;

        double totalEaten = 0;
        for (Meal meal : meals) {
            if (meal.isEaten()) {
                totalEaten += meal.getCalories();
            }
        }

        int goal = BaseActivity.currentMember.getHealthProfile().getDailyCalorieGoal();
        int progress = (int) Math.min(100, (totalEaten / goal) * 100);

        progressCalories.setMax(100);
        progressCalories.setProgress(progress);
        progressCalories.setText((int) totalEaten + " / " + goal + " kcal");
    }



    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(MealPlanViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        viewModel.getMealPlanLiveData().observe(this, mealPlanResponse -> {
            if (mealPlanResponse != null && mealPlanResponse.getMeals() != null) {
                BaseActivity.currentMember.setSavedMeals(mealPlanResponse.getMeals());

                BaseActivity.currentMember.setSavedNutrients(mealPlanResponse.getNutrients());  // ✅ Save nutrients
                customerViewModel.update(BaseActivity.currentMember); // ✅ Persist it

                mealAdapter.setItems(mealPlanResponse.getMeals());
                showNutritionChart(mealPlanResponse.getNutrients());


            }
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) showToast(error);
        });

    }

    public void setRecycleView() {
        mealAdapter = new MealAdapter(
                BaseActivity.currentMember.getSavedMeals(),  // List<Meal>
                R.layout.item_meal,
                holder -> {
                    holder.putView("image", holder.itemView.findViewById(R.id.imageView));
                    holder.putView("title", holder.itemView.findViewById(R.id.textViewTitle));
                    holder.putView("ready", holder.itemView.findViewById(R.id.textViewReady));
                    holder.putView("servings", holder.itemView.findViewById(R.id.textViewServings));
                    holder.putView("calories", holder.itemView.findViewById(R.id.textViewCalories));
                    holder.putView("checkbox", holder.itemView.findViewById(R.id.checkbox));

                },
                (holder, item, position) -> {
                    ((TextView) holder.getView("title")).setText(item.getTitle());
                    ((TextView) holder.getView("ready")).setText("Ready in " + item.getReadyInMinutes() + " mins");
                    ((TextView) holder.getView("servings")).setText("Servings: " + item.getServings());
                    ((TextView) holder.getView("calories")).setText("Calories: " + item.getCalories());


                    ImageView img = holder.getView("image");
                    String imgUrl = "https://spoonacular.com/recipeImages/" + item.getImage(); // Construct full image URL
                    Picasso.get().load(imgUrl).into(img); // Add Picasso to build.gradle if not added

                    CheckBox checkBox = holder.getView("checkbox");
                    checkBox.setOnCheckedChangeListener(null);
                    checkBox.setChecked(item.isEaten());

                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        item.setEaten(isChecked);

                        // Update UI
                        updateCalorieProgress(mealAdapter.getItems());

                        // Update model
                        BaseActivity.currentMember.setSavedMeals(mealAdapter.getItems());

                        // Persist change to Firebase
                        if (customerViewModel != null) {
                            customerViewModel.update(BaseActivity.currentMember);
                        }
                    });


                }
        );
        mealAdapter.setOnItemClickListener(new GenericAdapter.OnItemClickListener<Meal>() {
            @Override
            public void onItemClick(Meal meal, int position) {
                String url = meal.getSourceUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        rvMeals.setLayoutManager(new LinearLayoutManager(this));
        rvMeals.setAdapter(mealAdapter);

    }

    private void loadSavedMeals() {
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        customerViewModel.getAll();
        customerViewModel.getLiveDataCollection().observe(this, new Observer<Customers>() {
            @Override
            public void onChanged(Customers customers) {
                for (Customer cs : customers) {
                    if (cs.getIdFs().equals(BaseActivity.currentMember.getIdFs())) {
//                        mealAdapter.setItems(cs.getSavedMeals());
//                        showNutritionChart(cs.getSavedNutrients());
//                        updateCalorieProgress(cs.getSavedMeals());  // Add this line

                        BaseActivity.currentMember = cs;
                        savedNutrients = cs.getSavedNutrients();

                        mealAdapter.setItems(cs.getSavedMeals());
                        showNutritionChart(savedNutrients);
                        updateCalorieProgress(cs.getSavedMeals());

                        // Only check AFTER loading updated data
                        int goal = cs.getHealthProfile().getDailyCalorieGoal();
                        if (savedNutrients != null && Math.abs(savedNutrients.getCalories() - goal) > 5) {
                            showRecalculateMealPlanDialog();
                        }

                        break;
                    }

                    }
                }

        });
    }


    @Override
    public void setValidation() {

    }

    @Override
    public boolean validate() {
        return false;
    }
}