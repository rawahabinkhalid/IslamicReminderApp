package com.example.habitreminder.userhome;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.habitreminder.Journal.WriteJournal;
import com.example.habitreminder.R;
import com.example.habitreminder.Reminders.AddCustomHabit;
import com.example.habitreminder.habits.HabitSelectionActivityFrag;

public class AddHabitFragment extends Fragment implements View.OnClickListener{

    public AddHabitFragment() {
        // Required empty public constructor
    }

    Activity activity;
    public AddHabitFragment(Activity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_habit, container, false);
        LinearLayout habitLayout = view.findViewById(R.id.goto_habits);
        habitLayout.setOnClickListener(this);
        LinearLayout reminderLayout = view.findViewById(R.id.goto_reminders);
        reminderLayout.setOnClickListener(this);
        LinearLayout journalLayout = view.findViewById(R.id.goto_journal);
        journalLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goto_habits:
                HabitSelectionActivityFrag habitFragment = new HabitSelectionActivityFrag();
                fragmentTransaction(habitFragment);
                break;
            case R.id.goto_reminders:
              AddCustomHabit habitFragment1 = new AddCustomHabit();
               fragmentTransaction(habitFragment1);
                break;
            case R.id.goto_journal:
                WriteJournal writeJournal = new WriteJournal(activity);
                fragmentTransaction(writeJournal);
                break;
        }
    }

    private void fragmentTransaction(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.activity_slide_from_bottom, R.anim.activity_slide_from_top, R.anim.activity_slide_from_bottom, R.anim.activity_slide_from_top)
            .replace(R.id.mainFrame, fragment)
            .addToBackStack(null)
            .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
