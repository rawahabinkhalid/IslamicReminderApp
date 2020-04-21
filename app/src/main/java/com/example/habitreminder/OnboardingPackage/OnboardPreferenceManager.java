package com.example.habitreminder.OnboardingPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.habitreminder.MainActivity;
import com.example.habitreminder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class OnboardPreferenceManager {
    private SharedPreferences onboardingSharedPref;
    private SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "intro_slider-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_USER_LOGIN = "IsUserLogin";
    private static final String IS_GOOGLE_SIGN_IN = "IsGoogleSignIN";
    private static final String IS_FACEBOOK_SIGN_IN = "IsFacebookSignIN";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_LOGIN_TYPE = "user_login_type";

    private static final String USER_LOGIN_ID = "user_login_id";
    private static final String LOCATION_LATITUDE = "user_location_latitude";
    private static final String LOCATION_LONGITUDE = "user_location_longitude";
    private Context mContext;


    public OnboardPreferenceManager(Context context) {
        int PRIVATE_MODE = 0;
        mContext = context;
        onboardingSharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = onboardingSharedPref.edit();
    }

    public OnboardPreferenceManager() {
    }

    public void setUserData(String id, String name, String email, String userType) {
        editor.putString(USER_NAME, name);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_LOGIN_TYPE, userType);
        editor.putString(USER_LOGIN_ID, id);
        editor.commit();
    }

    void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setUserLogin(boolean userLogin) {
        editor.putBoolean(IS_USER_LOGIN, userLogin);
        editor.commit();
    }

    public boolean isUserLogin() {
        return onboardingSharedPref.getBoolean(IS_USER_LOGIN, false);
    }

    boolean isFirstTimeLaunch() {
        return onboardingSharedPref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setGoogleSignIn(boolean userLogin) {
        editor.putBoolean(IS_GOOGLE_SIGN_IN, userLogin);
        editor.commit();
    }

    public boolean isGoogleSignIn() {
        return onboardingSharedPref.getBoolean(IS_GOOGLE_SIGN_IN, false);
    }

    public void setFacebookSignIn(boolean userLogin) {
        editor.putBoolean(IS_FACEBOOK_SIGN_IN, userLogin);
        editor.commit();
    }

    public boolean isFacebookSignIn() {
        return onboardingSharedPref.getBoolean(IS_FACEBOOK_SIGN_IN, false);
    }

    public String getUserName() {
        return onboardingSharedPref.getString(USER_NAME, "user");
    }
}
