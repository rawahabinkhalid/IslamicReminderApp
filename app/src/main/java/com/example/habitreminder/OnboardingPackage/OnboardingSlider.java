package com.example.habitreminder.OnboardingPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.habitreminder.LoginSignup.LoginSignupActivity;
import com.example.habitreminder.R;
import com.example.habitreminder.userhome.UserDashboardActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class OnboardingSlider extends AppCompatActivity {

    private OnboardPreferenceManager prefManager;
    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;
        prefManager = new OnboardPreferenceManager(this);
        if (prefManager.isUserLogin()) {
            if(prefManager.isGoogleSignIn()){
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                if(account != null){
                    gotoHomeScreen();
                }
            }
            gotoHomeScreen();
        }else if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
        }
        setContentView(R.layout.activity_onboarding_slider);
        ViewPager onbordingSlider = findViewById(R.id.slideViewPager);
        SliderAdapter sliderAdapter = new SliderAdapter(this, OnboardingSlider.this);
        onbordingSlider.setAdapter(sliderAdapter);
        onbordingSlider.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(_context, LoginSignupActivity.class));
        finish();
    }

    private void gotoHomeScreen() {
        startActivity(new Intent(_context, UserDashboardActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) { }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }

        @Override
        public void onPageScrollStateChanged(int arg0) { }
    };
}
