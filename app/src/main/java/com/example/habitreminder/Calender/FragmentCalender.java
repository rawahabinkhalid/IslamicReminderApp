package com.example.habitreminder.Calender;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.habitreminder.R;
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
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCalender extends Fragment implements AdapterView.OnItemSelectedListener{

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
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static void createCalendar() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Log.d("asd", "in createCalendar");
//        Events events = service.events().list("primary")
//                .setMaxResults(10)
//                .setTimeMin(now)
//                .setOrderBy("startTime")
//                .setSingleEvents(true)
//                .execute();
//        List<Event> items = events.getItems();
//        if (items.isEmpty()) {
//            System.out.println("No upcoming events found.");
//        } else {
//            System.out.println("Upcoming events");
//            for (Event event : items) {
//                DateTime start = event.getStart().getDateTime();
//                if (start == null) {
//                    start = event.getStart().getDate();
//                }
//                System.out.printf("%s (%s)\n", event.getSummary(), start);
//            }
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Rootview = null;
        Rootview = inflater.inflate(R.layout.fragment_calender, container,false);
        // Get a reference for the week view in the layout.

// Set an action when any event is clicked.
//        mWeekView.setOnEventClickListener(mEventClickListener);

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
//        mWeekView.setOnEventClickListener(this);

// Set long press listener for events.
//        mWeekView.setEventLongPressListener(mEventLongPressListener);

        // Inflate the layout for this fragment
        spinner_month = Rootview.findViewById(R.id.spinner_month);
        spinner_view = Rootview.findViewById(R.id.spinner_view);
        mWeekView = (WeekView) Rootview.findViewById(R.id.weekView);
        calendarView = Rootview.findViewById(R.id.calendarView);
        weekView = Rootview.findViewById(R.id.weekView);
        dayView = Rootview.findViewById(R.id.dayView);
                mWeekView.setMonthChangeListener(mMonthChangeListener);
        dayView.setMonthChangeListener(mMonthChangeListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.month_array,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter);
        spinner_month.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterView = ArrayAdapter.createFromResource(getContext(),R.array.View,R.layout.spinner_item);
        adapterView.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_view.setAdapter(adapterView);
        spinner_view.setOnItemSelectedListener(this);
        try {
            createCalendar();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
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
        Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
//        Log.d("Values", String.valueOf(i));
        if(i == 0)
        {
            Log.d("Values0", String.valueOf(i));

//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
//            Date date = new Date();
            calendarView.setVisibility(View.VISIBLE);
            dayView.setVisibility(View.GONE);
            weekView.setVisibility(View.GONE);
//            calendarView.setDate(Long.parseLong(dateFormat.format(date)));
        }
        else if(i == 1)
        {
            Log.d("Values1", String.valueOf(i));
            weekView.setVisibility(View.VISIBLE);
            calendarView.setVisibility(View.GONE);
            dayView.setVisibility(View.GONE);




        }
        else if(i == 2)
        {
            Log.d("Values2", String.valueOf(i));
            dayView.setVisibility(View.VISIBLE);
            calendarView.setVisibility(View.GONE);
            weekView.setVisibility(View.GONE);


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
