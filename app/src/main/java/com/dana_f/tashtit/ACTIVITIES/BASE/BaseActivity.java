package com.dana_f.tashtit.ACTIVITIES.BASE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dana_f.model.Customer;
import com.dana_f.tashtit.ACTIVITIES.FirstPageActivity;
import com.dana_f.tashtit.ACTIVITIES.LogOutActiviy;
import com.dana_f.tashtit.ACTIVITIES.NutritionActivity;
import com.dana_f.tashtit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public abstract class BaseActivity extends AppCompatActivity {

    //public static Customer currentMember;

    public static Customer currentMember = null;
    //region Progress Dialog
    public ProgressDialog mProgressDialog;
    //region NAV_BAR
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setMenu();

    }

    protected abstract void initializeViews();

    protected abstract void setListeners();

    protected abstract void setViewModel();

    protected int getBottomNavgationMenuItemId() {
        return -1;
    }

    public void showProgressDialog(String title, @NonNull String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            if (title != null)
                mProgressDialog.setTitle(title);
            mProgressDialog.setIcon(R.mipmap.ic_launcher);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }
    //endregion

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    //was prive void

    private void setMenu() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    navigateToActivity(FirstPageActivity.class);
                } else if (itemId == R.id.nav_nutrition) {
                    navigateToActivity(NutritionActivity.class);
                } else if (itemId == R.id.nav_info) {
                    navigateToActivity(LogOutActiviy.class);
                } else {

                }
                return false;
            }
        });
    }

    protected void setSelectedNavigationItem(int itemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }

    private void navigateToActivity(Class<?> activityClass) {
        // Check if we're not already in the target activity
        if (activityClass == null) {
            Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
        } else {
            if (!this.getClass().equals(activityClass)) {
                Intent intent = new Intent(this, activityClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }
    }

    //endregion
}