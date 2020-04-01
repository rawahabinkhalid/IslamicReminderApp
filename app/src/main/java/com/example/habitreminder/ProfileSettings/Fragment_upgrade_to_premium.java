package com.example.habitreminder.ProfileSettings;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.habitreminder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_upgrade_to_premium extends Fragment {


    public Fragment_upgrade_to_premium() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_upgrade_to_premium, container, false);
        TextView back = view.findViewById(R.id.back_frag_upgradepremium);
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
        return view;
    }

}
