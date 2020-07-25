package com.example.findlostchildren.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findlostchildren.Fragments.HomeFragment;
import com.example.findlostchildren.Fragments.NotificationFragment;
import com.example.findlostchildren.Fragments.PersonalVictimFragment;
import com.example.findlostchildren.Fragments.ProfileFragment;
import com.example.findlostchildren.Fragments.SearchFragment;
import com.example.findlostchildren.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private Fragment fragment;
    private long backPressedTime;
    private TextView actionBarTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBarTV = findViewById(R.id.action_bar_TV);
        fragmentManager = getSupportFragmentManager();
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        actionBarTV.setText("Home");
        loadStartFragment();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        loadFragment(fragment, "home");
                        actionBarTV.setText("Home");
                        getSupportActionBar().setTitle("");
                        break;

                    case R.id.action_myVictims:
                        fragment = new PersonalVictimFragment();
                        loadFragment(fragment, "My Victims");
                        actionBarTV.setText("My Victims");
                        getSupportActionBar().setTitle("");
                        break;

                    case R.id.action_search:
                        fragment = new SearchFragment();
                        loadFragment(fragment, "search");
                        actionBarTV.setText("Search");
                        getSupportActionBar().setTitle("");
                        break;

                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        loadFragment(fragment, "profile");
                        actionBarTV.setText("Profile");
                        getSupportActionBar().setTitle("");
                        break;

                    case R.id.action_notification:
                        fragment = new NotificationFragment();
                        loadFragment(fragment, "notification");
                        actionBarTV.setText("Notification");
                        getSupportActionBar().setTitle("");
                        break;
                }
                return true;
            }
        });
    }

    private void loadStartFragment() {
        fragment = new HomeFragment();
        loadFragment(fragment, "home");
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(null);

        getSupportFragmentManager().popBackStack();
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (!fragment.getTag().equals("home")) {
            fragment = new HomeFragment();
            loadFragment(fragment, "home");
            actionBarTV.setText("Home");
            getSupportActionBar().setTitle("");
            bottomNavigationView.setSelectedItemId(R.id.action_home);


        } else {

            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finishAffinity();

            } else {
                Toast.makeText(this, "press again to exit ", Toast.LENGTH_SHORT).show();
            }
            backPressedTime = System.currentTimeMillis();
        }

    }
}
