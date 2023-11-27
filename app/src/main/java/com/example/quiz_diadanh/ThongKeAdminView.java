package com.example.quiz_diadanh;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ThongkeAdminView extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button btnTab1, btnTab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        btnTab1 = findViewById(R.id.btnTab1);
        btnTab2 = findViewById(R.id.btnTab2);

        // Set up ViewPager with a custom adapter
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        // Attach ViewPager to TabLayout
        tabLayout.setupWithViewPager(viewPager);

        btnTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to the first tab
                viewPager.setCurrentItem(0);
            }
        });

        btnTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to the second tab
                viewPager.setCurrentItem(1);
            }
        });
    }

    // Custom FragmentPagerAdapter
    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // Return the appropriate Fragment for each tab
            switch (position) {
                case 0:
                    return new YourFragment1();
                case 1:
                    return new YourFragment2();
                // Add more cases if needed
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Return the total number of tabs
            return 2; // In this example, there are two tabs
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Return the title for each tab
            switch (position) {
                case 0:
                    return "Hoàn Thành";
                case 1:
                    return "Xem biểu đồ";
                // Add more titles if needed
                default:
                    return null;
            }
        }
    }
}

}
