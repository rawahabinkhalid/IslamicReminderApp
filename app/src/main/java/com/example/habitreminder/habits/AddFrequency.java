package com.example.habitreminder.habits;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.habitreminder.Adapters.Main_Habits_Adapter;
import com.example.habitreminder.Data.AddFrequencyData;
import com.example.habitreminder.Data.SubHabits;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.example.habitreminder.userhome.FragmentHome;
import com.example.habitreminder.userhome.UserDashboardActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFrequency extends Fragment {
    private ImageButton backButton;
    private Button save;
    private TextView heading_frequency;
    private RadioButton radio_beginner, radio_intermediate, radio_advance;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID;
    private String subHabit_name, mainHabits_name, habit_key, subHabit_index, Body, Title, Token, Time, Interval, Sound, Type, Frequency;
    private String Frequency_Intermediate, Frequency_Advanced, Frequency_Beginner;
    private CollectionReference addHabits;
    private RadioGroup radioGroup;

    public AddFrequency() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_frequency, container, false);
        backButton = root.findViewById(R.id.backButton);
        save = root.findViewById(R.id.save);
        heading_frequency = root.findViewById(R.id.heading_frequency);
        radioGroup = (RadioGroup) root.findViewById(R.id.radio);
        radio_beginner = (RadioButton) root.findViewById(R.id.radio_beginner);
        radio_intermediate = (RadioButton) root.findViewById(R.id.radio_intermediate);
        radio_advance = (RadioButton) root.findViewById(R.id.radio_advance);

        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

//        radio_beginner = root.findViewById(R.id.radio_beginner);
//        radio_intermediate = root.findViewById(R.id.radio_intermediate);
//        radio_advance = root.findViewById(R.id.radio_advance);
        Title = "Habit Reminder Notification";
        Type = "Habit";
        Sound = "Enabled";
        Time = String.valueOf(ts);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subhabits_Fragment backButton = new Subhabits_Fragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, backButton, "healthFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        // Sub name of habits
//        SharedPreferences sharedPreferences_sub = this.getActivity().getSharedPreferences("Name",
//                Context.MODE_PRIVATE);
        // main name of habits
        SharedPreferences sharedPreferences_Main = this.getActivity().getSharedPreferences("Name_main",
                Context.MODE_PRIVATE);
        mainHabits_name = sharedPreferences_Main.getString("name_main", "");
        habit_key = sharedPreferences_Main.getString("key_main", "");
        subHabit_name = sharedPreferences_Main.getString("sub_name_main", "");
        subHabit_index = sharedPreferences_Main.getString("sub_index_main", "");
        Body = sharedPreferences_Main.getString("sub_notification_main", "");
        Frequency_Beginner = sharedPreferences_Main.getString("Frequency_Beginner", "");
        Frequency_Advanced = sharedPreferences_Main.getString("Frequency_Advanced", "");
        Frequency_Intermediate = sharedPreferences_Main.getString("Frequency_Intermediate", "");


        SharedPreferences companyId = this.getActivity().getSharedPreferences("Token",
                Context.MODE_PRIVATE);
        Token = companyId.getString("token", "");
        heading_frequency.setText(mainHabits_name);

        //current user ID
        OnboardPreferenceManager oPm = new OnboardPreferenceManager(getContext());

        if (oPm.isGoogleSignIn()) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                userID = acct.getId();
            }
        } else if (oPm.isFacebookSignIn()) {

        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            userID = mAuth.getCurrentUser().getUid();
        }
        addListenerOnButton();
        return root;
    }

    private void addListenerOnButton() {

        Frequency = "Beginner";
       // Interval = getEstimatedIntervalFromFrequency(Frequency_Beginner);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("selectedId _ In On", "on click");
                Log.i("selectedId _ Beginner", String.valueOf(radio_beginner.isChecked()));
                Log.i("selectedId _ Intermedi", String.valueOf(radio_intermediate.isChecked()));
                Log.i("selectedId _ Advanced", String.valueOf(radio_advance.isChecked()));
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                Log.i("selectedId", String.valueOf(selectedId));
//                if (selectedId == 2131296596) {
                if (radio_intermediate.isChecked()) {
                    Frequency = "Intermediate";
                  //  Interval = getEstimatedIntervalFromFrequency(Frequency_Intermediate);
//                } else if (selectedId == 2131296595) {
                } else if (radio_beginner.isChecked()) {
                    Frequency = "Beginner";
                   // Interval = getEstimatedIntervalFromFrequency(Frequency_Beginner);
                } else if (radio_advance.isChecked()) {
//                } else {
                    Frequency = "Advanced";
                   // Interval = getEstimatedIntervalFromFrequency(Frequency_Advanced);
                }
                if(radio_intermediate.isChecked() || radio_beginner.isChecked() || radio_advance.isChecked())
                    addNote();
            }
        });
    }

