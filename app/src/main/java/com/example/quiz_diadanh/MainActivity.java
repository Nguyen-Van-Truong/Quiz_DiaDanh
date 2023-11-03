package com.example.quiz_diadanh;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    Button btnCreateRoom, btnJoin;
    NavigationDrawerController drawerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đảm bảo đây là layout chứa các TextView bạn muốn truy cập

        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnJoin = findViewById(R.id.btnJoin);

        drawerController = new NavigationDrawerController(this);
        drawerController.setupDrawer();

        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code cho btnCreateRoom
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code cho btnJoin
            }
        });
    }
}
