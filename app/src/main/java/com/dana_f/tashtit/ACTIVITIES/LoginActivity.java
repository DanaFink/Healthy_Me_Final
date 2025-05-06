package com.dana_f.tashtit.ACTIVITIES;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dana_f.helper.inputValidators.EmailRule;
import com.dana_f.helper.inputValidators.EntryValidation;
import com.dana_f.helper.inputValidators.Rule;
import com.dana_f.helper.inputValidators.RuleOperation;
import com.dana_f.helper.inputValidators.Validator;
import com.dana_f.model.Customer;
import com.dana_f.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.dana_f.tashtit.R;
import com.dana_f.viewmodel.CustomerViewModel;

public class LoginActivity extends BaseActivity implements EntryValidation {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSave;
    private Button btnCancel;
    private CheckBox cbRememberMe;


    private CustomerViewModel viewModel;
    private SharedPreferences sharedPreferences;//shared prefernce used to store small user prefences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);


        initializeViews();
        setViewModel();
        setValidation();
        autoLoginIfRemembered();

    }

    @Override
    protected void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        setListeners();
    }

    @Override
    protected void setListeners() {
        btnSave.setOnClickListener(v -> {
            if (validate()) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                // Save credentials if "Remember Me" is checked
                if (cbRememberMe.isChecked()) {
                    sharedPreferences.edit()
                            .putString("userEmail", email)
                            .putString("userPassword", password)
                            .putBoolean("rememberMe", true)
                            .apply();
                } else {
                    sharedPreferences.edit().clear().apply();
                }

                viewModel.logIn(email, password);
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void autoLoginIfRemembered() {
        boolean remembered = sharedPreferences.getBoolean("rememberMe", false);
        if (remembered) {
            String savedEmail = sharedPreferences.getString("userEmail", "");
            String savedPassword = sharedPreferences.getString("userPassword", "");
            viewModel.logIn(savedEmail, savedPassword);
        }
    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        viewModel.getEntity().observe(this, new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                if (customer != null) {
                    BaseActivity.currentMember = customer;

                    if (!BaseActivity.currentMember.isBmiCompleted()) {
                        // Redirect to profile setup
                        startActivity(new Intent(LoginActivity.this, UserProfileActiviy.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, FirstPageActivity.class));
                    }

                }
            }
        });
    }

    @Override
    public void setValidation() {
        Validator.add(new Rule(etEmail, RuleOperation.REQUIRED, "Please enter email"));
        Validator.add(new EmailRule(etEmail, RuleOperation.TEXT, "Wrong email"));
        Validator.add(new Rule(etPassword, RuleOperation.REQUIRED, "Please enter password"));
    }


    @Override
    public boolean validate() {
        return Validator.validate();
    }
}