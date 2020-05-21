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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    private RecyclerView explore_recycler_view;
    private RecyclerView setting_recycler_view;
    private List<Profile_Settings_Model> profileExploreList = new ArrayList<>();
    private List<Profile_Settings_Model> profileSettingList = new ArrayList<>();
    private RecyclerView.Adapter Profile_Settings_Adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager1;

    public FragmentProfile_and_Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Rootview = null;
        Rootview = inflater.inflate(R.layout.fragment_profile_and_settings, container, false);
        userName = (TextView) Rootview.findViewById(R.id.user_name);
        userEmail = (TextView) Rootview.findViewById(R.id.user_email);
        change_profile_name = Rootview.findViewById(R.id.change_profile_name);
        upgradetopremium = Rootview.findViewById(R.id.upgrade_to_premium);
        mMainFrame = (FrameLayout) Rootview.findViewById(R.id.mainFrame);

        explore_recycler_view = Rootview.findViewById(R.id.explore_recycler_view);
        explore_recycler_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        explore_recycler_view.setLayoutManager(layoutManager);

        setting_recycler_view = Rootview.findViewById(R.id.setting_recycler_view);
        setting_recycler_view.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(getContext());
        setting_recycler_view.setLayoutManager(layoutManager1);

        getProfileSettingsData();

        privacyButton = (TextView) Rootview.findViewById(R.id.tv_priacy_terms);
        change_name = new Fragment_Change_name();
        upgrade = new Fragment_upgrade_to_premium();

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            Log.i("UID", UID);
            DocumentReference docRef = db.collection("users").document(UID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Data", "DocumentSnapshot data: " + document.get("name"));
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

    public void changeProfileName(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }

    private void getProfileSettingsData() {
        getExploreData();
        getSettingData();
    }

    private void getExploreData() {
        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
        dbMain.collection("profile_settings").whereEqualTo("Type", "Explore")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String Description = String.valueOf(document.get("Description"));
                                    String Name = String.valueOf(document.get("Name"));
                                    Boolean Status = Boolean.valueOf(String.valueOf(document.get("Status")));
                                    String Type = String.valueOf(document.get("Type"));
                                    Profile_Settings_Model tempModel = new Profile_Settings_Model(Name, Description, Status, Type);
                                    profileExploreList.add(tempModel);

                                }
                                Profile_Settings_Adapter = new Profile_Settings_Adapter(getContext(), profileExploreList);
                                explore_recycler_view.setAdapter(Profile_Settings_Adapter);
                            } else {
                                Log.d("TagJournal", "Error getting documents: ", task.getException());
                            }
                    }
                });
    }

    private void getSettingData() {
        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
        dbMain.collection("profile_settings").whereEqualTo("Type", "Setting")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String Description = String.valueOf(document.get("Description"));
                                    String Name = String.valueOf(document.get("Name"));
                                    Boolean Status = Boolean.valueOf(String.valueOf(document.get("Status")));
                                    String Type = String.valueOf(document.get("Type"));
                                    Profile_Settings_Model tempModel = new Profile_Settings_Model(Name, Description, Status, Type);
                                    profileSettingList.add(tempModel);
                                }
                                Profile_Settings_Adapter = new Profile_Settings_Adapter(getContext(), profileSettingList);
                                setting_recycler_view.setAdapter(Profile_Settings_Adapter);
                            } else {
                                Log.d("TagJournal", "Error getting documents: ", task.getException());
                            }
                    }
                });
    }
}


