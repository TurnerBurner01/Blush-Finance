package com.example.blushfinance;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private ImageView showPasswordToggle, showConfirmPasswordToggle;
    private Button registerButton, backToLoginButton;
    private com.example.blushfinance.DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // ✅ correct layout reference

        // Initialize views
        db = new com.example.blushfinance.DatabaseHelper(this);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.btnRegister);
        backToLoginButton = findViewById(R.id.btnBackToLogin); // New button
        showPasswordToggle = findViewById(R.id.showPasswordToggle);
        showConfirmPasswordToggle = findViewById(R.id.showConfirmPasswordToggle);

        // Toggle visibility for password
        showPasswordToggle.setOnClickListener(view -> {
            togglePasswordVisibility(passwordEditText, showPasswordToggle);
        });

        // Toggle visibility for confirm password
        showConfirmPasswordToggle.setOnClickListener(view -> {
            togglePasswordVisibility(confirmPasswordEditText, showConfirmPasswordToggle);
        });

        // Register button logic
        registerButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.length() < 6) {
                Toast.makeText(this, "Username must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(password)) {
                Toast.makeText(this, "Password must be at least 8 characters, include 1 uppercase letter and 1 number", Toast.LENGTH_LONG).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.userExists(username)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                boolean created = db.insertUser(username, password);
                if (created) {
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back to Login button logic
        backToLoginButton.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }

    // Toggle password visibility logic
    private void togglePasswordVisibility(EditText passwordField, ImageView toggleIcon) {
        if (passwordField.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_eye_off);
        } else {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_eye);
        }
        passwordField.setSelection(passwordField.getText().length());
    }

    // Password validation
    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&     // at least one uppercase letter
                password.matches(".*\\d.*");         // at least one digit
    }
}


