package com.example.quiz_diadanh;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerController {

    private MainActivity mainActivity;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    public NavigationDrawerController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        drawerLayout = mainActivity.findViewById(R.id.drawer_layout);
        navigationView = mainActivity.findViewById(R.id.nav_view);
    }

    public void setupDrawer() {
        setupToolbar();

        View headerView = navigationView.getHeaderView(0);

        // Handle for Create Room
        TextView txtCreateRoom = headerView.findViewById(R.id.txtCreateRoom);
        txtCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("create room");
                drawerLayout.closeDrawer(navigationView);
            }
        });

        // Handle for Join
        TextView txtJoin = headerView.findViewById(R.id.txtJoin);
        txtJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("join");
                drawerLayout.closeDrawer(navigationView);
            }
        });

        // Handle for Account
        TextView txtAccount = headerView.findViewById(R.id.txtAccount);
        txtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("account");
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
        Toolbar toolbar = mainActivity.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(mainActivity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}
