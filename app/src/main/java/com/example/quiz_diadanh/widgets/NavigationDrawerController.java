package com.example.quiz_diadanh.widgets;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.quiz_diadanh.CodeRoomActivity;
import com.example.quiz_diadanh.R;
import com.example.quiz_diadanh.UserAccountActivity;
import com.example.quiz_diadanh.WaitingRoomActivity;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerController {

    private AppCompatActivity activity;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    public NavigationDrawerController(AppCompatActivity activity) {
        this.activity = activity;
        drawerLayout = activity.findViewById(R.id.drawer_layout);
        navigationView = activity.findViewById(R.id.nav_view);
    }

    public void setupDrawer() {
        setupToolbar();

        View headerView = navigationView.getHeaderView(0);

        // Handle for Create Room
        TextView txtCreateRoom = headerView.findViewById(R.id.txtCreateRoom);
        txtCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WaitingRoomActivity.class);
                activity.startActivity(intent);
                drawerLayout.closeDrawer(navigationView);
            }
        });

        // Handle for Join
        TextView txtJoin = headerView.findViewById(R.id.txtJoin);
        txtJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CodeRoomActivity.class);
                activity.startActivity(intent);
                drawerLayout.closeDrawer(navigationView);
            }
        });

        // Handle for Account
        TextView txtAccount = headerView.findViewById(R.id.txtAccount);
        txtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserAccountActivity.class);
                activity.startActivity(intent);
                drawerLayout.closeDrawer(navigationView);
            }
        });

        // Handle for Exit
        TextView txtExit = headerView.findViewById(R.id.txtExit);
        txtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("exit");
                drawerLayout.closeDrawer(navigationView);
            }
        });
    }


    private void setupToolbar() {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(activity.getResources().getColor(android.R.color.white));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}
