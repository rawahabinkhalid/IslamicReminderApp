package com.example.habitreminder.OnboardingPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.habitreminder.LoginSignup.LoginSignupActivity;
import com.example.habitreminder.R;
import com.example.habitreminder.userhome.UserDashboardActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class OnboardingSlider extends AppCompatActivity {

    private OnboardPreferenceManager prefManager;
    private Context _context;
    private FirebaseFirestore db;
    private static final String TAG = "OnboardingSlider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;
        prefManager = new OnboardPreferenceManager(this);

        if (prefManager.isUserLogin()) {
            if (prefManager.isGoogleSignIn()) {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                if (account != null) {
                    setFirebaseToken(account.getId());
                    gotoHomeScreen();
                }
            }
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if(mAuth == null) {
                launchHomeScreen();
            } else {
                String userID = mAuth.getCurrentUser().getUid();
                if (userID == null) {
                    launchHomeScreen();
                } else {
                    setFirebaseToken(userID);
                    gotoHomeScreen();
                }
            }
        } else if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
        }
        setContentView(R.layout.activity_onboarding_slider);
        ViewPager onbordingSlider = findViewById(R.id.slideViewPager);
        SliderAdapter sliderAdapter = new SliderAdapter(this, OnboardingSlider.this);
        onbordingSlider.setAdapter(sliderAdapter);
        onbordingSlider.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void setFirebaseToken(final String id) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                                Locale.getDefault());
                        Date currentLocalTime = calendar.getTime();
                        DateFormat date = new SimpleDateFormat("z", Locale.getDefault());
                        String localTime = date.format(currentLocalTime);
                        localTime = localTime.split("GMT")[1];

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        db = FirebaseFirestore.getInstance();
                        DocumentReference dbRef = db.collection("users").document(id);

                        Map<String, Object> map = new HashMap<>();

                        map.put("token", token);
                        map.put("GMT", localTime);

                        dbRef
                                .update(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });

                        // Log and toast
                        String msg = "InstanceID Token: " + token;
                        SharedPreferences companyId = ((FragmentActivity) _context).getSharedPreferences("Token",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = companyId.edit();
                        editor.putString("token", token);
                        editor.commit();
                        Log.d(TAG, msg);
                        Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
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
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
}
