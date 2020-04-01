package com.example.habitreminder.OnboardingPackage;

import android.content.Context;
import android.content.SharedPreferences;

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
    private static final String LOCATION_LATITUDE = "user_location_latitude";
    private static final String LOCATION_LONGITUDE = "user_location_longitude";

    public OnboardPreferenceManager(Context context) {
        int PRIVATE_MODE = 0;
        onboardingSharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = onboardingSharedPref.edit();
    }

    public OnboardPreferenceManager() {}

    public void setUserData(String name, String email, String userType){
        editor.putString(USER_NAME, name);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_LOGIN_TYPE, userType);
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

    public String getUserName(){
        return onboardingSharedPref.getString(USER_NAME, "user");
    }
}
