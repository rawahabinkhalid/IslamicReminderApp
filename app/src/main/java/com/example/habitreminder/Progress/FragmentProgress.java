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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.habitreminder.Data.CalendarData;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private TextView record_heading;
    private TextView total_perfect_days1;
    private TextView record_current_streak;
    private TextView your_best_streak;
    private TextView total_habits_done;

    private TextView home_calender_heading;
    private TextView calender_home_subhead;
    private CalendarView calender_home;
    public List<CalendarData> calendarDataList;
    private ArrayList<String> description = new ArrayList<>();
    private ArrayList<String> end_date = new ArrayList<>();
    private ArrayList<String> start_date = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private String Locale = "%02d";
    private String template = "dd/MM/yyyy";
    private int month = -1;

    private TextView record_total_days_date;
    private TextView record_current_streak_date;
    private TextView best_streak_days_date;
    private TextView total_habits_done_date;

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
        record_heading = Rootview.findViewById(R.id.record_heading);
        total_perfect_days1 = Rootview.findViewById(R.id.total_perfect_days1);
        record_current_streak = Rootview.findViewById(R.id.record_current_streak);
        your_best_streak = Rootview.findViewById(R.id.your_best_streak);
        total_habits_done = Rootview.findViewById(R.id.total_habits_done);
        record_total_days_date = Rootview.findViewById(R.id.record_total_days_date);
        record_current_streak_date = Rootview.findViewById(R.id.record_current_streak_date);
        best_streak_days_date = Rootview.findViewById(R.id.best_streak_days_date);
        total_habits_done_date = Rootview.findViewById(R.id.total_habits_done_date);

        calender_home = Rootview.findViewById(R.id.calender_home);
        home_calender_heading = Rootview.findViewById(R.id.home_calender_heading);
        calender_home_subhead = Rootview.findViewById(R.id.calender_home_subhead);
        calender_home.setHeaderColor(R.color.white);
        calender_home.setHeaderLabelColor(R.color.black);
        calender_home.setForwardButtonImage(getResources().getDrawable(R.drawable.ic_navigate_next_black_24dp));
        calender_home.setPreviousButtonImage(getResources().getDrawable(R.drawable.ic_navigate_before_black_24dp));

        Calendar cal = Calendar.getInstance();

        List<Calendar> calendars = new ArrayList<>();
        calendars.add(cal);
