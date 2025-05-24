package com.dana_f.tashtit.ACTIVITIES;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dana_f.helper.BitMapHelper;
import com.dana_f.helper.inputValidators.EntryValidation;
import com.dana_f.model.Customer;
import com.dana_f.model.Exercise;
import com.dana_f.model.WorkoutProgram;
import com.dana_f.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.dana_f.tashtit.ADPTERS.BASE.GenericAdapter;
import com.dana_f.tashtit.ADPTERS.ExericesesAdapter;
import com.dana_f.tashtit.R;
import com.dana_f.viewmodel.CustomerViewModel;
import com.dana_f.viewmodel.ExerciseViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddExercisesActivity extends BaseActivity implements EntryValidation {
    private Spinner spinnerBodyPart;
    private SearchView searchView;
    private CheckBox checkBox;
    private RecyclerView rvExercises;

    private ExerciseViewModel viewModel;
    private ExericesesAdapter adapter;
    private CustomerViewModel customerViewModel;
    private List<Exercise> exercises1;
    private Bitmap selectedProgramImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_add_exercises);
        getLayoutInflater().inflate(R.layout.activity_add_exercises, findViewById(R.id.main));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();

        setViewModel();

        setRecyclerView();

        setListeners();


    }

    @Override
    protected void initializeViews() {
        spinnerBodyPart = findViewById(R.id.spinnerBodyPart);

        searchView = findViewById(R.id.searchView);

        checkBox = findViewById(R.id.checkBox);
        rvExercises = findViewById(R.id.rvExercises);

    }


    @Override
    protected void setListeners() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.filterByName(newText);
                return true;
            }
        });
        spinnerBodyPart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedBodyPart = parentView.getItemAtPosition(position).toString();

                if (position == 0) { // "All" option
                    checkBox.setChecked(false);    // Reset checkbox
                    viewModel.fetchExercises();
                    viewModel.getExercises().observe(AddExercisesActivity.this, new Observer<List<Exercise>>() {
                        @Override
                        public void onChanged(List<Exercise> exercises) {
                            adapter.setItems(exercises);
                            exercises1=exercises;
                        }
                    });
                } else {
                    viewModel.filterByBodyPartAndEquipment(selectedBodyPart, checkBox.isChecked());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                viewModel.getExercises();
            }
        });

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Object selectedItem = spinnerBodyPart.getSelectedItem();
            String selectedBodyPart = (selectedItem != null) ? selectedItem.toString() : "";

            if (selectedBodyPart.contains("All")) {
                if (isChecked) {
                    // Filter only exercises that use equipment (exclude bodyweight)
                    viewModel.filterByEquipmentOnly();
                } else {
                    // Reset to show everything
                    viewModel.fetchExercises();
                }
            } else {
                viewModel.filterByBodyPartAndEquipment(selectedBodyPart, isChecked);
            }
        });



    }



    private void showExerciseOptionsDialog(Exercise exercise) {
        new androidx.appcompat.app.AlertDialog.Builder(AddExercisesActivity.this)
                .setTitle("Add Exercise to Program")
                .setMessage("Would you like to add this exercise to a new program or to an existing one?")
                .setPositiveButton("New Program", (dialog, which) -> {
                    createNewProgramWithExercise(exercise);
                })
                .setNegativeButton("Existing Program", (dialog, which) -> {
                    chooseExistingProgram(exercise);
                })
                .setNeutralButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
    private void showImagePickerDialog(Consumer<Bitmap> onImageSelected) {
        String[] options = {"Camera", "Gallery"};
        new AlertDialog.Builder(this)
                .setTitle("Select Image Source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_GALLERY);
                    }
                    this.onImagePickedCallback = onImageSelected;
                })
                .show();
    }
    private static final int REQUEST_CAMERA = 1001;
    private static final int REQUEST_GALLERY = 1002;

    private Consumer<Bitmap> onImagePickedCallback;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && onImagePickedCallback != null) {
            Bitmap bitmap = null;

            try {
                if (requestCode == REQUEST_CAMERA && data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                } else if (requestCode == REQUEST_GALLERY && data != null) {
                    Uri imageUri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                }

                if (bitmap != null) {
                    onImagePickedCallback.accept(bitmap);
                    onImagePickedCallback = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createNewProgramWithExercise(Exercise exercise) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("New Program Name");

//        // Set up the input
//        final EditText input = new EditText(this);
//        input.setHint("Enter program name");
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);


        // Create layout for input + image selection
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText input = new EditText(this);
        input.setHint("Enter program name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(input);

        ImageView imagePreview = new ImageView(this);
        imagePreview.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                500
        ));
        imagePreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        layout.addView(imagePreview);

        Button chooseImageBtn = new Button(this);
        chooseImageBtn.setText("Choose Image");
        layout.addView(chooseImageBtn);

        builder.setView(layout);

        chooseImageBtn.setOnClickListener(v -> {
            showImagePickerDialog(image -> {
                selectedProgramImage = image;
                imagePreview.setImageBitmap(image); // show preview
            });
        });

        // Set up the buttons
        builder.setPositiveButton("Create", (dialog, which) -> {
            String programName = input.getText().toString().trim();

            if (!programName.isEmpty()) {


                List<Exercise> exercises = new ArrayList<>();
                exercises.add(exercise);

                WorkoutProgram newProgram = new WorkoutProgram(WorkoutProgram.Type.CUSTOM, programName, exercises);
                if (selectedProgramImage != null) {
                    Bitmap resized = BitMapHelper.resizeBitmap(selectedProgramImage, 512);
                    newProgram.setImgBase64(BitMapHelper.encodeTobase64(resized));
                }

                // --- Update Customer ---
                if (BaseActivity.currentMember.getPrograms() == null) {
                    BaseActivity.currentMember.setPrograms(new ArrayList<>());
                }
                BaseActivity.currentMember.getPrograms().add(newProgram);

                // --- Save Updated Customer to Firestore ---
                customerViewModel = new CustomerViewModel(getApplication());
                customerViewModel.update(BaseActivity.currentMember);
                customerViewModel.updateCustomerAndRefresh();

                showToast("Program '" + programName + "' created with " + exercise.getName() + "!");
            } else {
                showToast("Program name cannot be empty!");
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void chooseExistingProgram(Exercise exercise) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = BaseActivity.currentMember.getIdFs(); // Adjust if your ID is stored differently

        db.collection("Customers")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Customer customer = documentSnapshot.toObject(Customer.class);
                        List<WorkoutProgram> existingPrograms = customer.getPrograms();

                        if (existingPrograms == null || existingPrograms.isEmpty()) {
                            showToast("No existing programs. Please create a new one.");
                            return;
                        }

                        String[] programNames = new String[existingPrograms.size()];
                        for (int i = 0; i < existingPrograms.size(); i++) {
                            programNames[i] = existingPrograms.get(i).getTitle();
                        }

                        new androidx.appcompat.app.AlertDialog.Builder(this)
                                .setTitle("Select a Program")
                                .setItems(programNames, (dialog, which) -> {
                                    WorkoutProgram selectedProgram = existingPrograms.get(which);

                                    for (Exercise ex : selectedProgram.getExercises()) {
                                        if (exercise.getId().equals(ex.getId())) {
                                            showToast("This exercise already exists in this program.");
                                            return;
                                        }
                                    }


                                    selectedProgram.getExercises().add(exercise);

                                    showToast(exercise.getName() + " added to " + selectedProgram.getTitle());

                                    // Ensure the local member is updated for future use
                                    BaseActivity.currentMember.setPrograms(existingPrograms);

                                    // Save updated member back to Firestore
                                    customerViewModel = new CustomerViewModel(getApplication());
                                    customerViewModel.update(BaseActivity.currentMember);
                                    customerViewModel.updateCustomerAndRefresh();

                                    showToast("Customer's program updated successfully!");
                                })
                                .show();
                    } else {
                        showToast("No customer data found.");
                    }
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to load programs: " + e.getMessage());
                });
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void setRecyclerView() {

        adapter = new ExericesesAdapter(
                null, // List<Exercise>
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
        adapter.setOnItemLongClickListener(new GenericAdapter.OnItemLongClickListener<Exercise>() {
            @Override
            public boolean onItemLongClick(Exercise item, int position) {
                showExerciseOptionsDialog(item);
                return true;
            }
        });

        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        rvExercises.setAdapter(adapter);

    }


//    protected void testViewModel() {
//        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
//        customerViewModel.getAll();
//        customerViewModel.getLiveDataCollection().observe(this, new Observer<Customers>() {
//            @Override
//            public void onChanged(Customers customers) {
//                for (Customer cs : customers) {
//                    System.out.println(cs.getPrograms());
//                }
//            }
//        });
//
//    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        viewModel.fetchExercises();

        viewModel.getExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                adapter.setItems(exercises);
            }
        });

        viewModel.fetchBodyParts();
        viewModel.getBodyParts().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> bodyParts) {

                List<String> updatedBodyParts = new ArrayList<>();
                updatedBodyParts.add("All (reset all the exercises)");  // top
                updatedBodyParts.addAll(bodyParts);

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                        AddExercisesActivity.this,
                        android.R.layout.simple_spinner_item,
                        updatedBodyParts
                );
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBodyPart.setAdapter(spinnerAdapter);
            }
        });
        viewModel.getPrograms().observe(this, new Observer<List<WorkoutProgram>>() {
            @Override
            public void onChanged(List<WorkoutProgram> workoutPrograms) {

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