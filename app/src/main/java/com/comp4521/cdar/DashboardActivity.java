package com.comp4521.cdar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import static com.comp4521.cdar.User.RESOURCES_ENTITY;
import static com.comp4521.cdar.User.SERVICE_PROVIDER;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    UserCalendar currentCalendar;
    private CalendarView MainCalendar;
    private Button pendingButton,submitButton,todayButton;
    private EditText titleInput, startTimeInput, endTimeInput;
    private Spinner resSelector;
    private List<String> res;
    private String currentPermit, currentCID, currentUID, atCalendarID;
    private LocalDate selectedDate = LocalDate.now();
    private DatabaseReference currentEventsRef, atCalendarRef, usersSpRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Bundle calendarData = getIntent().getExtras();
        if (calendarData != null) {
            currentCID = calendarData.getString("calendarID");
            currentUID = calendarData.getString("currentUid");
            currentPermit = calendarData.getString("permit");
            String msg="CID/UID Passing Check";
            Log.d(msg, currentCID);
            Log.d(msg, currentUID);
            if (currentPermit.equals(SERVICE_PROVIDER))
                currentEventsRef = FirebaseDBManager.getInstance().getDatabaseRef().child("calendars").child(currentUID).child("events");
            else {
                atCalendarRef = FirebaseDBManager.getInstance().getDatabaseRef().child("users").child(currentPermit).child(currentUID).child("atCalendar");
                atCalendarRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            atCalendarID = snapshot.getValue(String.class);
                            // Do something with the atCalendar value
                            String msgg = "atCalendarID";
                            Log.d(msgg, "atCalendar value: " + atCalendarID);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors here
                    }
                });
                usersSpRef = FirebaseDBManager.getInstance().getDatabaseRef().child("users").child(SERVICE_PROVIDER);
                usersSpRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot permitSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot userSnapshot : permitSnapshot.getChildren()) {
                                String msggg="searchedUID";
                                String ownCalendar="";
                                if (userSnapshot.getKey().equals("ownCalendar")) {
                                    Log.d(msggg, "FOUND CID: " + userSnapshot.getValue());
                                    ownCalendar = userSnapshot.getValue().toString().trim();
                                }
                                if (ownCalendar != null && ownCalendar.equals(atCalendarID)) {
                                    String searchedUid = permitSnapshot.getKey(); //title of this user directory = target uid
                                    // Do something with the matching uid value
                                    Log.d(msggg, "Matching uid found: " + searchedUid);
                                    currentEventsRef = FirebaseDBManager.getInstance().getDatabaseRef().child("calendars").child(searchedUid).child("events");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors here
                    }
                });
            }
        } else {
            // Handle the case where no data was passed from Login Activity
        }

        MainCalendar = findViewById(R.id.calendar_id);
        resSelector = findViewById(R.id.RE_list);
        titleInput = findViewById(R.id.title_input);
        startTimeInput = findViewById(R.id.startTime_input);
        endTimeInput = findViewById(R.id.endTime_input);
        pendingButton = findViewById(R.id.pending_btn);
        submitButton = findViewById(R.id.submit_btn);
        todayButton = findViewById(R.id.today_btn);

        //prepare RE list for spinner
        DatabaseReference databaseRef = FirebaseDBManager.getInstance().getDatabaseRef();
        DatabaseReference resRef = databaseRef.child("users").child(RESOURCES_ENTITY);
        resRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> dataList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String resName = data.child("username").getValue(String.class);
                    dataList.add(resName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(DashboardActivity.this, android.R.layout.simple_spinner_item, dataList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                resSelector.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        MainCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Create a Calendar instance for the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Convert the Calendar instance to a LocalDate
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth);

                // Do something with the selected date
                String msg="select_Date";
                Log.d(msg, "Selected date: " + selectedDate.toString());
                calendarData.putString("selectedDate", selectedDate.toString());
            }
        });


        pendingButton.setOnClickListener(view -> {
            //jump to PendingRequests intent
            Intent intent = new Intent(DashboardActivity.this, PendingRequestsActivity.class);
            intent.putExtras(calendarData);
            startActivity(intent);
        });

        submitButton.setOnClickListener(view -> {
            //get user input for new request
            String bookingTitle = titleInput.getText().toString().trim();
            String startTime = startTimeInput.getText().toString().trim();
            String endTime = endTimeInput.getText().toString().trim();
            String res = resSelector.getSelectedItem().toString().trim();

            Event newEvent = new Event(bookingTitle, res, selectedDate, stringToLocalTime(startTime), stringToLocalTime(endTime),Event.PENDING);
            currentEventsRef.child(selectedDate.toString()+"_"+newEvent.getStartTime_str()).setValue(newEvent);
            calendarData.putString("eventsRef", currentEventsRef.toString());
        });

        todayButton.setOnClickListener(view -> {
            MainCalendar.setDate(System.currentTimeMillis());
        });
    }

    public String searchResIDByName(String resName){
        return "";
    }

    public static LocalTime stringToLocalTime(String time) {
        // Create a formatter for the input time string
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Parse the input time string to a LocalTime object
        LocalTime localTime = LocalTime.parse(time, inputFormatter);

        String Msg="TimeConvertor";
        Log.d(Msg, time);
        Log.d(Msg, localTime.toString());
        return localTime;
    }

}