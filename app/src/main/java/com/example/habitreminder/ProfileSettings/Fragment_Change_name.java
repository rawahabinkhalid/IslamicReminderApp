package com.example.habitreminder.ProfileSettings;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Change_name extends Fragment {

    FirebaseFirestore db;
    EditText changeName;
    Button btn;
    private FirebaseAuth mAuth;
    String uid;
    String personName = "";
    String UID = "";
    OnboardPreferenceManager oPm;
    public Fragment_Change_name() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Rootview = null;
        Rootview = inflater.inflate(R.layout.fragment_change_name, container,false);
        changeName = Rootview.findViewById(R.id.changeName);
        btn =  Rootview.findViewById(R.id.changenamebtn);
        db = FirebaseFirestore.getInstance();
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//        getWindow().setLayout((int) (width*.8),(int) (height*.6));
        oPm = new OnboardPreferenceManager(getApplicationContext());
        if (oPm.isGoogleSignIn()) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                personName = acct.getDisplayName();
//                cPN.setUID(acct.getId());
                UID = acct.getId();

            }
        } else if (oPm.isFacebookSignIn()) {
        }else {
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
//            cPN.setUID(mAuth.getCurrentUser().getUid());
            UID = mAuth.getCurrentUser().getUid();
        }
        final Map<String, Object> data = new HashMap<>();
//        data.put("name", changeName);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference nodeRef = db.collection("users").document(UID);
                Log.i("UID", UID);
                nodeRef.update("name", changeName.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"Updated Note!",Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
        TextView back = Rootview.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("back clicked..", "yes");
                FragmentProfile_and_Settings profile_and_settings = new FragmentProfile_and_Settings();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, profile_and_settings, "Profile")
                        .addToBackStack("Profile")
                        .commit();
            }
        });
        // Inflate the layout for this fragment
        return Rootview;
    }

}
