package com.example.habitreminder.habits;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitreminder.Adapters.Main_Habits_Adapter;
import com.example.habitreminder.Data.HabitsData;
import com.example.habitreminder.Data.SubHabits;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.example.habitreminder.userhome.FragmentHome;
import com.firebase.client.DataSnapshot;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class HabitSelectionActivityFrag extends Fragment implements View.OnClickListener {
    private Button saveData;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //    private CollectionReference addHabitRef;
    private String userID;
    private FirebaseFirestore db;
    private RecyclerView myHabitsRv;
    //    private List<HabitsData> myHabitslist;
    private Main_Habits_Adapter myHabitsAdaper;


    Activity activity;

    public HabitSelectionActivityFrag(Activity activity) {
        this.activity = activity;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HabitSelectionActivityFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HabitSelectionActivityFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static HabitSelectionActivityFrag newInstance(String param1, String param2) {
        HabitSelectionActivityFrag fragment = new HabitSelectionActivityFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //OnboardPreferenceManager oPm = new OnboardPreferenceManager(getContext());

//        if (oPm.isGoogleSignIn()) {
//            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
//            if (acct != null) {
//                userID = acct.getId();
//            }
//        } else if (oPm.isFacebookSignIn()) {
//
//        } else {
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            userID = mAuth.getCurrentUser().getUid();
//        }
        View view = inflater.inflate(R.layout.fragment_habit_selection_activity, container, false);
        ImageButton backButton = view.findViewById(R.id.backButton);
        saveData = view.findViewById(R.id.saveData);
        backButton.setOnClickListener(this);
        saveData.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        myHabitsRv = view.findViewById(R.id.rv_main_habits);
//        addHabitRef = db.collection("habits");

        GridLayoutManager gl = new GridLayoutManager(getActivity(), 2);
        gl.setOrientation(gl.VERTICAL);
        myHabitsRv.setLayoutManager(gl);
//        myHabitslist = new ArrayList<>();

        //addHabitRef =
        db.collection("habits/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<HabitsData> habitData = new ArrayList<>();
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
//                    for (DocumentSnapshot d : myListOfDocuments) {
//                        Log.i("HabitsSelect", String.valueOf(d.getId()));
//                        List<Map<String, Object>> subHabits = (List<Map<String, Object>>) d.get("SubHabits");
//                        Log.i("subHabits_Name", String.valueOf(d.getString("Name")));
//                        Log.i("subHabits_Notification", String.valueOf(d.getString("Notification")));
//                        Log.i("subHabits", String.valueOf(subHabits));
//                        HabitsData tempHabitModel = new HabitsData(d.getId(), d.getString("Name"), d.getString("Notification"), subHabits);
//                        habitData.add(tempHabitModel);
//                    }
                    for (DocumentSnapshot d : myListOfDocuments) {
                        String name = d.getString("Name");
                        String notification = d.getString("Notification");

                        List<SubHabits> subHabits = new ArrayList<>();
                        List<Map<String, Object>> s = (List<Map<String, Object>>) d.get("SubHabits");
                        int index = 0;
                        for (Map<String, Object> data : s) {
                            Log.i("subHabitsData", String.valueOf(data));
                            int frequencyAdvanced = Integer.parseInt(data.get("Frequency_Advanced").toString());
                            int frequency_Beginner = Integer.parseInt(data.get("Frequency_Beginner").toString());
                            int frequency_Intermediate = Integer.parseInt(data.get("Frequency_Intermediate").toString());
                            String sub_name = data.get("Name").toString();
                            String sub_notification = data.get("Notification").toString();

                            subHabits.add(new SubHabits(frequencyAdvanced, frequency_Beginner, frequency_Intermediate, String.valueOf(index), sub_name, sub_notification));
                            index++;
                        }

                        habitData.add(new HabitsData(d.getId(), name, notification, subHabits));
                    }

                }

//            }

                myHabitsAdaper = new Main_Habits_Adapter(getActivity(), habitData);
                myHabitsRv.setAdapter(myHabitsAdaper);
                myHabitsAdaper.notifyDataSetChanged();
            }
        });


       /* addHabitRef.document().collection("").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.d("data1", queryDocumentSnapshots.toString());
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    Log.d(" aaaaaa1", "" + list);
                    int counter = 0;
                    for (DocumentSnapshot d : list) {
                        // Log.d("data" + counter++, d.toObject(JournalData.class).getjDescription());
                        HabitsData p = d.toObject(HabitsData.class);
                        myHabitslist.add(p);


                    }
                } else {
                    Log.d("data2", queryDocumentSnapshots.toString());
                }


            }
        });
*/
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.backButton:
                FragmentHome backButton = new FragmentHome();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, backButton, "healthFragment")
                        .addToBackStack(null)
                        .commit();

                break;
//            case R.id.health:
//                Subhabits_Fragment subhabitsFragment = new Subhabits_Fragment();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
//                        .replace(R.id.mainFrame, subhabitsFragment, "healthFragment")
//                        .addToBackStack("habitFragment")
//                        .commit();
//                break;
            case R.id.saveData:
                // SaveData();
                Toast.makeText(getActivity(), "Save Data", Toast.LENGTH_SHORT).show();
                AddFrequency frequency = new AddFrequency();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, frequency, "healthFragment")
                        .addToBackStack(null)
                        .commit();
                break;

        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
