package com.example.habitreminder.habits;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.habitreminder.Data.HabitsData;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.example.habitreminder.userhome.FragmentHome;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddCustomHabit extends Fragment implements View.OnClickListener , TimePickerDialog.OnTimeSetListener {

    private static final int NOTIFICATION_REMINDER_NIGHT = 1;
    private OnFragmentInteractionListener mListener;
    private EditText habit_title;
    private Button addHabits;
    private FirebaseFirestore db;
    private CollectionReference addHabitRef;
    private String userID;
    private LinearLayout setCustom_Time;
    int Pickhours , Pickminuts ;
    int hours , minuts;


    public AddCustomHabit() {
        // Required empty public constructor
    }

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
        View view = inflater.inflate(R.layout.fragment_add_custom_habit, container, false);
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        habit_title = view.findViewById(R.id.habit_title);
        addHabits = view.findViewById(R.id.save);
        setCustom_Time = view.findViewById(R.id.custom_time);
        db = FirebaseFirestore.getInstance();



        addHabitRef = db.collection("users");
        addHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(habit_title.getText())){
                    habit_title.setError("Add Title");
                }
                else {

                    addHabits();
                    Toast.makeText(getActivity(), "Reminder Added", Toast.LENGTH_SHORT).show();

                }


            }
        });


        // listner for custom time
        LinearLayout write_habit = view.findViewById(R.id.custom_time);
        LinearLayout no_timer = view.findViewById(R.id.no_timer);
        write_habit.setOnClickListener(this);
        String title = habit_title.getText().toString();
        no_timer.setOnClickListener(this);

        return view;
    }

    private void addHabits() {
        Calendar calender = Calendar.getInstance();

        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = "" + mdformat.format(calender.getTime());

        String title = habit_title.getText().toString();
        HabitsData habitsData = new HabitsData(title, strDate);
        Log.d("myData", habitsData.toString());
        addHabitRef.document(userID).collection("AddHabits").add(habitsData);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_timer:
                ScheduleHabit scheduleHabit = new ScheduleHabit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, scheduleHabit, "addCustomHabit")
                        .addToBackStack("habitFragment")
                        .commit();
                break;
            case R.id.custom_time:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final Context context = inflater.getContext();
                final LayoutInflater inflater1 = LayoutInflater.from(context);
                //
                View description_view = inflater1.inflate(R.layout.time_picker, null);

                alertDialog.setView(description_view);
                alertDialog.show();

            break;
            case R.id.backButton:
                FragmentHome backHome = new FragmentHome();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, backHome, "addCustomHabit")
                        .addToBackStack("habitFragment")
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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
