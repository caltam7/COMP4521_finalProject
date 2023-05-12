package com.comp4521.cdar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    UserCalendar currentCalendar;
    private Button pendingButton;
    private Spinner resSelector;
    private List<String> res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Bundle calendarData = getIntent().getExtras();

        resSelector = findViewById(R.id.RE_list);
        pendingButton = findViewById(R.id.pending_btn);

        pendingButton.setOnClickListener(view -> {
            //jump to PendingRequests intent
            Intent intent = new Intent(DashboardActivity.this, PendingRequestsActivity.class);
            startActivity(intent);
        });

        if (calendarData != null) {
            String currentCID = calendarData.getString("calendarID");
            String currentUID = calendarData.getString("currentUid");
            String msg="CID/UID Passing Check";
            Log.d(msg, currentCID);
            Log.d(msg, currentUID);
            // get currentCalendar
            //currentCalendar = new UserCalendar(currentCID, new UserCalendar.UserCalendarLoadedCallback() {
            //    @Override
            //    public void onUserCalendarLoaded(UserCalendar calendar) {
                    //do sth with calendar (a event list)
            //    }
            //});

        } else {
            // Handle the case where no data was passed from Login Activity
        }
    }
}