//        cal.set(2020, 5, 7);
        calendars.add(cal);

        cal = Calendar.getInstance();
        month = cal.getTime().getMonth();
        SimpleDateFormat simpleMonth = new SimpleDateFormat("MMMM");
        calender_home_subhead.setText(simpleMonth.format(cal.getTime()) + " - Nothing done yet");

        getCalendarData();
        fetchStringResources();
        return Rootview;
    }

    private void getCalendarData() {
        List<EventDay> events = new ArrayList<>();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference calendarRef = db.collection("calendar/");


        calendarRef.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
//                    Log.i("documentCal", String.valueOf(document.get("events")));
                    if (document.get("events") != null) {
                        Map<String, ArrayList<String>> events_map = (Map<String, ArrayList<String>>) document.get("events");
//                    for (Object o : events) {
//                    Log.d("cal_value", "List element " + events.toString());
                        for (Map.Entry<String, ArrayList<String>> entry : events_map.entrySet()) {
                            if (entry.getKey().equals("end_date"))
                                end_date = entry.getValue();
                            else if (entry.getKey().equals("description"))
                                description = entry.getValue();
                            else if (entry.getKey().equals("title"))
                                title = entry.getValue();
                            else if (entry.getKey().equals("start_date"))
                                start_date = entry.getValue();
                        }

                        int iter = 0;
                        for (String d : title) {
                            try {
                                Date date1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").parse(start_date.get(iter));
                                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                                String year = df.format(date1);

//                            Log.i("cal_value_data", Integer.parseInt(year) + " " + date1.getMonth() + " " + date1.getDate() + " " + date1.getHours() + " " + date1.getMinutes() + " " + date1.getSeconds());

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Integer.parseInt(year), date1.getMonth(), date1.getDate(), date1.getHours(), date1.getMinutes(), date1.getSeconds());
                                events.add(new EventDay(calendar, R.drawable.applogo));
//                            events.add(new EventDay(calendar, R.drawable.applogo, Color.parseColor("#228B22")));
                                calender_home.setEvents(events);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            iter++;
                        }
                    }
                }
            }
        });

        calender_home.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                String selected_date = String.format(Locale, clickedDayCalendar.getTime().getDate());
                selected_date += "/";
                selected_date += String.format(Locale, clickedDayCalendar.getTime().getMonth() + 1);
                selected_date += "/";
                selected_date += String.valueOf(clickedDayCalendar.getTime().getYear() + 1900);
                int iter = 0;
                String events_selected_date = "";
                try {
                    Date date_selected = new SimpleDateFormat(template, java.util.Locale.ENGLISH).parse(selected_date);
                    for (String d : title) {
                        if (String.valueOf(date_selected).equals(String.valueOf(new SimpleDateFormat(template, java.util.Locale.ENGLISH).parse(start_date.get(iter))))) {
                            events_selected_date += title.get(iter) + "\n";
                            Toast.makeText(getContext(), events_selected_date, Toast.LENGTH_LONG).show();
                        }
                        iter++;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        calender_home.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat simpleMonth = new SimpleDateFormat("MMMM");
                cal.set(Calendar.YEAR, month, 1);
                cal.add(Calendar.MONTH, -1);
                month--;
                if (month < 0)
                    month = 11;
                calender_home_subhead.setText(simpleMonth.format(cal.getTime()) + " - Nothing done yet");
            }
        });
        calender_home.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat simpleMonth = new SimpleDateFormat("MMMM");
                cal.set(Calendar.YEAR, month, 1);
                cal.add(Calendar.MONTH, 1);
                month++;
                if (month > 11)
                    month = 0;
                calender_home_subhead.setText(simpleMonth.format(cal.getTime()) + " - Nothing done yet");
            }
        });
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

    private void fetchRecordResources() {
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
                                int Record1 = (d.get("Record1") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record1")));
                                int Record2 = (d.get("Record2") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record2")));
                                int Record3 = (d.get("Record3") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record3")));
                                int Record4 = (d.get("Record4") == null) ? 0 : Integer.parseInt(String.valueOf(d.get("Record4")));
                                String Update_TimeStamp = (d.get("Update_TimeStamp") == null) ? "" : String.valueOf(d.get("Update_TimeStamp"));
//                                record_total_days_numeric.setText(String.valueOf(Record1));
//                                record_current_streak_numeric.setText(String.valueOf(Record2));
//                                best_streak_days_numeric.setText(String.valueOf(Record3));
//                                total_habits_done_numeric.setText(String.valueOf(Record4));
                                if (!Update_TimeStamp.equals("")) {
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDateTime now = LocalDateTime.now();
                                    String timestamp = dtf.format(now);
                                    if (Update_TimeStamp.contains(timestamp)) {
                                        Update_TimeStamp = Update_TimeStamp.replace(timestamp, "Today");
                                    } else {
                                        Calendar cal = Calendar.getInstance();
                                        cal.add(Calendar.DATE, -1);
                                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        String timestamp_yest = dateFormat.format(cal.getTime());
                                        if (Update_TimeStamp.contains(timestamp_yest)) {
                                            Update_TimeStamp = Update_TimeStamp.replace(timestamp_yest, "Yesterday");
                                        }
                                    }
                                }
                                record_total_days_date.setText(Update_TimeStamp);
                                record_current_streak_date.setText(Update_TimeStamp);
                                best_streak_days_date.setText(Update_TimeStamp);
                                total_habits_done_date.setText(Update_TimeStamp);
                            } else {
                                Log.d("TagRecords", "Error getting documents: ", task.getException());
                            }
                    }
                });

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
        dbMain.collection("home")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    home_calender_heading.setText(String.valueOf(document.get("Calendar")));
                                    record_heading.setText(String.valueOf(document.get("Record")));
                                    total_perfect_days1.setText(String.valueOf(document.get("Record1")));
                                    record_current_streak.setText(String.valueOf(document.get("Record2")));
                                    your_best_streak.setText(String.valueOf(document.get("Record3")));
                                    total_habits_done.setText(String.valueOf(document.get("Record4")));
//                                    reminder_heading.setText(String.valueOf(document.get("Reminder_Heading")));
//                                    reminder_button.setText(String.valueOf(document.get("Reminder_Button")));
//                                    journal_heading.setText(String.valueOf(document.get("Journal_Heading")));
//                                    journal_tasks.setText(String.valueOf(document.get("Journal_Subheading")));
//                                    journal_button.setText(String.valueOf(document.get("Journal_Button")));
                                }
                            } else {
                                Log.d("TagJournal", "Error getting documents: ", task.getException());
                            }
                    }
                });

    }


}
