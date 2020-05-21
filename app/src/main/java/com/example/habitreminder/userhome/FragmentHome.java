package com.example.habitreminder.userhome;


import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.habitreminder.Adapters.Main_Habits_Adapter;
import com.example.habitreminder.Adapters.ReminderAdapter;
import com.example.habitreminder.Adapters.JournalAdapterHome;
import com.example.habitreminder.Data.CalendarData;
import com.example.habitreminder.Data.HabitsData;
import com.example.habitreminder.Data.ReminderData;
import com.example.habitreminder.Data.JournalData;
import com.example.habitreminder.Data.SubHabits;
import com.example.habitreminder.Journal.WriteJournal;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.example.habitreminder.Reminders.AddCustomHabit;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

public class FragmentHome extends Fragment {
    private String user;
    String uid;
    private Button addJournal, addReminder;
    private TextView welcomeuser;
    private String personName = "";
    String UID;
    private RecyclerView myJournal_RV;
    private RecyclerView.LayoutManager layoutManager;

    public List<JournalData> journalDataList;
    public List<ReminderData> reminderDataList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseUser CurrentUser = mAuth.getCurrentUser();

    public String userID;

    public JournalAdapterHome desAdapters;
    public ReminderAdapter reminderAdapter;
    private RecyclerView myHabits_RV;
    private TextView home_calender_heading;
    private TextView record_heading;
    private TextView total_perfect_days1;
    private TextView record_current_streak;
    private TextView your_best_streak;
    private TextView total_habits_done;
    private TextView reminder_heading;
    private Button reminder_button;
    private TextView journal_heading;
    private TextView journal_tasks;
    private TextView calender_home_subhead;
    private Button journal_button;

    private TextView record_total_days_numeric;
    private TextView record_current_streak_numeric;
    private TextView best_streak_days_numeric;
    private TextView total_habits_done_numeric;

    private TextView record_total_days_date;
    private TextView record_current_streak_date;
    private TextView best_streak_days_date;
    private TextView total_habits_done_date;
    private CalendarView calender_home;
    public List<CalendarData> calendarDataList;
    private ArrayList<String> description = new ArrayList<>();
    private ArrayList<String> end_date = new ArrayList<>();
    private ArrayList<String> start_date = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private String Locale = "%02d";
    private String template = "dd/MM/yyyy";
    private int month = -1;
    private Main_Habits_Adapter myHabitsAdaper;
    private FirebaseFirestore db;
    PieChart pieChartStreaks;
    String streaks_completed_text;
    String currentStreak = "0";
    String bestStreak = "0";
    String streaks_completed_text_firebase = "";

    public FragmentHome() {

    }

    Activity activity;

