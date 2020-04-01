package com.example.habitreminder.habits;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.habitreminder.R;
import com.example.habitreminder.userhome.FragmentHome;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFrequency extends Fragment implements  View.OnClickListener{

    private ImageButton backButton;
    private Button save;

    public AddFrequency() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_frequency, container, false);
         backButton = root.findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        save = root.findViewById(R.id.save);
        save.setOnClickListener(this);


    return  root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backButton:
                HealthHabitsFrag backButton = new HealthHabitsFrag();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, backButton, "healthFragment")
                        .addToBackStack(null)
                        .commit();

                break;
            case  R.id.save:
                Toast.makeText(getActivity(), "Save info", Toast.LENGTH_SHORT).show();
                FragmentHome fragmentHome = new FragmentHome();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, fragmentHome, "healthFragment")
                        .addToBackStack(null)
                        .commit();

                break;


    }
}
}
