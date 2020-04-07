package com.example.habitreminder.habits;


import android.content.Context;
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

import com.example.habitreminder.Data.AddFrequencyData;
import com.example.habitreminder.Data.SubHabits;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.example.habitreminder.userhome.FragmentHome;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
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
    private String subHabit_name, mainHabits_name;
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

//        radio_beginner = root.findViewById(R.id.radio_beginner);
//        radio_intermediate = root.findViewById(R.id.radio_intermediate);
//        radio_advance = root.findViewById(R.id.radio_advance);

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
        SharedPreferences sharedPreferences_sub = this.getActivity().getSharedPreferences("Name",
                Context.MODE_PRIVATE);
        subHabit_name = sharedPreferences_sub.getString("name", "");
        heading_frequency.setText(subHabit_name);
        // main name of habits
        SharedPreferences sharedPreferences_Main = this.getActivity().getSharedPreferences("Name_main",
                Context.MODE_PRIVATE);
        mainHabits_name = sharedPreferences_Main.getString("name_main", "");

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
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId == 2131296596) {
                    Toast.makeText(getActivity(), "selected Intermediate", Toast.LENGTH_SHORT).show();
                    addNote_Intermediate();
                } else if (selectedId == 2131296595) {
                    Toast.makeText(getActivity(), "selected Beginner", Toast.LENGTH_SHORT).show();
                    addNote_Beginner();
                } else {
                    Toast.makeText(getActivity(), "selected Advance", Toast.LENGTH_SHORT).show();
                    addNote_Advance();
                }


            }

        });

    }


    private void addNote_Advance() {
        Map<String, Object> map = new HashMap<>();

        map.put("subHabitsName", subHabit_name);
        map.put("habitName", mainHabits_name);
        map.put("advance", "3");
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
