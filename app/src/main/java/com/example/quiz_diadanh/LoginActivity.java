package com.example.quiz_diadanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.User;
import com.example.quiz_diadanh.model.UserPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private TextView loginButton, registerText, forgotPasswordText;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize UI Components
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        progressBar = findViewById(R.id.progressBar); // Initialize the ProgressBar

        loginButton.setOnClickListener(v -> loginUser());
        registerText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        forgotPasswordText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotActivity.class)));
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Email format validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Invalid email format");
            return;
        }

        progressBar.setVisibility(View.VISIBLE); // Show the progress bar

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    progressBar.setVisibility(View.GONE); // Hide the progress bar

                    if (task.isSuccessful()) {
                        FirebaseService firebaseService = new FirebaseService();
                        firebaseService.getUserByEmail(email, new FirebaseService.OnUserReceivedListener() {
                            @Override
                            public void onUserReceived(User user) {
                                saveUserToSharedPreferences(user);
                                // Navigate to MainActivity
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onError(Exception exception) {
                                Toast.makeText(LoginActivity.this, "onError:"+exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // If login fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToSharedPreferences(User user) {
        UserPreferences userPreferences = new UserPreferences(LoginActivity.this);
        userPreferences.saveUser(user);
        Toast.makeText(LoginActivity.this, "user: " + user, Toast.LENGTH_SHORT).show();

    }
}