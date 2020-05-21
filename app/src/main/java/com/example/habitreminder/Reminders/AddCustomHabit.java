package com.example.habitreminder.Reminders;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.habitreminder.Data.ReminderData;
import com.example.habitreminder.Data.ReminderDataWithoutTime;
import com.example.habitreminder.NotificationPublisher;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.example.habitreminder.userhome.FragmentHome;
import com.example.habitreminder.userhome.UserDashboardActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AddCustomHabit extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private static final int NOTIFICATION_REMINDER_NIGHT = 1;
    private OnFragmentInteractionListener mListener;
    private EditText habit_title;
    private Button addHabits, five_min, ten_min, half_hour;
    private LinearLayout no_timer, write_habit;
    private FirebaseFirestore db;
    private CollectionReference addHabitRef;
    private String userID, selectedTypeOfReminder, time, timestamp, Token;
    private LinearLayout setCustom_Time;
    private TimePicker timePicker1;
    int Pickhours, Pickminuts;
    int hours, minuts;


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
//        View view = inflater.inflate(R.layout.fragment_add_custom_habit, container, false);
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        habit_title = view.findViewById(R.id.habit_title);
        addHabits = view.findViewById(R.id.save);
        setCustom_Time = view.findViewById(R.id.custom_time);
        five_min = view.findViewById(R.id.five_min);
        ten_min = view.findViewById(R.id.ten_min);
        half_hour = view.findViewById(R.id.half_hour);
        SharedPreferences companyId = this.getActivity().getSharedPreferences("Token",
                Context.MODE_PRIVATE);
        Token = companyId.getString("token", "");
        selectedTypeOfReminder = "";
        time = "";

        db = FirebaseFirestore.getInstance();


        addHabitRef = db.collection("users");
        addHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(habit_title.getText())) {
                    habit_title.setError("Add Title");
                } else {

                    addHabits();

                }


            }
        });


        // listner for custom time
        write_habit = view.findViewById(R.id.custom_time);
        no_timer = view.findViewById(R.id.no_timer);
        write_habit.setOnClickListener(this);
        five_min.setOnClickListener(this);
        ten_min.setOnClickListener(this);
        half_hour.setOnClickListener(this);

        String title = habit_title.getText().toString();
        no_timer.setOnClickListener(this);

        return view;
    }

    private void addHabits() {
        Calendar calender = Calendar.getInstance();

        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = "" + mdformat.format(calender.getTime());

        final String title = habit_title.getText().toString();
        if (selectedTypeOfReminder.equals("WithoutTimer")) {
            if (!title.equals("")) {
                ReminderDataWithoutTime reminderData = new ReminderDataWithoutTime(title, selectedTypeOfReminder, strDate);
                addHabitRef.document(userID).collection("AddReminder").add(reminderData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess : task was successful");
                        Toast.makeText(getActivity(), "Reminder Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), UserDashboardActivity.class));
                        ((FragmentActivity) getContext()).finish();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onSuccess : task was unsuccessful");
                            }
                        });
            } else {
                Toast.makeText(getContext(), "Kindly enter title.", Toast.LENGTH_LONG).show();
            }
        } else {
            if (time.equals(""))
                Toast.makeText(getContext(), "Kindly select time.", Toast.LENGTH_LONG).show();
            else if (title.equals(""))
                Toast.makeText(getContext(), "Kindly enter title.", Toast.LENGTH_LONG).show();
            else {
                ReminderData reminderData = new ReminderData(title, selectedTypeOfReminder, time, strDate);
                addHabitRef.document(userID).collection("AddReminder").add(reminderData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess : task was successful");
                        setNotificationForReminder(title);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onSuccess : task was unsuccessful");
                            }
                        });
            }
        }
