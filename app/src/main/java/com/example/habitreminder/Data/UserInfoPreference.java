package com.example.habitreminder.Data;

import android.content.SharedPreferences;

public class UserInfoPreference {
    private SharedPreferences userPreferences;
    private SharedPreferences.Editor editor;

    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_LOGIN_TYPE = "user_login_type";
    private static final String LOCATION_LATITUDE = "user_location_latitude";
    private static final String LOCATION_LONGITUDE = "user_location_longitude";

}
