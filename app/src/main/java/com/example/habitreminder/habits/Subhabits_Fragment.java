package com.example.habitreminder.habits;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitreminder.Adapters.Main_Habits_Adapter;
import com.example.habitreminder.Adapters.Sub_Habits_Adapter;
import com.example.habitreminder.Data.HabitsData;
import com.example.habitreminder.Data.SubHabits;
import com.example.habitreminder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;


public class Subhabits_Fragment extends Fragment implements View.OnClickListener {


    private FirebaseFirestore db;
    private RecyclerView mysub_HabitsRv;
    //    private List<HabitsData> myHabitslist;
    private List<SubHabits> subHabits = new ArrayList<>();
    private Sub_Habits_Adapter mySub_HabitsAdaper;
    private CollectionReference addHabitRef;
    private TextView heading_subTilte;
    private Button save;

    Activity activity;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Subhabits_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthHabitsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static Subhabits_Fragment newInstance(String param1, String param2) {
        Subhabits_Fragment fragment = new Subhabits_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_habits, container, false);
        ImageButton backbtn = view.findViewById(R.id.backButton);
        heading_subTilte = view.findViewById(R.id.heading_subTilte);
        save = view.findViewById(R.id.save);
        save.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        Button save = view.findViewById(R.id.save);
        save.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        mysub_HabitsRv = view.findViewById(R.id.rv_sub_habits);
        addHabitRef = db.collection("habits");

        GridLayoutManager gl = new GridLayoutManager(getActivity(), 2);
        gl.setOrientation(gl.VERTICAL);
        mysub_HabitsRv.setLayoutManager(gl);
//        myHabitslist = new ArrayList<>();
        SharedPreferences sharedPreferences2 = this.getActivity().getSharedPreferences("Name_main",
                Context.MODE_PRIVATE);
        final String documentName = sharedPreferences2.getString("name_main", "");
        final String documentKey = sharedPreferences2.getString("key_main", "");
        Log.i("name_main", documentName);

        heading_subTilte.setText(documentName);
        db.collection("habits/").document(documentKey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                List<HabitsData> habitData = new ArrayList<>();
                if (task.isSuccessful()) {
                    DocumentSnapshot d = task.getResult();
//                    Log.i("subHabitsData", String.valueOf(d));
//                    Log.i("subHabitsData", String.valueOf(d.get("SubHabits")));
//                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
//
//                    for (DocumentSnapshot d : myListOfDocuments) {
//                    String name = d.getString("Name");
//                        if (!name.equals(documentName)) {
//                            continue;
//                        }
//                    String notification = d.getString("Notification");
                    try {
                        List<Map<String, Object>> s = (List<Map<String, Object>>) d.get("SubHabits");
                        int index = 0;
                        for (Map<String, Object> data : s) {
//                            Log.i("subHabitsData", String.valueOf(data));
                            int frequencyAdvanced = Integer.parseInt(data.get("Frequency_Advanced").toString());
                            int frequency_Beginner = Integer.parseInt(data.get("Frequency_Beginner").toString());
                            int frequency_Intermediate = Integer.parseInt(data.get("Frequency_Intermediate").toString());
                            String sub_name = data.get("Name").toString();
                            String sub_notification = data.get("Notification").toString();

                            subHabits.add(new SubHabits(frequencyAdvanced, frequency_Beginner, frequency_Intermediate, String.valueOf(index), sub_name, sub_notification));
                            index++;
                        }
                        Log.i("subHabitsData", String.valueOf(subHabits));
//                    habitData.add(new HabitsData(d.getId(), name, notification, subHabits));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                }
//                Log.i("subHabitsData", String.valueOf(habitData));
                mySub_HabitsAdaper = new Sub_Habits_Adapter(getActivity(), subHabits);
                mysub_HabitsRv.setAdapter(mySub_HabitsAdaper);
                mySub_HabitsAdaper.notifyDataSetChanged();
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                HabitSelectionActivityFrag backButton = new HabitSelectionActivityFrag();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, backButton, "healthFragment")
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.save:
                AddFrequency takeVitamin = new AddFrequency();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, takeVitamin, "takevit")
                        .addToBackStack("takeVit")
                        .commit();
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