    public FragmentHome(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        final FragmentManager fragmentManager;

        OnboardPreferenceManager oPm = new OnboardPreferenceManager(getContext());
        final String userName = oPm.getUserName();
        //Toast.makeText(getContext(), userName.toString(), Toast.LENGTH_SHORT).show();
        View rootview = null;
        rootview = inflater.inflate(R.layout.fragment_home, container, false);
        // journal data
        journalDataList = new ArrayList<>();
        myJournal_RV = (RecyclerView) rootview.findViewById(R.id.home_journalData);
        LinearLayoutManager ll = new LinearLayoutManager(activity);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        ll.setReverseLayout(true);
        myJournal_RV.setLayoutManager(ll);

        home_calender_heading = rootview.findViewById(R.id.home_calender_heading);
        record_heading = rootview.findViewById(R.id.record_heading);
        total_perfect_days1 = rootview.findViewById(R.id.total_perfect_days1);
        record_current_streak = rootview.findViewById(R.id.record_current_streak);
        your_best_streak = rootview.findViewById(R.id.your_best_streak);
        total_habits_done = rootview.findViewById(R.id.total_habits_done);

        record_total_days_numeric = rootview.findViewById(R.id.record_total_days_numeric);
        record_current_streak_numeric = rootview.findViewById(R.id.record_current_streak_numeric);
        best_streak_days_numeric = rootview.findViewById(R.id.best_streak_days_numeric);
        total_habits_done_numeric = rootview.findViewById(R.id.total_habits_done_numeric);

        record_total_days_date = rootview.findViewById(R.id.record_total_days_date);
        record_current_streak_date = rootview.findViewById(R.id.record_current_streak_date);
        best_streak_days_date = rootview.findViewById(R.id.best_streak_days_date);
        total_habits_done_date = rootview.findViewById(R.id.total_habits_done_date);

        reminder_heading = rootview.findViewById(R.id.reminder_heading);
       // reminder_button = rootview.findViewById(R.id.reminder_button);
        journal_heading = rootview.findViewById(R.id.journal_heading);
        journal_tasks = rootview.findViewById(R.id.journal_tasks);
        journal_button = rootview.findViewById(R.id.journal_button);
//        calender_home = rootview.findViewById(R.id.calender_home);
//        calender_home_subhead = rootview.findViewById(R.id.calender_home_subhead);
        pieChartStreaks = rootview.findViewById(R.id.streaksCompleted);
//        calender_home.setHeaderColor(R.color.white);
//        calender_home.setHeaderLabelColor(R.color.black);
//        calender_home.setForwardButtonImage(getResources().getDrawable(R.drawable.ic_navigate_next_black_24dp));
//        calender_home.setPreviousButtonImage(getResources().getDrawable(R.drawable.ic_navigate_before_black_24dp));

        Calendar cal = Calendar.getInstance();

        List<Calendar> calendars = new ArrayList<>();
        calendars.add(cal);
        cal.set(2020, 5, 7);
        calendars.add(cal);
        //calender_home.setHighlightedDays(calendars);

//        cal = Calendar.getInstance();
//        month = cal.getTime().getMonth();
//        SimpleDateFormat simpleMonth = new SimpleDateFormat("MMMM");
//        calender_home_subhead.setText(simpleMonth.format(cal.getTime()) + " - Nothing done yet");

        getHabitStatusForCalendar();
//        getCalendarData();
        db = FirebaseFirestore.getInstance();

        fetchStringResources();
      //  fetchRecordResources();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference addJournalRef = db.collection("users");

        addJournalRef.document(userID).collection("AddJournal").orderBy("timestamp", Query.Direction.DESCENDING).limit(2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.d("data1", queryDocumentSnapshots.toString());
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    Log.d(" list1", "" + list);
                    int counter = 0;
                    for (DocumentSnapshot d : list) {
                        //Log.d("data" + counter++, d.toObject(JournalData.class).getjDescription());
                        JournalData p = d.toObject(JournalData.class);
                        journalDataList.add(p);

                    }
                } else {
                    Log.d("data2", queryDocumentSnapshots.toString());
                }

                desAdapters = new JournalAdapterHome(getActivity(), journalDataList);

                myJournal_RV.setAdapter(desAdapters);
                desAdapters.notifyDataSetChanged();
            }
        });


// load data reminder
        reminderDataList = new ArrayList<>();
        myHabits_RV = (RecyclerView) rootview.findViewById(R.id.rv_habits);
//        LinearLayoutManager ll1 = new LinearLayoutManager(activity);
//        ll1.setOrientation(LinearLayoutManager.VERTICAL);
//        myHabits_RV.setLayoutManager(ll1);
        GridLayoutManager gl = new GridLayoutManager(getActivity(), 2);
        gl.setOrientation(gl.VERTICAL);
        myHabits_RV.setLayoutManager(gl);

        getHabitsAtHome();
        streakchartmethod();
     //   CollectionReference addHabits = db.collection("users");


