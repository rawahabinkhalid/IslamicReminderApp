package com.example.habitreminder.userhome;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.habitreminder.Calender.FragmentCalender;
import com.example.habitreminder.Journal.WriteJournal;
import com.example.habitreminder.ProfileSettings.FragmentProfile_and_Settings;
import com.example.habitreminder.Progress.FragmentProgress;
import com.example.habitreminder.R;
import com.example.habitreminder.habits.AddCustomHabit;
import com.example.habitreminder.habits.HabitSelectionActivityFrag;
import com.example.habitreminder.habits.HealthHabitsFrag;
import com.example.habitreminder.habits.ScheduleHabit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserDashboardActivity extends AppCompatActivity implements
        AddHabitFragment.OnFragmentInteractionListener,
        HabitSelectionActivityFrag.OnFragmentInteractionListener,
        HealthHabitsFrag.OnFragmentInteractionListener,
        AddCustomHabit.OnFragmentInteractionListener,
        ScheduleHabit.OnFragmentInteractionListener,
        WriteJournal.OnFragmentInteractionListener {

    private FragmentHome homeFragment;
    private AddHabitFragment addHabitFragment;
    private FragmentCalender calenderFragment = new FragmentCalender();
    private FragmentProfile_and_Settings profile_and_settingsFragment;
    private FragmentProgress fragmentProgress;
    private int flag = 0;
    private int fragmentState = 1;
    private boolean doubleBackToExitPressedOnce = false;

    public UserDashboardActivity() throws GeneralSecurityException, IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FloatingActionButton addHabit = findViewById(R.id.fab);
        homeFragment = new FragmentHome();
//        calenderFragment = new FragmentCalender();
        addHabitFragment = new AddHabitFragment(this);
        profile_and_settingsFragment = new FragmentProfile_and_Settings();
        fragmentProgress = new FragmentProgress();
        setFragment(homeFragment);

        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    setFragment(addHabitFragment);
                    flag = 1;
                } else if (flag == 1) {
                    flag = 0;
                    if (fragmentState == 1) {
                        setFragment(homeFragment);
                    } else if (fragmentState == 2) {
                        setFragment(calenderFragment);
                    } else if (fragmentState == 4) {
                        setFragment(fragmentProgress);
                    } else if (fragmentState == 5) {
                        setFragment(profile_and_settingsFragment);
                    }
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentState = 1;
                    setFragment(homeFragment);
                    break;
                case R.id.navigation_calender:
                    fragmentState = 2;
                    setFragment(calenderFragment);
                    break;
                case R.id.navigation_barchart:
                    setFragment(fragmentProgress);
                    fragmentState = 4;
                    break;
                case R.id.navigation_profile:
                    setFragment(profile_and_settingsFragment);
                    fragmentState = 5;
                    break;
            }
            return false;
        }
    };

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.activity_slide_from_bottom, R.anim.activity_slide_from_top, R.anim.activity_slide_from_bottom, R.anim.activity_slide_from_top);
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1500);
    }
}
