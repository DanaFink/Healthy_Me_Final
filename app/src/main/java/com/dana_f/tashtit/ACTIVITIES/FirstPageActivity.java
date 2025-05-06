package com.dana_f.tashtit.ACTIVITIES;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dana_f.helper.inputValidators.EntryValidation;
import com.dana_f.model.Customer;
import com.dana_f.model.Customers;
import com.dana_f.model.Exercise;
import com.dana_f.model.WorkoutProgram;
import com.dana_f.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.dana_f.tashtit.ADPTERS.BASE.GenericAdapter;
import com.dana_f.tashtit.ADPTERS.BASE.SwipeCallback;
import com.dana_f.tashtit.ADPTERS.BASE.SwipeConfig;
import com.dana_f.tashtit.ADPTERS.BodyTypeAdapter;
import com.dana_f.tashtit.ADPTERS.WorkoutProgramAdapter;
import com.dana_f.tashtit.R;
import com.dana_f.viewmodel.CustomerViewModel;
import com.dana_f.viewmodel.ExerciseViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class FirstPageActivity extends BaseActivity implements EntryValidation {

    private List<String> bodyParts;

    private List<WorkoutProgram> defaultPrograms;

    private List<WorkoutProgram> currentPrograms;
    private List<Exercise> exercises;

    private ImageView imgPlus;

    private TextView tvName;

    private RecyclerView rvDefultPlans;
    private BodyTypeAdapter adapter;

    private WorkoutProgramAdapter adapter1;

    private ExerciseViewModel viewModel;

    private CustomerViewModel customerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // setContentView(R.layout.activity_first_page);
        getLayoutInflater().inflate(R.layout.activity_first_page, findViewById(R.id.main));

        //getLayoutInflater().inflate(R.layout.activity_first_page, findViewById(R.id.content_frame));//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        initializeViews();

        setRecyclerView();

        setViewModel();

        setListeners();


    }

    @Override
    protected int getBottomNavgationMenuItemId() {
        return R.id.nav_home;
    }

    @Override
    protected void initializeViews() {
        bottomNavigationView.post(() -> setSelectedNavigationItem(R.id.nav_home));

        tvName = findViewById(R.id.tvName);

        if (BaseActivity.currentMember != null) {
            tvName.setText(BaseActivity.currentMember.getName());
        }

        rvDefultPlans = findViewById(R.id.rvDefultPlans);

        imgPlus = findViewById(R.id.imgPlus);

    }

    @Override
    protected void setListeners() {
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, AddExercisesActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void setViewModel() {

        viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        viewModel.fetchExercises();

        viewModel.getPrograms().observe(this, new Observer<List<WorkoutProgram>>() {
            @Override
            public void onChanged(List<WorkoutProgram> workoutPrograms) {
                defaultPrograms = workoutPrograms;
                if (BaseActivity.currentMember.getPrograms() == null || BaseActivity.currentMember.getPrograms().isEmpty()) {
                    BaseActivity.currentMember.setPrograms(defaultPrograms);
                    adapter1.setItems(defaultPrograms);

                    if (customerViewModel == null) {
                        customerViewModel = new ViewModelProvider(FirstPageActivity.this).get(CustomerViewModel.class);
                    }
                    customerViewModel.update(BaseActivity.currentMember);

                }
                if (BaseActivity.currentMember.getPrograms() != null) {

                    setCustomerViewModel();
                }

            }
        });

    }


    private void setCustomerViewModel() {
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        customerViewModel.getAll();
        customerViewModel.getLiveDataCollection().observe(this, new Observer<Customers>() {
            @Override
            public void onChanged(Customers customers) {
                Log.d("DEBUG", "Received customers: " + customers.size());

                for (Customer cs : customers) {
                    if (cs.getIdFs().equals(BaseActivity.currentMember.getIdFs())) {

                        BaseActivity.currentMember = cs; // ← Refresh currentMember with updated data

                        List<WorkoutProgram> updatedPrograms = cs.getPrograms();

                        if (updatedPrograms != null && !updatedPrograms.isEmpty()) {
                            adapter1.setItems(updatedPrograms);  // Update adapter with real data
                        } else {
                            adapter1.setItems(new ArrayList<>());  // Prevent null issues
                        }
                        break;
                    }
                }
            }
        });
    }


    private void setRecyclerView() {

        adapter1 = new WorkoutProgramAdapter(null, R.layout.iteam_program,
                // Initialize ViewHolder - this runs only once per ViewHolder
                holder -> {

                    holder.putView("img", holder.itemView.findViewById(R.id.ivPic));

                    holder.putView("name", holder.itemView.findViewById(R.id.tvName));

                    holder.putView("count", holder.itemView.findViewById(R.id.tvExerciseCount));

                },
                ((holder, item, position) -> {
                    // Bind data to ViewHolder - this runs for every item
                    ((TextView) holder.getView("name")).setText(item.getTitle());

                    ((TextView) holder.getView("count"))
                            .setText("Exercises: " + (item.getExercises() != null ? item.getExercises().size() : 0));

                    ImageView thumb = holder.getView("img");
                    int resId;
                    switch (item.getType()) {               // enum Type in WorkoutProgram
                        case CORE:
                            resId = R.drawable.core_thumb;
                            break;
                        case BACK_CABLE:
                            resId = R.drawable.back_bi_thumb;
                            break;
                        case LEGS_BODYWEIGHT:
                            resId = R.drawable.leg_burn_thumb;
                            break;
                        case CHEST_MACHINE:
                            resId = R.drawable.chest_thumb;
                            break;

                        case ASSISTED_ABS:
                            resId = R.drawable.abs_thumb;
                            break;
                        case LATS_MACHINE:
                            resId = R.drawable.lats_thumb;
                            break;

                        case ARMS_BARBELL:
                            resId = R.drawable.barbelle;
                            break;
                        case FULL_BODY_BARBELL:
                            resId = R.drawable.full_body_thumb;
                            break;
                        default:
                            resId = R.drawable.cardio;
                            break;
                    }
                    thumb.setImageResource(resId);
                })
        );

        adapter1.setOnItemClickListener(new GenericAdapter.OnItemClickListener<WorkoutProgram>() {
            @Override
            public void onItemClick(WorkoutProgram item, int position) {
                Intent intent = new Intent(FirstPageActivity.this, WorkOutExerciseActivity.class);
                intent.putExtra("WORKOUT", item);
                startActivityForResult(intent, 1);


            }
        });


        rvDefultPlans.setAdapter(adapter1);
        rvDefultPlans.setLayoutManager(new LinearLayoutManager(this));

        // Configure swipe visuals
        SwipeConfig config = new SwipeConfig();
        config.leftBackgroundColor = Color.RED;
        config.leftText = "Delete";
        config.leftIconResId = R.drawable.trashcan; // Replace with your own trash icon

        SwipeCallback<WorkoutProgram> swipeCallback = new SwipeCallback<>(adapter1, this, config);

// Swipe actions
        adapter1.setOnItemSwipeListener(new GenericAdapter.OnItemSwipeListener<WorkoutProgram>() {
            @Override
            public void onItemSwipeRight(WorkoutProgram item, int position) {
                // Optional: no action or show edit/share/etc.
                adapter1.notifyItemChanged(position);
            }

            @Override
            public void onItemSwipeLeft(WorkoutProgram item, int position) {
                new MaterialAlertDialogBuilder(FirstPageActivity.this)
                        .setMessage("Delete \"" + item.getTitle() + "\"?")
                        .setIcon(R.drawable.trashcan)
                        .setCancelable(true)
                        .setTitle("Delete")
                        .setNegativeButton("No", (dialog, which) -> {
                            adapter1.notifyItemChanged(position); // Cancel
                        })
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Remove from current member and update
                            BaseActivity.currentMember.getPrograms().remove(position);
                            customerViewModel.update(BaseActivity.currentMember);

                            // Update adapter
                            adapter1.getItems().remove(position);
                            adapter1.notifyItemRemoved(position);
                        })
                        .show();
            }
        });

// Attach swipe handler to RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(rvDefultPlans);


    }

    @Override
    public void setValidation() {

    }

    @Override
    public boolean validate() {
        return false;
    }
}