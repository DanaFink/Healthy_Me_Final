package com.dana_f.tashtit.ACTIVITIES;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dana_f.helper.inputValidators.EntryValidation;
import com.dana_f.model.WorkoutProgram;
import com.dana_f.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.dana_f.tashtit.ADPTERS.ExericesesAdapter;
import com.dana_f.tashtit.R;
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

        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        rvExercises.setAdapter(adapter);


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