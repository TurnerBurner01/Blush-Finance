package com.example.blushfinance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextNewPassword;
    private Button buttonResetPassword, buttonBackToLogin;
    private com.example.blushfinance.DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        db = new com.example.blushfinance.DatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        buttonResetPassword.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String newPassword = editTextNewPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(newPassword)) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!db.userExists(username)) {
                Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 8 || !newPassword.matches(".*[A-Z].*") || !newPassword.matches(".*\\d.*")) {
                Toast.makeText(this, "Password must be 8+ chars, include uppercase and number", Toast.LENGTH_LONG).show();
                return;
            }

            boolean updated = db.updatePassword(username, newPassword);
            if (updated) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}


