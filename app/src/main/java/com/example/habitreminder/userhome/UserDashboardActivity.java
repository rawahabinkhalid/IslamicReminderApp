package com.example.habitreminder.userhome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.habitreminder.Calender.FragmentCalender;
import com.example.habitreminder.Common;
import com.example.habitreminder.Journal.WriteJournal;
import com.example.habitreminder.ProfileSettings.FragmentProfile_and_Settings;
import com.example.habitreminder.Progress.FragmentProgress;
import com.example.habitreminder.R;
import com.example.habitreminder.Reminders.AddCustomHabit;
import com.example.habitreminder.habits.HabitSelectionActivityFrag;
import com.example.habitreminder.habits.Subhabits_Fragment;
import com.example.habitreminder.Reminders.ScheduleHabit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.List;

import static java.security.AccessController.getContext;

public class UserDashboardActivity extends AppCompatActivity implements
        AddHabitFragment.OnFragmentInteractionListener,
        HabitSelectionActivityFrag.OnFragmentInteractionListener,
        Subhabits_Fragment.OnFragmentInteractionListener,
        AddCustomHabit.OnFragmentInteractionListener,
        ScheduleHabit.OnFragmentInteractionListener,
        WriteJournal.OnFragmentInteractionListener {

    private FragmentHome homeFragment;
    private AddHabitFragment addHabitFragment;
    private FragmentCalender calenderFragment = new FragmentCalender();
    private FragmentProfile_and_Settings profile_and_settingsFragment;
    private FragmentProgress fragmentProgress;
    private int flag = 0;
    private int fragmentState = 1;
    private boolean doubleBackToExitPressedOnce = false;
    private Context _context;

    public UserDashboardActivity() throws GeneralSecurityException, IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FloatingActionButton addHabit = findViewById(R.id.fab);
        homeFragment = new FragmentHome();
//        calenderFragment = new FragmentCalender();
        addHabitFragment = new AddHabitFragment(this);
        profile_and_settingsFragment = new FragmentProfile_and_Settings();
        fragmentProgress = new FragmentProgress();
        _context = this;

        setFragment(homeFragment);

//        new MyAsyncTask().execute();

        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    setFragment(addHabitFragment);
                    flag = 1;
                } else if (flag == 1) {
                    flag = 0;
                    if (fragmentState == 1) {
                        setFragment(homeFragment);
                    } else if (fragmentState == 2) {
                        setFragment(calenderFragment);
                    } else if (fragmentState == 4) {
                        setFragment(fragmentProgress);
                    } else if (fragmentState == 5) {
                        setFragment(profile_and_settingsFragment);
                    }
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentState = 1;
                    setFragment(homeFragment);
                    break;
                case R.id.navigation_calender:
                    fragmentState = 2;
                    setFragment(calenderFragment);
                    break;
                case R.id.navigation_barchart:
                    setFragment(fragmentProgress);
                    fragmentState = 4;
                    break;
                case R.id.navigation_profile:
                    setFragment(profile_and_settingsFragment);
                    fragmentState = 5;
                    break;
            }
            return false;
        }
    };

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.activity_slide_from_bottom, R.anim.activity_slide_from_top, R.anim.activity_slide_from_bottom, R.anim.activity_slide_from_top);
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1500);
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
//            while (_context != null) {
////                Log.i("productsAsync", "in loop");
////                Log.i("productsAsync", String.valueOf(selectedProductsDataList));
//                SharedPreferences data = _context.getSharedPreferences("NotificationData",
//                        Context.MODE_PRIVATE);
//                String habitKey = data.getString("habitKey", "");
//                String habitName = data.getString("habitName", "");
//                String title = data.getString("title", "");
//                String body = data.getString("body", "");
//
//                if(!habitKey.equals("") && !title.equals("") && !body.equals("")) {
//                    SharedPreferences companyId = _context.getSharedPreferences("NotificationData",
//                            Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = companyId.edit();
//                    editor.putString("habitKey", "");
//                    editor.putString("habitName", "");
//                    editor.putString("title", "");
//                    editor.putString("body", "");
//                    editor.commit();
//
//                    final FragmentManager fm = ((FragmentActivity) _context).getSupportFragmentManager();
//
//                    final AlertDialog alertDialog = new AlertDialog.Builder(UserDashboardActivity.this).create();
//                    LayoutInflater inflater = LayoutInflater.from(_context);
//                    View view_popup = inflater.inflate(R.layout.notification_alert, null);
//                    alertDialog.setView(view_popup);
//                    TextView title_tv = (TextView) view_popup.findViewById(R.id.title_tv);
//                    TextView body_tv = (TextView) view_popup.findViewById(R.id.body_tv);
//                    title_tv.setText(title);
////        body_tv.setText(respMsg);
//                    body_tv.setText("Did you perform '" + habitName + "' habit?");
//                    Button btn_yes = (Button) view_popup.findViewById(R.id.btn_yes);
//                    btn_yes.setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            Toast.makeText(_context, "Yes", Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                    Button btn_no = (Button) view_popup.findViewById(R.id.btn_no);
//                    btn_no.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(_context, "No", Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                    alertDialog.show();
//
//
////                    Common.displayAlert(_context, habitKey, habitName, title, body);
//                    return null;
//                }
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (_context != null)
                new MyAsyncTask().execute();
        }
    }
}
