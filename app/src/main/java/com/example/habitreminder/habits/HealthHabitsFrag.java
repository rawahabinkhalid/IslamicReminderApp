package com.example.habitreminder.habits;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.habitreminder.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HealthHabitsFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HealthHabitsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthHabitsFrag extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HealthHabitsFrag() {
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
    public static HealthHabitsFrag newInstance(String param1, String param2) {
        HealthHabitsFrag fragment = new HealthHabitsFrag();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_habits, container, false);
        ImageButton backbtn = view.findViewById(R.id.backButton);
        backbtn.setOnClickListener(this);
        //TextView back = view.findViewById(R.id.goBack);
        LinearLayout takeVit = view.findViewById(R.id.takeVit);
        takeVit.setOnClickListener(this);
        Button save = view.findViewById(R.id.save);
        save.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backButton:
               HabitSelectionActivityFrag backButton = new HabitSelectionActivityFrag();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, backButton, "healthFragment")
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.takeVit:
                Toast.makeText(getActivity(), "thek hai", Toast.LENGTH_SHORT).show();
                AddFrequency takeVitamin = new AddFrequency();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, takeVitamin, "takevit")
                        .addToBackStack("takeVit")
                        .commit();
                break;

            case R.id.save:
//                AddFrequency takeVitamin = new AddFrequency();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
//                        .replace(R.id.mainFrame, takeVitamin, "takevit")
//                        .addToBackStack("takeVit")
//                        .commit();
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
