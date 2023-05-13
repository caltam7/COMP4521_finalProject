package com.comp4521.cdar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import static com.comp4521.cdar.User.RESOURCES_ENTITY;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    UserCalendar currentCalendar;
    private Button pendingButton,submitButton;
    private EditText startTimeInput, endTimeInput;
    private Spinner resSelector;
    private List<String> res;
    private String currentPermit, currentCID, currentUID;

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

        resSelector = findViewById(R.id.RE_list);
        startTimeInput = findViewById(R.id.startTime_input);
        endTimeInput = findViewById(R.id.endTime_input);
        pendingButton = findViewById(R.id.pending_btn);
        submitButton = findViewById(R.id.submit_btn);

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


        pendingButton.setOnClickListener(view -> {
            //jump to PendingRequests intent
            Intent intent = new Intent(DashboardActivity.this, PendingRequestsActivity.class);
            startActivity(intent);
        });

        submitButton.setOnClickListener(view -> {
            //get user input for new request
            String startTime = startTimeInput.toString().trim();
            String endTime = endTimeInput.toString().trim();
            String res = resSelector.getSelectedItem().toString().trim();

        });
    }
}