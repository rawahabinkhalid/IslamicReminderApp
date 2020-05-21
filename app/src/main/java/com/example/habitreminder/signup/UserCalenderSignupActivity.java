package com.example.habitreminder.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.OnboardingPackage.OnboardingSlider;
import com.example.habitreminder.R;
import com.firebase.client.ServerValue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserCalenderSignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Map<String, Object> userData = new HashMap<>();
    Map<String, Object> calendarData = new HashMap<>();
    FirebaseFirestore db;
    private OnboardPreferenceManager prefManager;
    private Context _context;
    private String[] location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_calender_signup);
        _context = this;
        prefManager = new OnboardPreferenceManager(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        TextView goBack = findViewById(R.id.header_signup_back);
        ImageView googleCalendar = findViewById(R.id.google_calendar);
        Button createAccount = findViewById(R.id.next);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String email = intent.getStringExtra("email");
        final String password = intent.getStringExtra("password");
        String locationIntent = intent.getStringExtra("location");
        if(locationIntent != null ){
            location = locationIntent.split(",");
        }

        if (ContextCompat.checkSelfPermission(UserCalenderSignupActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }

        googleCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        requestPermission();
                        return;
                    }
                }
                Map data = readCalendarEvent(UserCalenderSignupActivity.this);
                calendarData.put("events", data);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> locationData = new HashMap<>();
                locationData.put("latitude", location[0]);
                locationData.put("longitude", location[1]);
                userData.put("name", name);
                userData.put("email", email);
                userData.put("user_location", locationData);
                userData.put("account_type", "APPUSER");
                userData.put("createdAt", FieldValue.serverTimestamp());
                userData.put("timestamp", ServerValue.TIMESTAMP);
                userData.put("Record1", 0);
                userData.put("Record2", 0);
                userData.put("Record3", 0);
                userData.put("Record4", 0);
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setTimestampsInSnapshotsEnabled(true).build();
                db.setFirestoreSettings(settings);
                createUserAccount(email, password);
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void createUserAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserData(user);
                    } else {
                        Toast.makeText(UserCalenderSignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void saveUserData(final FirebaseUser user){
        db.collection("users").document(user.getUid()).set(userData)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void v) {
                    prefManager.setUserData(user.getUid(), userData.get("name").toString(), userData.get("email").toString(), userData.get("account_type").toString());
                    if(calendarData.size() > 0){
                        saveCalendarEvents(user.getUid());
                        return;
                    }
                    redirectToHome();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) { }
            });
    }

    private void saveCalendarEvents(String user) {
        db.collection("calendar").document(user).set(calendarData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                redirectToHome();
            }
        });
    }

    private void redirectToHome(){
        prefManager.setUserLogin(true);
        startActivity(new Intent(_context, OnboardingSlider.class));
        finish();
    }

    public static Map<String, Object> readCalendarEvent(Context context) {
        ProgressDialog progressDialog;
        int progress = 8;

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading ... ");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(progress);
        progressDialog.show();

        Map<String, Object> calendarData = new HashMap<>();
        ArrayList<String> calendarID = new ArrayList<>();
        ArrayList<String> nameOfEvent = new ArrayList<>();
        ArrayList<String> startDates = new ArrayList<>();
        ArrayList<String> endDates = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.android.calendar/events"), new String[]{"calendar_id", "title", "description", "dtstart", "dtend", "eventLocation"}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        // fetching calendars name
        String[] CNames = cursor != null ? new String[cursor.getCount()] : new String[0];
        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        for (int i = 0; i < CNames.length; i++) {
            nameOfEvent.add(cursor.getString(1));
            descriptions.add(cursor.getString(2));
            startDates.add(getDate(Long.parseLong(cursor.getString(3))));
            endDates.add(getDate(Long.parseLong(cursor.getString(4))));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }
        calendarData.put("calendar_id", calendarID);
        calendarData.put("title", nameOfEvent);
        calendarData.put("description", descriptions);
        calendarData.put("start_date", startDates);
        calendarData.put("end_date", endDates);
        progressDialog.dismiss();

        return calendarData;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 1000) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPermission(){
        int locationRequestCode = 1000;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, locationRequestCode);
    }
}
