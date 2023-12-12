package com.example.quiz_diadanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    TextView send;
    View backIcon;
    EditText emailInput;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        backIcon = findViewById(R.id.backIcon);
        send = findViewById(R.id.send);
        emailInput = findViewById(R.id.emailInput);
        auth = FirebaseAuth.getInstance();

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                    resetPassword(email);
                } else {
                    emailInput.setError("Enter your email");
                }
            }
        });
    }

    private void resetPassword(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Inform the user to check their email
                            Toast.makeText(ForgotActivity.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                            // Optionally, navigate to a new Activity (e.g., login screen)
                            startActivity(new Intent(ForgotActivity.this, LoginActivity.class));
                        } else {
                            // Handle error - user not found, etc.
                            Toast.makeText(ForgotActivity.this, "Failed to send reset email", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
