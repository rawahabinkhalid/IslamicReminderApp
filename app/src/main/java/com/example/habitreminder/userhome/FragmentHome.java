package com.example.habitreminder.userhome;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitreminder.Adapters.HabitsAdapter;
import com.example.habitreminder.Adapters.JournalAdapterHome;
import com.example.habitreminder.Data.HabitsData;
import com.example.habitreminder.Data.JournalData;
import com.example.habitreminder.Journal.WriteJournal;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.example.habitreminder.habits.AddCustomHabit;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment  {
    private String user;
    String uid;
    private Button addJournal, addReminder;
    private TextView welcomeuser;
    private String personName = "";
    String UID;
    private RecyclerView myJournal_RV;
    private RecyclerView.LayoutManager layoutManager;

    public List<JournalData> journalDataList;
    public List<HabitsData> habitsDataList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseUser CurrentUser = mAuth.getCurrentUser();

   public String userID;

    public JournalAdapterHome desAdapters;
    public HabitsAdapter habitsAdapter;
    private RecyclerView myHabits_RV;



    public FragmentHome(){

    }

    Activity activity;
    public FragmentHome(Activity activity){
        this.activity =activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        OnboardPreferenceManager oPms = new OnboardPreferenceManager(getContext());

        if (oPms.isGoogleSignIn()) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                userID = acct.getId();
                Log.d("isss",userID);
            }
        } else if (oPms.isFacebookSignIn()) {

        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            cPN.setUID(mAuth.getCurrentUser().getUid());
            userID = mAuth.getCurrentUser().getUid();
        }

        final FragmentManager fragmentManager;

        OnboardPreferenceManager oPm = new OnboardPreferenceManager(getContext());
        final String userName = oPm.getUserName();
        //Toast.makeText(getContext(), userName.toString(), Toast.LENGTH_SHORT).show();
        View rootview = null;
        rootview = inflater.inflate(R.layout.fragment_home, container,false);
        // journal data
        journalDataList = new ArrayList<>();
        myJournal_RV = (RecyclerView) rootview.findViewById(R.id.home_journalData);
        LinearLayoutManager ll = new LinearLayoutManager(activity);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        ll.setReverseLayout(true);
        myJournal_RV.setLayoutManager(ll);


         FirebaseFirestore db = FirebaseFirestore.getInstance();
         CollectionReference addJournalRef = db.collection("users");


        addJournalRef.document(userID).collection("AddJournal").orderBy("timestamp" , Query.Direction.DESCENDING).limit(2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.d("data1", queryDocumentSnapshots.toString());
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    Log.d(" list1", "" + list);
                    int counter = 0;
                    for (DocumentSnapshot d : list) {
                        //Log.d("data" + counter++, d.toObject(JournalData.class).getjDescription());
                        JournalData p = d.toObject(JournalData.class);
                        journalDataList.add(p);

                    }
                } else {
                    Log.d("data2", queryDocumentSnapshots.toString());
                }

                desAdapters = new JournalAdapterHome(getActivity(), journalDataList);

                myJournal_RV.setAdapter(desAdapters);
                desAdapters.notifyDataSetChanged();
            }
        });


// load data reminder
        habitsDataList = new ArrayList<>();
        myHabits_RV = (RecyclerView) rootview.findViewById(R.id.rv_habits);
        LinearLayoutManager ll1 = new LinearLayoutManager(activity);
        ll1.setOrientation(LinearLayoutManager.VERTICAL);
        myHabits_RV.setLayoutManager(ll1);


        CollectionReference addHabits = db.collection("users");


     addHabits.document(userID).collection("AddHabits").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.d("data1", queryDocumentSnapshots.toString());
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    Log.d(" list1", "" + list);
                    int counter = 0;
                    for (DocumentSnapshot d : list) {

                        HabitsData p = d.toObject(HabitsData.class);
                        habitsDataList.add(p);

                    }
                } else {
                    Log.d("data2", queryDocumentSnapshots.toString());
                }

                habitsAdapter = new HabitsAdapter(getActivity(), habitsDataList);

                myHabits_RV.setAdapter(habitsAdapter);
                habitsAdapter.notifyDataSetChanged();
            }
        });
        //add journal button
       addJournal = (Button) rootview.findViewById(R.id.journal_button);
        addJournal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WriteJournal writeJournal = new WriteJournal(getActivity());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, writeJournal, "writeJournal")
                        .addToBackStack("writeJournal")
                        .commit();
            }
        });
        // add reminder button
        addReminder = (Button) rootview.findViewById(R.id.reminder_button);
        addReminder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddCustomHabit addHabit = new AddCustomHabit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, addHabit, "addHabit")
                        .addToBackStack("addHabit")
                        .commit();
            }
        });

        welcomeuser = rootview.findViewById(R.id.welcomeUser);
        if (oPm.isGoogleSignIn()) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                personName = acct.getDisplayName();
                welcomeuser.setText("Welcome,\n"+personName);
//                cPN.setUID(acct.getId());
            }
        } else if (oPm.isFacebookSignIn()) {
            welcomeuser.setText("Welcome,\n Facebook user");
        } else {
            //FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            cPN.setUID(mAuth.getCurrentUser().getUid());
            UID = mAuth.getCurrentUser().getUid();
            DocumentReference docRef = db.collection("users").document(UID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Data","DocumentSnapshot data: " + document.get("timestamp"));
                            personName = document.get("name").toString();
                            welcomeuser.setText("Welcome,\n"+userName);
                        } else {
                            Log.d("Failed", "No such document");
                        }
                    } else {
                        Log.d("Exception", "get failed with ", task.getException());
                    }
                }
            });

        }

        return rootview;
    }

}
