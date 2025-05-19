package com.dana_f.tashtit.ACTIVITIES;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dana_f.helper.inputValidators.EntryValidation;
import com.dana_f.model.WorkoutProgram;
import com.dana_f.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.dana_f.tashtit.ADPTERS.ExericesesAdapter;
import com.dana_f.tashtit.R;
import com.dana_f.viewmodel.CustomerViewModel;
import com.dana_f.viewmodel.ExerciseViewModel;

public class WorkOutExerciseActivity extends BaseActivity implements EntryValidation {
    private TextView tvProgramName;

    private RecyclerView rvExercises;

    private ExericesesAdapter adapter;

    private ExerciseViewModel viewModel;

    private WorkoutProgram oldWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_work_out_exercise);
        getLayoutInflater().inflate(R.layout.activity_work_out_exercise, findViewById(R.id.main));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setRecyclerView();
    }

//    private void getExtras() {
//        if (getIntent().hasExtra("WORKOUT")) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                oldWorkout = getIntent().getSerializableExtra("WORKOUT", WorkoutProgram.class);
//
//                if (oldWorkout != null) {
//                    tvBodyPart.setText(oldWorkout.ge);
//                    etName.setText(oldMember.getName());
//                    etPhone.setText(oldMember.getPhone());
//                    etAddress.setText(oldMember.getAddress());
//                    spnCity.setSelection(findCityIndexByIdFs(oldMember.getCityId()));
//
//                    etEmail.setText(oldMember.getEmail());
//                    //etPassword.setText(oldMember.getPassword());
//                    //etRePassword.setText(oldMember.getPassword());
////                    if (oldMember.getPhoto() != null) {
////                        bitmapPhoto = BitMapHelper.decodeBase64(oldMember.getPhoto());
////                        ivMember.setImageBitmap(bitmapPhoto);
////                    }
//                }
//            }
//        }
//    }

    @Override
    protected void initializeViews() {
        tvProgramName = findViewById(R.id.tvProgramName);
        rvExercises = findViewById(R.id.rvExercises);
        oldWorkout = (WorkoutProgram) getIntent().getSerializableExtra("WORKOUT");
        tvProgramName.setText(oldWorkout.getTitle());


    }


    private void setRecyclerView() {
        ExericesesAdapter adapter = new ExericesesAdapter(
                oldWorkout.getExercises(), // List<Exercise>
                R.layout.iteam_exercise, // Your item layout
                (view) -> {
                    // Initialize ViewHolder here
                    // You usually don't need to do anything, unless you want to setup click listeners etc.
                },
                (holder, exercise, position) -> {
                    // Bind the Exercise data to the ViewHolder views here
                    TextView tvName = holder.itemView.findViewById(R.id.tvName);
                    TextView tvEquipment = holder.itemView.findViewById(R.id.tvEquipment);
                    TextView tvBodyPart = holder.itemView.findViewById(R.id.tvBodyPart);
                    TextView tvTarget = holder.itemView.findViewById(R.id.tvTarget);
                    TextView tvInstructions = holder.itemView.findViewById(R.id.tvInstructions);
                    ImageView imgGIF = holder.itemView.findViewById(R.id.imgGIF);

                    tvName.setText("name: " + exercise.getName());
                    tvEquipment.setText("equipment: " + exercise.getEquipment());
                    tvBodyPart.setText("body part: " + exercise.getBodyPart());
                    tvTarget.setText("target muscle: " + exercise.getTarget());
                    tvInstructions.setText(TextUtils.join("\n", exercise.getInstructions()));


                    Glide.with(imgGIF.getContext()).load(exercise.getGifUrl()).into(imgGIF);
                }
        );
        adapter.setOnItemLongClickListener((exercise, position) -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Remove Exercise")
                    .setMessage("Are you sure you want to remove this exercise?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        oldWorkout.getExercises().remove(position);
                        adapter.notifyItemRemoved(position);
                        saveUpdatedProgramToFirebase();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

            return true;
        });

        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        rvExercises.setAdapter(adapter);


    }
    private void saveUpdatedProgramToFirebase() {
        CustomerViewModel customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        // Match by title and type (or any other distinguishing attribute)
        for (int i = 0; i < BaseActivity.currentMember.getPrograms().size(); i++) {
            WorkoutProgram wp = BaseActivity.currentMember.getPrograms().get(i);

            if (wp.getTitle().equals(oldWorkout.getTitle()) && wp.getType() == oldWorkout.getType()) {
                BaseActivity.currentMember.getPrograms().set(i, oldWorkout);
                break;
            }
        }

        customerViewModel.update(BaseActivity.currentMember);
        Toast.makeText(this, "Exercise removed and saved", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void setListeners() {

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