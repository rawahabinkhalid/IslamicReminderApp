package com.example.habitreminder.Journal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitreminder.Adapters.JournalAdapter;
import com.example.habitreminder.Data.JournalData;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.example.habitreminder.userhome.FragmentHome;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WriteJournal extends Fragment implements View.OnClickListener {

    public WriteJournal() {

    }

    Activity activity;

    public WriteJournal(Activity activity) {
        this.activity = activity;
    }

    private Button button;
    private OnFragmentInteractionListener mListener;
    private RecyclerView myJournal_RV;

    private EditText jDescription;
    private CollectionReference addJournalRefs;
    public List<JournalData> journalDataList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseUser CurrentUser = mAuth.getCurrentUser();

    private String userID;
    private  ImageButton backButton;
    private JournalAdapter desAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_journal, container, false);

        myJournal_RV = (RecyclerView) view.findViewById(R.id.write_journal_rv);
        LinearLayoutManager ll = new LinearLayoutManager(activity);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        myJournal_RV.setLayoutManager(ll);
        journalDataList = new ArrayList<>();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference addJournalRef = db.collection("users/");


        addJournalRef.document(userID).collection("AddJournal").orderBy("timestamp").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.d("data1", queryDocumentSnapshots.toString());
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    Log.d(" aaaaaa1", "" + list);
                    int counter = 0;
                    for (DocumentSnapshot d : list) {
                       // Log.d("data" + counter++, d.toObject(JournalData.class).getjDescription());
                        JournalData p = d.toObject(JournalData.class);
                        journalDataList.add(p);


                    }
                } else {
                    Log.d("data2", queryDocumentSnapshots.toString());
                }

                desAdapter = new JournalAdapter(activity, journalDataList);
                myJournal_RV.setAdapter(desAdapter);
                desAdapter.notifyDataSetChanged();

            }
        });
        backButton = (ImageButton) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        button = view.findViewById(R.id.add_message);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Add Journal");
                alertDialog.setPositiveButton(android.R.string.ok,null);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final Context context = inflater.getContext();
                final LayoutInflater inflater1 = LayoutInflater.from(context);
                //
                View description_view = inflater1.inflate(R.layout.write_journal_edittext, null);
                final EditText description = (EditText) description_view.findViewById(R.id.journal_description_item);
                jDescription = description;

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                final CollectionReference addJournalRef = db.collection("users");

                addJournalRefs = addJournalRef;

                alertDialog.setView(description_view);
                alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(description.getText().toString())) {
                            Toast.makeText(context, "Kindly Fill ", Toast.LENGTH_SHORT).show();
                            description.setError("aaaaa");
                        } else {

                            addNote();
                            addJournalRef.document(userID)
                                    .collection("AddJournal").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    if (!queryDocumentSnapshots.isEmpty()) {

                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        Log.d(" aaaaaa", "" + list);
                                        for (DocumentSnapshot d : list) {

                                            JournalData p = d.toObject(JournalData.class);
                                            journalDataList.add(p);

                                        }
                                    }
                                }
                            });
                        }
                    }
                });
                alertDialog.show();
            }
        });
        return view;
    }


    private void addNote() {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss");
        String format = simpleDateFormat.format(new Date());
        String timestamp = format;
        Log.i("timestamp", timestamp);
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = "" + mdformat.format(calender.getTime());
        String jDescriptions = jDescription.getText().toString();
        Date d = Calendar.getInstance().getTime();
        CharSequence s = DateFormat.format("MMMM d, yyyy ",d.getTime());
        JournalData note = new JournalData(jDescriptions, s.toString(),timestamp);
        Log.i("time" , String.valueOf(s));

        addJournalRefs.document(userID)
                .collection("AddJournal").add(note);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                FragmentHome backButton = new FragmentHome();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, backButton, "healthFragment")
                        .addToBackStack(null)
                        .commit();

                break;
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
