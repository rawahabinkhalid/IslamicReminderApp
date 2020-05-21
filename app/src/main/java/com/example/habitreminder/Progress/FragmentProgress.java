package com.example.habitreminder.Progress;


import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontStyle;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.DocumentsContract;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProgress extends Fragment {

    PieChart pieChart;
    PieChart pieChartStreaks;
    String reminder_completed_text;
    String streaks_completed_text;
    public String userID;
    String ReminderCompleted = "0";
    int totalDoneReminder = 0;
    int totalRemainingReminder = 0;
    String currentStreak = "0";
    String bestStreak = "0";
    TextView upcoming_reminders_total;
    TextView remianing_time_digits;
    TextView logestStreakDay;
    TextView ajrTracker;
    TextView progress_daily_goals;
    TextView upcoming_reminders;
    TextView streaks;
    TextView longestStreak;
    String reminder_completed_text_firebase = "", streaks_completed_text_firebase = "";

    public FragmentProgress() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Rootview = null;
        OnboardPreferenceManager oPms = new OnboardPreferenceManager(getContext());

        if (oPms.isGoogleSignIn()) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                userID = acct.getId();
                Log.d("isss", userID);
            }
        } else if (oPms.isFacebookSignIn()) {

        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            cPN.setUID(mAuth.getCurrentUser().getUid());
            userID = mAuth.getCurrentUser().getUid();
        }
        Rootview = inflater.inflate(R.layout.fragment_progress, container, false);
        // Inflate the layout for this fragment
        ajrTracker = Rootview.findViewById(R.id.ajrTracker);
        progress_daily_goals = Rootview.findViewById(R.id.progress_daily_goals);
        upcoming_reminders = Rootview.findViewById(R.id.upcoming_reminders);
        streaks = Rootview.findViewById(R.id.streaks);
        longestStreak = Rootview.findViewById(R.id.longestStreak);
