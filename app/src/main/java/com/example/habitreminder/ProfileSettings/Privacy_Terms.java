package com.example.habitreminder.ProfileSettings;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habitreminder.Adapters.JournalAdapter;
import com.example.habitreminder.Data.JournalData;
import com.example.habitreminder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Privacy_Terms extends Fragment {

   public TextView privacyTerm ,privacyTerm1,privacyTerm2,privacyTerm3,privacyTerm4;
private FirebaseFirestore myFirebase;



    public Privacy_Terms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_privacy__terms, container, false);
        privacyTerm = view.findViewById(R.id.tv_priacy_terms);
        privacyTerm1 = view.findViewById(R.id.tv_priacy_terms1);
        privacyTerm2= view.findViewById(R.id.tv_priacy_terms2);
        privacyTerm3= view.findViewById(R.id.tv_priacy_terms3);
        privacyTerm4= view.findViewById(R.id.tv_priacy_terms4);

        myFirebase = FirebaseFirestore.getInstance();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference addJournalRef = db.collection("Privacy & terms");


        addJournalRef.document("0yRUY7RxMv3qUJg3JDMa").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists() && documentSnapshot != null) {
                        String privacy = documentSnapshot.getString("PrivacyTerms");
                        String privacy1 = documentSnapshot.getString("PrivacyTerms1");
                        String privacy2 = documentSnapshot.getString("PrivacyTerms2");
                        String privacy3 = documentSnapshot.getString("PrivacyTerms3");
                        String privacy4 = documentSnapshot.getString("PrivacyTerms4");
                       // Log.d("data is ", privacy);
                        privacyTerm.setText("Privacy Terms" + privacy);
                        privacyTerm1.setText("Privacy Terms" + privacy1);
                        privacyTerm2.setText("Privacy Terms" + privacy2);
                        privacyTerm3.setText("Privacy Terms" + privacy3);
                        privacyTerm4.setText("Privacy Terms" + privacy4);

                    }
                }
                else {
                    Log.d("privacy","ERROR"+ task.getException().getMessage());
                    Log.d("privacy1","ERROR"+ task.getException().getMessage());
                    Log.d("privacy2","ERROR"+ task.getException().getMessage());
                }
            }
        });


        return view;
    }

}
