package com.example.habitreminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.habitreminder.userhome.UserDashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    String habitKey;
    String UserID;
    String habitName;
    String title;
    String body;
    String Type;
    int Record1;
    int Record2;
    int Record3;
    int Record4;
    int ReminderDone;
    String Record1_Day;
    String ReminderDone_Day;
    //    int Record1_Temp;
//    int Record2_Temp;
//    int Record3_Temp;
//    int Record4_Temp;
    String ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MyFirebaseMsg", "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context _context = this;

        SharedPreferences data = _context.getSharedPreferences("NotificationData",
                Context.MODE_PRIVATE);
        habitKey = data.getString("habitKey", "");
        UserID = data.getString("UserID", "");
        habitName = data.getString("habitName", "");
        title = data.getString("title", "");
        body = data.getString("body", "");
        Type = data.getString("Type", "");
        RelativeLayout rl_main_activity = findViewById(R.id.rl_main_activity);
        ViewPager slideViewPager = findViewById(R.id.slideViewPager);
        slideViewPager.setVisibility(View.GONE);
        rl_main_activity.setVisibility(View.GONE);
        Log.i("MyFirebaseMsg", habitKey);
        Log.i("MyFirebaseMsg", UserID);
        Log.i("MyFirebaseMsg", habitName);
        Log.i("MyFirebaseMsg", title);
        Log.i("MyFirebaseMsg", body);
        Log.i("MyFirebaseMsg", Type);

        if (!body.equals("") || !habitKey.equals("")) {
            Log.i("MyFirebaseMsg", "in iff");
            rl_main_activity.setVisibility(View.VISIBLE);
            SharedPreferences companyId = _context.getSharedPreferences("NotificationData",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = companyId.edit();
            editor.putString("habitKey", "");
            editor.putString("UserID", "");
            editor.putString("habitName", "");
            editor.putString("title", "");
            editor.putString("body", "");
            editor.putString("Type", "");
            editor.commit();

//            final FragmentManager fm = ((FragmentActivity) _context).getSupportFragmentManager();
//
//            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//            LayoutInflater inflater = LayoutInflater.from(_context);
//            View view_popup = inflater.inflate(R.layout.notification_alert, null);
//            alertDialog.setView(view_popup);
            TextView title_tv = (TextView) findViewById(R.id.title_tv);
            TextView body_tv = (TextView) findViewById(R.id.body_tv);
            title_tv.setText(title);
//        body_tv.setText(respMsg);
            if (Type.equals("Habit")) {
                body_tv.setText("Did you perform '" + habitName + "' habit?");
                Button btn_yes = (Button) findViewById(R.id.btn_yes);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(_context, "Yes", Toast.LENGTH_LONG).show();
                        yesClicked();
                    }
                });

                Button btn_no = (Button) findViewById(R.id.btn_no);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(_context, "No", Toast.LENGTH_LONG).show();
                        noClicked();
                    }
                });

            } else {
                body_tv.setText("Did you perform '" + title + "' reminder?");
                Button btn_yes = (Button) findViewById(R.id.btn_yes);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(_context, "Yes", Toast.LENGTH_LONG).show();
                        yesClicked_Rem();
                    }
                });

                Button btn_no = (Button) findViewById(R.id.btn_no);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(_context, "No", Toast.LENGTH_LONG).show();
                        noClicked_Rem();
                    }
                });
            }

//            alertDialog.show();


//            Toast.makeText(this, "HELLO YOU ARE IN MAIN", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("MyFirebaseMsg", "in else");
            slideViewPager.setVisibility(View.VISIBLE);
        }
    }

    private void yesClicked() {
        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
        dbMain.collection("users").document(UserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                                LocalDateTime now = LocalDateTime.now();
                                ts = dtf.format(now);
                                Log.i("TagMainActivity", String.valueOf(ts));

                                DocumentSnapshot d = task.getResult();
                                Record1 = (d.get("Record1") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record1")));
                                Record2 = (d.get("Record2") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record2")));
                                Record3 = (d.get("Record3") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record3")));
                                Record4 = (d.get("Record4") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record4")));
                                Record1_Day = (d.get("Record1_Day") == null) ? String.valueOf(ts) : String.valueOf(d.get("Record1_Day"));
                                if (d.get("Record1_Day") == null || !Record1_Day.equals(String.valueOf(ts)))
                                    Record1++;
                                Record2++;
                                if (Record2 > Record3)
                                    Record3 = Record2;
                                Record4++;
                                updateData();
                            } else {
                                Log.d("TagMainActivity", "Error getting documents: ", task.getException());
                            }
                    }
                });
    }


    private void noClicked() {
        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
        dbMain.collection("users").document(UserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                                LocalDateTime now = LocalDateTime.now();
                                ts = dtf.format(now);
                                Log.i("TagMainActivity", String.valueOf(ts));

                                DocumentSnapshot d = task.getResult();
                                Record1 = (d.get("Record1") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record1")));
                                Record2 = (d.get("Record2") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record2")));
                                Record3 = (d.get("Record3") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record3")));
                                Record4 = (d.get("Record4") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record4")));
                                Record1_Day = (d.get("Record1_Day") == null) ? String.valueOf(ts) : String.valueOf(d.get("Record1_Day"));
                                Record2 = 0;
                                updateData();
                            } else {
                                Log.d("TagMainActivity", "Error getting documents: ", task.getException());
                            }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(UserID);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String timestamp = dtf.format(now);
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDateTime now1 = LocalDateTime.now();
        String timestamp1 = dtf1.format(now1);

        Map<String, Object> updates = new HashMap<>();
        updates.put("Record1", Record1);
        updates.put("Record2", Record2);
        updates.put("Record3", Record3);
        updates.put("Record4", Record4);
        updates.put("Record1_Day", ts);
        updates.put("Update_TimeStamp", timestamp + " at " + timestamp1);

        userRef
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TagMainActivity", "DocumentSnapshot successfully updated!");
                        startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TagMainActivity", "Error updating document", e);
                    }
                });

    }


    private void yesClicked_Rem() {
        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
        dbMain.collection("users").document(UserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                                LocalDateTime now = LocalDateTime.now();
                                ts = dtf.format(now);
                                Log.i("TagMainActivity", String.valueOf(ts));

                                DocumentSnapshot d = task.getResult();
                                ReminderDone = (d.get("ReminderDone") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("ReminderDone")));
                                ReminderDone_Day = (d.get("ReminderDone_Day") == null) ? String.valueOf(ts) : String.valueOf(d.get("ReminderDone_Day"));
                                ReminderDone++;
                                updateData_Rem();
                            } else {
                                Log.d("TagMainActivity", "Error getting documents: ", task.getException());
                            }
                    }
                });

    }


    private void noClicked_Rem() {
        startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateData_Rem() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(UserID);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String timestamp = dtf.format(now);
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDateTime now1 = LocalDateTime.now();
        String timestamp1 = dtf1.format(now1);

        Map<String, Object> updates = new HashMap<>();
        updates.put("ReminderDone", ReminderDone);
        updates.put("ReminderDone_Day", ReminderDone_Day);
        updates.put("Update_Rem_TimeStamp", timestamp + " at " + timestamp1);

        userRef
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TagMainActivity", "DocumentSnapshot successfully updated!");
                        startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TagMainActivity", "Error updating document", e);
                    }
                });

    }
}
