package com.example.blushfinance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.blushfinance.fragments.Finance_Page.FinanceFragment;
import com.example.blushfinance.fragments.Home_Page.HomeFragment;
import com.example.blushfinance.fragments.News_Page.NewsFragment;
import com.example.blushfinance.fragments.Profile_Page.ProfileFragment;
import com.example.blushfinance.fragments.Pots_Page.PotsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_menu);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(navListener);

        // Load default fragment (Home)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.nav_finance) {
                    selectedFragment = new FinanceFragment();
                } else if (item.getItemId() == R.id.nav_news) {
                    selectedFragment = new NewsFragment();
                } else if (item.getItemId() == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                } else if (item.getItemId() == R.id.nav_pots) {
                    selectedFragment = new PotsFragment();
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }

                return false;
            };
}