//        Log.d("myData", reminderData.toString());


    }

    private void setNotificationForReminder(String title) {
        Map<String, Object> map = new HashMap<>();

        map.put("Body", "Reminder: " + title);
        map.put("Sound", "Enabled");
        map.put("Time", timestamp);
        map.put("Title", title);
        map.put("Token", Token);
        map.put("Type", "Reminder");
        map.put("UserID", userID);
        map.put("Status", "Pending");
        db.collection("notifications").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess : task was successful");
                Toast.makeText(getActivity(), "Reminder Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), UserDashboardActivity.class));
                ((FragmentActivity) getContext()).finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onSuccess : task was unsuccessful");
                    }
                });
    }


    private void resetAllTimer() {
        five_min.setBackground(getResources().getDrawable(R.drawable.five_min_button));
        ten_min.setBackground(getResources().getDrawable(R.drawable.ten_min_button));
        half_hour.setBackground(getResources().getDrawable(R.drawable.half_hour_button));
        no_timer.setBackground(getResources().getDrawable(R.drawable.write_habit_button));
        write_habit.setBackground(getResources().getDrawable(R.drawable.write_habit_button));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_timer:
//                ScheduleHabit scheduleHabit = new ScheduleHabit();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
//                        .replace(R.id.mainFrame, scheduleHabit, "addCustomHabit")
//                        .addToBackStack("habitFragment")
//                        .commit();
                resetAllTimer();
                no_timer.setBackground(getResources().getDrawable(R.drawable.write_habit_button_selected));
                selectedTypeOfReminder = "WithoutTimer";
                break;
            case R.id.five_min:
                resetAllTimer();
                five_min.setBackground(getResources().getDrawable(R.drawable.five_min_button_selected));
                selectedTypeOfReminder = "During";
                time = "5 Min";
//                scheduleNotification(getNotification("5 minutes delay"), 5000);
                getTimeFromDuration(5);
                break;
            case R.id.ten_min:
                resetAllTimer();
                ten_min.setBackground(getResources().getDrawable(R.drawable.ten_min_button_selected));
                selectedTypeOfReminder = "During";
                time = "10 Min";
//                scheduleNotification(getNotification("10 minutes delay"), 10000*60);
                getTimeFromDuration(10);
                break;
            case R.id.half_hour:
                resetAllTimer();
                half_hour.setBackground(getResources().getDrawable(R.drawable.half_hour_button_selected));
                selectedTypeOfReminder = "During";
                time = "30 Min";
//                scheduleNotification(getNotification("30 minutes delay"), 30000*60);
                getTimeFromDuration(30);
                break;
            case R.id.custom_time:
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final Context context = inflater.getContext();
//                final LayoutInflater inflater1 = LayoutInflater.from(context);
//                //
//                View description_view = inflater1.inflate(R.layout.time_picker, null);
//
//                alertDialog.setView(description_view);
//                alertDialog.show();
                Calendar mcurrentTime = Calendar.getInstance();
                final int year = mcurrentTime.get(Calendar.YEAR);
                final int month = mcurrentTime.get(Calendar.MONTH);
                final int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        eReminderTime.setText( selectedHour + ":" + selectedMinute);
                        selectedTypeOfReminder = "Custom";
                        time = selectedHour + ":" + selectedMinute;
                        String str_month = String.valueOf(month + 1);
                        if(str_month.length() == 1)
                            str_month = "0" + str_month;
                        timestamp = year + "-" + str_month + "-" + day + " " + selectedHour + ":" + selectedMinute + ":00.000";
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

                resetAllTimer();
                write_habit.setBackground(getResources().getDrawable(R.drawable.write_habit_button_selected));
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

    private void getTimeFromDuration(int temp_time) {
        Date date = new Date(System.currentTimeMillis() + temp_time * 60 * 1000);
        long time_long = date.getTime();
        Timestamp ts = new Timestamp(time_long);
        timestamp = String.valueOf(ts);
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

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(getContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(getContext());
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_stat_ic_notification);
        return builder.build();
    }
}
