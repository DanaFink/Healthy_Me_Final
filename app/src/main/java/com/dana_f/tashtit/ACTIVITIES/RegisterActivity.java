package com.dana_f.tashtit.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dana_f.helper.inputValidators.CompareRule;
import com.dana_f.helper.inputValidators.EmailRule;
import com.dana_f.helper.inputValidators.EntryValidation;
import com.dana_f.helper.inputValidators.NameRule;
import com.dana_f.helper.inputValidators.Rule;
import com.dana_f.helper.inputValidators.RuleOperation;
import com.dana_f.helper.inputValidators.Validator;
import com.dana_f.model.Customer;
import com.dana_f.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.dana_f.tashtit.R;
import com.dana_f.viewmodel.CustomerViewModel;

public class RegisterActivity extends BaseActivity implements EntryValidation {
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordCheck;
    private Button btnCancel;
    private Button btnSave;

    private CustomerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setViewModel();
        setValidation();
    }

    @Override
    protected void initializeViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordCheck = findViewById(R.id.etPasswordCheck);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        setListeners();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void setListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Customer customer = new Customer(etEmail.getText().toString(), etName.getText().toString(), etPassword.getText().toString());
                    viewModel.save(customer);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        viewModel.getSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(RegisterActivity.this, aBoolean ? "Success" : "Error", Toast.LENGTH_SHORT).show();
                if (aBoolean) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void setValidation() {
        Validator.add(new Rule(etName, RuleOperation.REQUIRED, "Please enter your name"));
        Validator.add(new NameRule(etName, RuleOperation.NAME, "Wrong name"));
        Validator.add(new Rule(etEmail, RuleOperation.REQUIRED, "Please enter email"));
        Validator.add(new EmailRule(etEmail, RuleOperation.TEXT, "Wrong email"));
        Validator.add(new Rule(etPassword, RuleOperation.REQUIRED, "Please enter password"));
        Validator.add(new Rule(etPasswordCheck, RuleOperation.REQUIRED, "Please enter Re Password"));
        Validator.add(new CompareRule(etPasswordCheck, etPassword, RuleOperation.COMPARE, "Password don't match"));
    }

    @Override
    public boolean validate() {
        return Validator.validate();
    }
}