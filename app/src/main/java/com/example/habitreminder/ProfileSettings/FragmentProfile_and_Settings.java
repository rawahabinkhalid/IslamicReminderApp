package com.example.habitreminder.ProfileSettings;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.habitreminder.Data.Data;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile_and_Settings extends Fragment {
    private FirebaseAuth mAuth;
    private String user;
    FirebaseFirestore db;
    String uid;
    private TextView privacyButton;
    TextView userName;
    TextView userEmail;
    TextView change_profile_name;
    TextView upgradetopremium;
    String personName = "";
    String personEmail = "";
    String UID = "";
    Data dt;
    OnboardPreferenceManager oPm;
    private FrameLayout mMainFrame;
    Fragment_Change_name change_name;
    Fragment_upgrade_to_premium upgrade;


    public FragmentProfile_and_Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Rootview = null;
        Rootview = inflater.inflate(R.layout.fragment_profile_and_settings, container,false);
        userName = (TextView) Rootview.findViewById(R.id.user_name);
        userEmail = (TextView) Rootview.findViewById(R.id.user_email);
        change_profile_name = Rootview.findViewById(R.id.change_profile_name);
        upgradetopremium = Rootview.findViewById(R.id.upgrade_to_premium);
        mMainFrame = (FrameLayout) Rootview.findViewById(R.id.mainFrame);

        privacyButton = (TextView) Rootview.findViewById(R.id.tv_priacy_terms);
        change_name = new Fragment_Change_name();
        upgrade = new Fragment_upgrade_to_premium();

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(), "ok hai", Toast.LENGTH_SHORT).show();
                Privacy_Terms privacy_terms = new Privacy_Terms();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, privacy_terms, "privacy and terms")
                        .addToBackStack("privacy&terms")
                        .commit();

            }
        });


        // Inflate the layout for this fragment
        oPm = new OnboardPreferenceManager(getContext());
        dt = new Data();
        if (oPm.isGoogleSignIn()) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                personName = acct.getDisplayName();
                personEmail = acct.getEmail();
                UID = acct.getId();
                userName.setText(personName);
                userEmail.setText(personEmail);
            }
    } else if (oPm.isFacebookSignIn()) {
        userName.setText("Welcome,\n Facebook user");
    } else {
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
//            user = mAuth.getCurrentUser().getUid();
            personEmail = mAuth.getCurrentUser().getEmail();
            UID = mAuth.getCurrentUser().getUid();
            Log.i("UID",UID);
            DocumentReference docRef = db.collection("users").document(UID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Data","DocumentSnapshot data: " + document.get("name"));
                          personName = document.get("name").toString();
                            userName.setText(personName);
                            userEmail.setText(personEmail);

                        } else {
                            Log.d("Failed", "No such document");
                        }
                    } else {
                        Log.d("Exception", "get failed with ", task.getException());
                    }
                }
            });
//

            FirebaseUser user_firebase = FirebaseAuth.getInstance().getCurrentUser();
        }

            dt.setUID(UID);
            dt.setEmail(personEmail);
            dt.setName(personName);

        change_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfileName(change_name);
            }
        });
        upgradetopremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfileName(upgrade);
            }
        });

        return Rootview;
    }
    public void changeProfileName(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commit();
    }
    }


