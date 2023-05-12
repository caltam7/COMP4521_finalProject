package com.comp4521.cdar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class DashboardActivity extends AppCompatActivity {
    UserCalendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Bundle calendarData = getIntent().getExtras();

        if (calendarData != null) {
            String currentCID = calendarData.getString("calendarID");
            // get currentCalendar
            currentCalendar = new UserCalendar(currentCID, new UserCalendar.UserCalendarLoadedCallback() {
                @Override
                public void onUserCalendarLoaded(UserCalendar calendar) {
                    //do sth with calendar (a event list)
                }
            });

        } else {
            // Handle the case where no data was passed from Login Activity
        }
    }
}