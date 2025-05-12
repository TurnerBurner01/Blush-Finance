package com.example.blushfinance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.blushfinance.fragments.Finance_Page.FinanceFragment;
import com.example.blushfinance.fragments.Home_Page.HomeFragment;
import com.example.blushfinance.fragments.News_Page.NewsFragment;
import com.example.blushfinance.fragments.Profile_Page.ProfileFragment;
import com.example.blushfinance.fragments.Pots_Page.PotsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonCreate;
    private TextView textForgotPassword;
    private boolean isLoggedIn = false;
    private com.example.blushfinance.fragments.DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // login.xml layout

        // Initialize database and views
        db = new com.example.blushfinance.fragments.DatabaseHelper(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreate = findViewById(R.id.buttonCreateAccount);
        textForgotPassword = findViewById(R.id.textForgotPassword); // link to Forgot Password

        // Login button logic
        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkUser(username, password)) {
                // Saving username for home page
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("username", username)
                        .apply();

                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                isLoggedIn = true;
                showMainApp();
            } else {
                Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Create account button
        buttonCreate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.blushfinance.fragments.RegisterActivity.class);
            startActivity(intent);
        });

        // Forgot password link
        textForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.blushfinance.fragments.Profile_Page.ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    // Load the main app with bottom navigation
    private void showMainApp() {
        setContentView(R.layout.activity_main); // activity_main.xml layout

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_menu);
        bottomNav.setOnItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_home);

        if (isLoggedIn) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }
  
    // Bottom navigation fragment switcher using if-else
    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_finance) {
            selectedFragment = new FinanceFragment();
        } else if (itemId == R.id.nav_news) {
            selectedFragment = new NewsFragment();
        } else if (itemId == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        } else if (itemId == R.id.nav_pots) {
            selectedFragment = new PotsFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }

        return false;
    };
}