//        addHabits.document(userID).collection("AddReminder").orderBy("defaultDate", Query.Direction.DESCENDING).limit(2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (!queryDocumentSnapshots.isEmpty()) {
//                    Log.d("data1", queryDocumentSnapshots.toString());
//                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                    Log.d(" list1", "" + list);
//                    int counter = 0;
//                    for (DocumentSnapshot d : list) {
//
//                        ReminderData p = d.toObject(ReminderData.class);
//                        reminderDataList.add(p);
//
//                    }
//                } else {
//                    Log.d("data2", queryDocumentSnapshots.toString());
//                }
//
//                reminderAdapter = new ReminderAdapter(getActivity(), reminderDataList);
//
//                myHabits_RV.setAdapter(reminderAdapter);
//                reminderAdapter.notifyDataSetChanged();
//            }
//        });
        //add journal button
        addJournal = (Button) rootview.findViewById(R.id.journal_button);
        addJournal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WriteJournal writeJournal = new WriteJournal(getActivity());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
                        .replace(R.id.mainFrame, writeJournal, "writeJournal")
                        .addToBackStack("writeJournal")
                        .commit();
            }
        });
        // add reminder button
      //  addReminder = (Button) rootview.findViewById(R.id.reminder_button);
//        addReminder.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                AddCustomHabit addHabit = new AddCustomHabit();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out)
//                        .replace(R.id.mainFrame, addHabit, "addHabit")
//                        .addToBackStack("addHabit")
//                        .commit();
//            }
//        });

        welcomeuser = rootview.findViewById(R.id.welcomeUser);
        if (oPm.isGoogleSignIn()) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                personName = acct.getDisplayName();
                welcomeuser.setText("As-salāmu ʿalaykum " + personName);