//    private void addListenerOnButton() {
//        Frequency = "Beginner";
//        Interval = getEstimatedIntervalFromFrequency(Frequency_Beginner);
//        save.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // get selected radio button from radioGroup
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                Log.i("selectedId", String.valueOf(selectedId));
////                String frequency = "";
//                if (selectedId == 2131296596) {
////                    Toast.makeText(getActivity(), "selected Intermediate", Toast.LENGTH_SHORT).show();
////                    addNote_Intermediate();
////                    frequency = "2";
//                    Frequency = "Intermediate";
//                    Interval = getEstimatedIntervalFromFrequency(Frequency_Intermediate);
//                } else if (selectedId == 2131296595) {
////                    Toast.makeText(getActivity(), "selected Beginner", Toast.LENGTH_SHORT).show();
////                    addNote_Beginner();
////                    frequency = "1";
//                    Frequency = "Beginner";
//                    Interval = getEstimatedIntervalFromFrequency(Frequency_Beginner);
//                } else {
////                    Toast.makeText(getActivity(), "selected Advance", Toast.LENGTH_SHORT).show();
////                    addNote_Advance();
////                    frequency = "3";
//                    Frequency = "Advanced";
//                    Interval = getEstimatedIntervalFromFrequency(Frequency_Advanced);
//                }
//                addNote();
//
//            }
//
//        });
//
//    }

//    private String getEstimatedIntervalFromFrequency(String Frequency) {
//        return Math.round(24 / Double.parseDouble(Frequency)) + " Hours";
//    }


    private void addNote() {
        Map<String, Object> map = new HashMap<>();

        map.put("subHabitsName", subHabit_name);
        map.put("habitName", mainHabits_name);
        map.put("frequency", Frequency);
//        AddFrequencyData addHabits = new AddFrequencyData();

        db.collection("users").document(userID).collection("AddHabits").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess : task was successful");
                setNotificationForHabit();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onSuccess : task was unsuccessful");
                    }
                });


    }

    private void setNotificationForHabit() {
        Map<String, Object> map = new HashMap<>();

        map.put("Body", Body);
        map.put("Frequency", Frequency);
        map.put("HabitKey", habit_key);
        map.put("Interval", Interval);
        map.put("Sound", Sound);
        map.put("SubHabit", subHabit_name);
        map.put("SubHabitIndex", subHabit_index);
        map.put("Time", Time);
        map.put("Title", Title);
        map.put("Token", Token);
        map.put("Type", Type);
        map.put("UserID", userID);
        map.put("Status", "Pending");
        db.collection("notifications").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess : task was successful");
                startActivity(new Intent(getContext(), UserDashboardActivity.class));
                ((FragmentActivity) getContext()).finish();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onSuccess : task was unsuccessful");
                    }
                });
    }

    private void addNote_Advance() {
        Map<String, Object> map = new HashMap<>();

        map.put("subHabitsName", subHabit_name);
        map.put("habitName", mainHabits_name);
        map.put("advance", "3");
//        AddFrequencyData addHabits = new AddFrequencyData();

        db.collection("users").document(userID).collection("AddHabits").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess : task was successful");


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onSuccess : task was unsuccessful");
                    }
                });


    }

    private void addNote_Beginner() {
        Map<String, Object> map = new HashMap<>();

        map.put("subHabitsName", subHabit_name);
        map.put("habitName", mainHabits_name);
        map.put("beginner", "1");
        db.collection("users").document(userID).collection("AddHibits").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess : task was successful");


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onSuccess : task was unsuccessful");
                    }
                });


    }

    private void addNote_Intermediate() {
        Map<String, Object> map = new HashMap<>();

        map.put("subHabitsName", subHabit_name);
        map.put("habitName", mainHabits_name);
        map.put("intermediate", "2");
//        AddFrequencyData addHabits = new AddFrequencyData();

        db.collection("users").document(userID).collection("AddHibits").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess : task was successful");


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onSuccess : task was unsuccessful");
                    }
                });


    }


}
