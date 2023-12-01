package com.example.quiz_diadanh;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz_diadanh.widgets.NavigationDrawerController;

public class QuizAnswerActivity extends AppCompatActivity {

    TextView txtCreateRoom, txtJoin, test;
    NavigationDrawerController drawerController; // Khai báo controller cho Navigation Drawer

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_answer);

        txtCreateRoom = findViewById(R.id.txtCreateRoom);
        txtJoin = findViewById(R.id.txtJoin);
        test = findViewById(R.id.test);

        drawerController = new NavigationDrawerController(this);
        drawerController.setupDrawer(); // Thiết lập Drawer

        // Set up listeners or additional logic if needed
    }
}