//                cPN.setUID(acct.getId());
            }
        } else if (oPm.isFacebookSignIn()) {
            welcomeuser.setText("As-salāmu ʿalaykum Facebook user");
        } else {
            //FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            cPN.setUID(mAuth.getCurrentUser().getUid());
            UID = mAuth.getCurrentUser().getUid();
            DocumentReference docRef = db.collection("users").document(UID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Data", "DocumentSnapshot data: " + document.get("timestamp"));
                            personName = document.get("name").toString();
                            welcomeuser.setText("As-salāmu ʿalaykum " + userName);
                        } else {
                            Log.d("Failed", "No such document");
                        }
                    } else {
                        Log.d("Exception", "get failed with ", task.getException());
                    }
                }
            });

        }

        return rootview;
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
            streaks_completed_text = "Streak\n\n" + currentStreak;
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
        double rem_percent = 100 - percent;
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


    private void getHabitsAtHome() {
        db.collection("habits/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<HabitsData> habitData = new ArrayList<>();
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
//                    for (DocumentSnapshot d : myListOfDocuments) {
//                        Log.i("HabitsSelect", String.valueOf(d.getId()));
//                        List<Map<String, Object>> subHabits = (List<Map<String, Object>>) d.get("SubHabits");
//                        Log.i("subHabits_Name", String.valueOf(d.getString("Name")));
//                        Log.i("subHabits_Notification", String.valueOf(d.getString("Notification")));
//                        Log.i("subHabits", String.valueOf(subHabits));
//                        HabitsData tempHabitModel = new HabitsData(d.getId(), d.getString("Name"), d.getString("Notification"), subHabits);
//                        habitData.add(tempHabitModel);
//                    }
                    for (DocumentSnapshot d : myListOfDocuments) {
                        String name = d.getString("Name");
                        String notification = d.getString("Notification");

                        List<SubHabits> subHabits = new ArrayList<>();
                        if(d.get("SubHabits") != null) {
                            List<Map<String, Object>> s = (List<Map<String, Object>>) d.get("SubHabits");
                            int index = 0;
                            for (Map<String, Object> data : s) {
                                Log.i("subHabitsData", String.valueOf(data));
                                int frequencyAdvanced = Integer.parseInt(data.get("Frequency_Advanced").toString());
                                int frequency_Beginner = Integer.parseInt(data.get("Frequency_Beginner").toString());
                                int frequency_Intermediate = Integer.parseInt(data.get("Frequency_Intermediate").toString());
                                String sub_name = data.get("Name").toString();
                                String sub_notification = data.get("Notification").toString();

                                subHabits.add(new SubHabits(frequencyAdvanced, frequency_Beginner, frequency_Intermediate, String.valueOf(index), sub_name, sub_notification));
                                index++;
                            }
                            habitData.add(new HabitsData(d.getId(), name, notification, subHabits));
                        }
                    }

                }

//            }

                myHabitsAdaper = new Main_Habits_Adapter(getActivity(), habitData);
                myHabits_RV.setAdapter(myHabitsAdaper);
                myHabitsAdaper.notifyDataSetChanged();
            }
        });


    }

    private void getHabitStatusForCalendar() {

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

//        calender_home.setOnDayClickListener(new OnDayClickListener() {
//            @Override
//            public void onDayClick(EventDay eventDay) {
//                Calendar clickedDayCalendar = eventDay.getCalendar();
//                String selected_date = String.format(Locale, clickedDayCalendar.getTime().getDate());
//                selected_date += "/";
//                selected_date += String.format(Locale, clickedDayCalendar.getTime().getMonth() + 1);
//                selected_date += "/";
//                selected_date += String.valueOf(clickedDayCalendar.getTime().getYear() + 1900);
//                int iter = 0;
//                String events_selected_date = "";
//                try {
//                    Date date_selected = new SimpleDateFormat(template, java.util.Locale.ENGLISH).parse(selected_date);
//                    for (String d : title) {
//                        if (String.valueOf(date_selected).equals(String.valueOf(new SimpleDateFormat(template, java.util.Locale.ENGLISH).parse(start_date.get(iter))))) {
//                            events_selected_date += title.get(iter) + "\n";
//                            Toast.makeText(getContext(), events_selected_date, Toast.LENGTH_LONG).show();
//                        }
//                        iter++;
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        calender_home.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
//            @Override
//            public void onChange() {
//                Calendar cal = Calendar.getInstance();
//                SimpleDateFormat simpleMonth = new SimpleDateFormat("MMMM");
//                cal.set(Calendar.YEAR, month, 1);
//                cal.add(Calendar.MONTH, -1);
//                month--;
//                if (month < 0)
//                    month = 11;
//                calender_home_subhead.setText(simpleMonth.format(cal.getTime()) + " - Nothing done yet");
//            }
//        });
//        calender_home.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
//            @Override
//            public void onChange() {
//                Calendar cal = Calendar.getInstance();
//                SimpleDateFormat simpleMonth = new SimpleDateFormat("MMMM");
//                cal.set(Calendar.YEAR, month, 1);
//                cal.add(Calendar.MONTH, 1);
//                month++;
//                if (month > 11)
//                    month = 0;
//                calender_home_subhead.setText(simpleMonth.format(cal.getTime()) + " - Nothing done yet");
//            }
//        });
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

    private void fetchStringResources() {
        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
        dbMain.collection("home")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null)
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    home_calender_heading.setText(String.valueOf(document.get("Calendar")));
//                                    record_heading.setText(String.valueOf(document.get("Record")));
//                                    total_perfect_days1.setText(String.valueOf(document.get("Record1")));
//                                    record_current_streak.setText(String.valueOf(document.get("Record2")));
//                                    your_best_streak.setText(String.valueOf(document.get("Record3")));
//                                    total_habits_done.setText(String.valueOf(document.get("Record4")));
//                                    reminder_heading.setText(String.valueOf(document.get("Reminder_Heading")));
//                                    reminder_button.setText(String.valueOf(document.get("Reminder_Button")));
                                    journal_heading.setText(String.valueOf(document.get("Journal_Heading")));
                                    journal_tasks.setText(String.valueOf(document.get("Journal_Subheading")));
                                    journal_button.setText(String.valueOf(document.get("Journal_Button")));
                                }
                            } else {
                                Log.d("TagJournal", "Error getting documents: ", task.getException());
                            }
                    }
                });

    }


}