//        progress_daily_goals = Rootview.findViewById(R.id.progress_daily_goals);
        pieChart = Rootview.findViewById(R.id.remindersCompleted);
        pieChartStreaks = Rootview.findViewById(R.id.streaksCompleted);
        upcoming_reminders_total = Rootview.findViewById(R.id.upcoming_reminders_total);
        remianing_time_digits = Rootview.findViewById(R.id.remianing_time_digits);
        logestStreakDay = Rootview.findViewById(R.id.logestStreakDay);
        fetchStringResources();
        return Rootview;
    }

    private void streakchartmethod() {
        pieChartStreaks.setUsePercentValues(false);
        pieChartStreaks.getDescription().setEnabled(false);
        pieChartStreaks.setExtraOffsets(5, 10, 5, 5);

        pieChartStreaks.setDragDecelerationFrictionCoef(0.95f);

        pieChartStreaks.setDrawHoleEnabled(true);
        pieChartStreaks.setHoleColor(Color.WHITE);
        pieChartStreaks.setTransparentCircleRadius(0);
        pieChartStreaks.setRotationEnabled(false);
        if(streaks_completed_text_firebase.equals(""))
            streaks_completed_text = getString(R.string.current_streaks) + currentStreak;
        else
            streaks_completed_text = streaks_completed_text_firebase + currentStreak;
//        streaks_completed_text = getString(R.string.current_streaks) + currentStreak;
        SpannableString ss = new SpannableString(streaks_completed_text);
        String[] words = streaks_completed_text.split("\n");  // uses an array
        String lastWord = words[words.length - 1];

        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);

        ForegroundColorSpan fcsgreen = new ForegroundColorSpan(Color.parseColor("#112331"));
        ForegroundColorSpan fcspink = new ForegroundColorSpan(Color.parseColor("#FFCFB5"));
        ss.setSpan(fcsgreen, 0, streaks_completed_text.length() - lastWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(boldSpan, streaks_completed_text.length() - lastWord.length(), streaks_completed_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcspink, streaks_completed_text.length() - lastWord.length(), streaks_completed_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(fcsgreen, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(FontStyle.FONT_WEIGHT_MAX, 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(fcspink, 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChartStreaks.setCenterText(ss);
        pieChartStreaks.setCenterTextSize(18);
        pieChartStreaks.setCenterTextColor(Color.parseColor("#113221"));
        pieChartStreaks.setHoleRadius(75);
        pieChartStreaks.getLegend().setEnabled(false);
        double percent = 0;
        double currentStr = Integer.parseInt(currentStreak);
        double bestStr = Integer.parseInt(bestStreak);
        if (bestStr > 0)
            percent = currentStr / bestStr * 100;
//        percent = Math.floor(Integer.parseInt(String.valueOf(percent)));
//        percent = Integer.parseInt(String.valueOf( Math.floor(Double.parseDouble(String.valueOf(percent)))));
        double rem_percent = 100 - percent;
        logestStreakDay.setText(String.valueOf(bestStreak));
        Log.i("streak", String.valueOf(percent));
        ArrayList<PieEntry> Val = new ArrayList<>();

        Val.add(new PieEntry(Float.parseFloat(String.valueOf(percent)), ""));
        Val.add(new PieEntry(Float.parseFloat(String.valueOf(rem_percent)), ""));
        final int[] MY_COLORS = {Color.rgb(191, 239, 187), Color.rgb(230, 230, 230)};
        PieDataSet dataSet = new PieDataSet(Val, "Streaks");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(MY_COLORS);
        PieData data = new PieData(dataSet);
//        data.setValueTextSize(10f);
//        data.setValueTextColor(Color.YELLOW);
        data.setValueTextSize(0);


        pieChartStreaks.setData(data);
        pieChartStreaks.invalidate();
    }

    private void piechartmethod() {
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setRotationEnabled(false);
        if(reminder_completed_text_firebase.equals(""))
            reminder_completed_text = getString(R.string.pie_chart_reminder_heading) + ReminderCompleted;
        else
            reminder_completed_text = reminder_completed_text_firebase + ReminderCompleted;
        SpannableString ss = new SpannableString(reminder_completed_text);

//        Log.i("LastStringPos", reminder_completed_text.substring(reminder_completed_text.lastIndexOf(' ') + 1));
//        Log.i("LastStringPos", reminder_completed_text.substring(reminder_completed_text.lastIndexOf(' ') + 1).length() + "");
        String[] words = reminder_completed_text.split("\n");  // uses an array
        String lastWord = words[words.length - 1];

        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);

        ForegroundColorSpan fcsgreen = new ForegroundColorSpan(Color.parseColor("#112331"));
        ForegroundColorSpan fcspink = new ForegroundColorSpan(Color.parseColor("#FFCFB5"));
        ss.setSpan(fcsgreen, 0, reminder_completed_text.length() - lastWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(boldSpan, reminder_completed_text.length() - lastWord.length(), reminder_completed_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcspink, reminder_completed_text.length() - lastWord.length(), reminder_completed_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(fcsgreen, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(FontStyle.FONT_WEIGHT_MAX, 19, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(fcspink, 19, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChart.setCenterText(ss);
        pieChart.setCenterTextSize(18);
        pieChart.setCenterTextColor(Color.parseColor("#113221"));
        double reminderCompleted = Integer.parseInt(ReminderCompleted);
        double TotalDoneReminder = totalDoneReminder;
        double percent = 0;
        if (totalDoneReminder > 0)
            percent = reminderCompleted / TotalDoneReminder * 100;
        Log.i("streak", String.valueOf(percent));
        double rem_percent = 100 - percent;
        Log.i("ReminderCount", String.valueOf(percent));
        pieChart.setHoleRadius(75);
        pieChart.getLegend().setEnabled(false);
        upcoming_reminders_total.setText(String.valueOf(totalRemainingReminder));

        ArrayList<PieEntry> Val = new ArrayList<>();

        Val.add(new PieEntry(Float.parseFloat(String.valueOf(percent)), ""));
        Val.add(new PieEntry(Float.parseFloat(String.valueOf(rem_percent)), ""));
        final int[] MY_COLORS = {Color.rgb(254, 207, 181), Color.rgb(230, 230, 230)};
        PieDataSet dataSet = new PieDataSet(Val, "Reminder Completed");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(MY_COLORS);
        PieData data = new PieData(dataSet);
//        data.setValueTextSize(10f);
//        data.setValueTextColor(Color.YELLOW);
        data.setValueTextSize(0);


        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void getReminderCount() {
        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
        dbMain.collection("users").document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                DocumentSnapshot d = task.getResult();
                                if (d.get("ReminderDone") != null)
                                    ReminderCompleted = String.valueOf(d.get("ReminderDone"));
                                if (d.get("Record2") != null)
                                    currentStreak = String.valueOf(d.get("Record2"));
                                if (d.get("Record3") != null)
                                    bestStreak = String.valueOf(d.get("Record3"));
                                getTotalReminderCount();
                            } else {
                                Log.d("TagMainActivity", "Error getting documents: ", task.getException());
                            }
                    }
                });
    }

    private void getTotalReminderCount() {
        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
        dbMain.collection("users").document(userID).collection("AddReminder")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                Calendar calender = Calendar.getInstance();
                                try {
//                                    Date dateNow = new Date("dd/MM/yyyy HH:mm:ss");
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    Date date = new Date();
                                    Date dateNow = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(formatter.format(date));

//                                    SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                                    String strDate = "" + mdformat.format(calender.getTime());

//                                totalReminder = task.getResult().size();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.get("defaultDate") != null) {
                                            Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(String.valueOf(document.get("defaultDate")));
                                            Log.i("ReminderCount", String.valueOf(dateNow));
                                            Log.i("ReminderCount", String.valueOf(date1));
                                            if (date1.compareTo(dateNow) <= 0) {
                                                Log.i("ReminderCount", "if");
                                                totalDoneReminder++;
                                            } else {
                                                Log.i("ReminderCount", "else");
                                                totalRemainingReminder++;
                                            }
                                        }
                                    }
                                    piechartmethod();
                                    streakchartmethod();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.d("TagMainActivity", "Error getting documents: ", task.getException());
                            }
                    }
                });
    }

    private void fetchStringResources() {
        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
        dbMain.collection("graphs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ajrTracker.setText(String.valueOf(document.get("MainHeading")));
                                    progress_daily_goals.setText(String.valueOf(document.get("Graph1Heading")));
                                    upcoming_reminders.setText(document.get("Graph1Desc") + ": ");
                                    streaks.setText(String.valueOf(document.get("Graph2Heading")));
                                    longestStreak.setText(document.get("Graph2Desc") + ": ");
                                    streaks_completed_text_firebase = String.valueOf(document.get("Graph2Text")).replace(" ", "\n") + "\n\n";
                                    reminder_completed_text_firebase = String.valueOf(document.get("Graph1Text")).replace(" ", "\n") + "\n\n";
                                    reminder_completed_text = reminder_completed_text_firebase + ReminderCompleted;
                                    streaks_completed_text = streaks_completed_text_firebase + currentStreak;

                                    getReminderCount();
                                    piechartmethod();
                                    streakchartmethod();
                                }
                            } else {
                                Log.d("TagJournal", "Error getting documents: ", task.getException());
                            }
                    }
                });

    }


}
