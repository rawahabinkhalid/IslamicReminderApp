package com.example.habitreminder.Calender;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
//import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.habitreminder.Adapters.JournalAdapter;
import com.example.habitreminder.Data.CalendarData;
import com.example.habitreminder.Data.JournalData;
import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCalender extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    Spinner spinner_month;
    Spinner spinner_view;
    WeekView mWeekView;
    CalendarView calendarView;
    WeekView weekView;
    WeekView dayView;
    private String userID;
    public List<CalendarData> calendarDataList;
    private ArrayList<String> description = new ArrayList<>();
    private ArrayList<String> end_date = new ArrayList<>();
    private ArrayList<String> start_date = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private String Locale = "%02d";
    private String template = "dd/MM/yyyy";
    private ArrayList<WeekViewEvent> mNewEvents = new ArrayList<WeekViewEvent>();

    public FragmentCalender() {
        // Required empty public constructor
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = FragmentCalender.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
        File tokenFolder = new File(Environment.getExternalStorageDirectory() +
                File.separator + TOKENS_DIRECTORY_PATH);
        if (!tokenFolder.exists()) {
            tokenFolder.mkdirs();
        }

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(tokenFolder))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private void createCalendar() {
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
                        ArrayList<WeekViewEvent> events_week = new ArrayList<WeekViewEvent>();
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
                                calendarView.setEvents(events);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            iter++;
                        }
                    }
                }
            }
        });

        calendarView.setOnDayClickListener(new OnDayClickListener() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        View Rootview = null;
        Rootview = inflater.inflate(R.layout.fragment_calender, container, false);
        // Get a reference for the week view in the layout.

// Set an action when any event is clicked.
//        mWeekView.setOnEventClickListener(mEventClickListener);

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
//        mWeekView.setOnEventClickListener(this);

// Set long press listener for events.
//        mWeekView.setEventLongPressListener(mEventLongPressListener);

        // Inflate the layout for this fragment
        Calendar calendar = Calendar.getInstance();
        spinner_month = Rootview.findViewById(R.id.spinner_month);
        spinner_view = Rootview.findViewById(R.id.spinner_view);
        mWeekView = (WeekView) Rootview.findViewById(R.id.weekView);
        calendarView = Rootview.findViewById(R.id.calendarView);
        weekView = Rootview.findViewById(R.id.weekView);
        dayView = Rootview.findViewById(R.id.dayView);
        mWeekView.setMonthChangeListener(mMonthChangeListener);
        dayView.setMonthChangeListener(mMonthChangeListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.month_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter);
        spinner_month.setSelected(false);
        spinner_month.setSelection(calendar.getTime().getMonth(), true);
        spinner_month.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterView = ArrayAdapter.createFromResource(getContext(), R.array.View, R.layout.spinner_item);
        adapterView.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_view.setAdapter(adapterView);
        spinner_view.setSelected(false);
        spinner_view.setSelection(0, true);
        spinner_view.setOnItemSelectedListener(this);
        calendarView.setHeaderColor(R.color.white);
        calendarView.setHeaderLabelColor(R.color.black);
        calendarView.setForwardButtonImage(getResources().getDrawable(R.drawable.ic_navigate_next_black_24dp));
        calendarView.setPreviousButtonImage(getResources().getDrawable(R.drawable.ic_navigate_before_black_24dp));

        calendar.set((calendar.getTime().getYear() + 1900), calendar.getTime().getMonth(), 1);

        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        createCalendar();
        return Rootview;
    }

    //    MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
//        @Override
//        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
//            // Populate the week view with some events.
//            List<WeekViewEvent> events = getEvents(newYear, newMonth);
//            return events;
//        }
//    };
    MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // Populate the week view with some events.
//        List<WeekViewEvent> events = getEvents(newYear, newMonth);
//        return events;
            return new ArrayList<WeekViewEvent>();
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
//        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
//        Log.d("Values", String.valueOf(i));
        if (text.contains("View")) {
            if (i == 0) {
                Log.d("Values0", String.valueOf(i));
                calendarView.setVisibility(View.VISIBLE);
                dayView.setVisibility(View.GONE);
                weekView.setVisibility(View.GONE);
//            calendarView.setDate(Long.parseLong(dateFormat.format(date)));
            } else if (i == 1) {
                Log.d("Values1", String.valueOf(i));
                weekView.setVisibility(View.VISIBLE);
                calendarView.setVisibility(View.GONE);
                dayView.setVisibility(View.GONE);
            } else if (i == 2) {
                Log.d("Values2", String.valueOf(i));
                dayView.setVisibility(View.VISIBLE);
                calendarView.setVisibility(View.GONE);
                weekView.setVisibility(View.GONE);
            }
        } else {
            if (calendarView.getVisibility() == View.VISIBLE) {
                Calendar calendar = Calendar.getInstance();
                calendar.set((calendar.getTime().getYear() + 1900), i, 1);

                try {
                    calendarView.setDate(calendar);
                } catch (OutOfDateRangeException e) {
                    e.printStackTrace();
                }
//            } else if(weekView.getVisibility() == View.VISIBLE) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set((calendar.getTime().getYear() + 1900), i, 1);
//
//                try {
//                    weekView.setDate(calendar);
//                } catch (OutOfDateRangeException e) {
//                    e.printStackTrace();
//                }
//            } else if(dayView.getVisibility() == View.VISIBLE) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set((calendar.getTime().getYear() + 1900), i, 1);
//
//                try {
//                    dayView.setDate(calendar);
//                } catch (OutOfDateRangeException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
