package com.zybooks.weighttrackingappui.ui.theme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.zybooks.weighttrackingappui.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        dbHelper = new DatabaseHelper(this);

        buttonRegister.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = dbHelper.registerUser(email, password);
                if (success) {
                    Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (dbHelper.loginUser(email, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, WeightActivity.class);
                startActivity(intent);
                finish(); // prevent going back to login screen
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
