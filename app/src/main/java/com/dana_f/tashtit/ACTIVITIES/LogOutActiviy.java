package com.dana_f.tashtit.ACTIVITIES;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dana_f.helper.BitMapHelper;
import com.dana_f.helper.inputValidators.EntryValidation;
import com.dana_f.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.dana_f.tashtit.R;
import com.dana_f.viewmodel.CustomerViewModel;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class LogOutActiviy extends BaseActivity implements EntryValidation {
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 200;
    private CircleImageView profileImage;
    private TextView tvName, tvEmail;
    private Button btnChanges;
    private ImageView btnLogOut;
    private Uri imageUri;
    private Bitmap photo;
    private CustomerViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_log_out_activiy, findViewById(R.id.main));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setViewModel();
        setListeners();

    }

    protected int getBottomNavgationMenuItemId() {
        return R.id.nav_info;
    }

    @Override
    protected void initializeViews() {
        profileImage = findViewById(R.id.profileImage);

        tvName = findViewById(R.id.tvName);
        tvName.setText("Name: " + BaseActivity.currentMember.getName());


        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText("Email: " + BaseActivity.currentMember.getEMAIL());


        btnChanges = findViewById(R.id.btnChanges);
        btnLogOut = findViewById(R.id.btnLogOut);
    }

    @Override
    protected void setListeners() {
        btnChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogOutActiviy.this, UserProfileActiviy.class));
                finish();

            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit().clear().apply();

                // Clear memory copy
                BaseActivity.currentMember = null;

                // Go to login screen
                Intent intent = new Intent(LogOutActiviy.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showImagePickerDialog() {
        String[] options = {"Camera", "Gallery"};
        new AlertDialog.Builder(this)
                .setTitle("Select Image Source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) openCamera();
                    else openGallery();
                })
                .show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profileImage.setImageBitmap(photo);
                uploadImageFromBitmap(photo);
            } else if (requestCode == REQUEST_GALLERY && data != null) {
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);
                uploadImageFromUri(imageUri);
            }
        }
    }

    private void uploadImageFromBitmap(Bitmap bitmap) {
        BaseActivity.currentMember.setImg(BitMapHelper.encodeTobase64(bitmap));
        viewModel.save(BaseActivity.currentMember);
    }


    private void uploadImageFromUri(Uri uri) {
        try {
            // Get Bitmap from URI
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            // Optionally resize or compress the bitmap if needed here

            // Encode and save
            BaseActivity.currentMember.setImg(BitMapHelper.encodeTobase64(bitmap));
            viewModel.save(BaseActivity.currentMember);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image from gallery", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
    }


    @Override
    public void setValidation() {

    }

    @Override
    public boolean validate() {
        return false;
    }
}