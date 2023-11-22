package com.example.quiz_diadanh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextView txtCreateRoom, txtJoin, test, test2;
    NavigationDrawerController drawerController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("hahaahahaha");


        txtCreateRoom = findViewById(R.id.txtCreateRoom);
        txtJoin = findViewById(R.id.txtJoin);
        test = findViewById(R.id.test);
        test2 = findViewById(R.id.test2);

        test.setVisibility(View.GONE);
        test2.setVisibility(View.GONE);


        drawerController = new NavigationDrawerController(this);
        drawerController.setupDrawer();

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateQuizActivity.class);
                startActivity(intent);
            }
        });
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditQuizActivity.class);
                startActivity(intent);
            }
        });
        txtCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        txtJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }

    public static void setTextViewDrawableSize(TextView textView, int drawableResId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(textView.getContext(), drawableResId);
        if (drawable != null) {
            drawable.setBounds(0, 0, width, height);
        }
        textView.setCompoundDrawables(null, drawable, null, null);
    }

}
