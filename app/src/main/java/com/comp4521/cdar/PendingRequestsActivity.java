package com.comp4521.cdar;

import static com.comp4521.cdar.User.SERVICE_PROVIDER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PendingRequestsActivity extends AppCompatActivity {
    private String currentPermit, currentCID, currentUID, atCalendarID, selectedDate;
    private DatabaseReference currentEventsRef, atCalendarRef, usersSpRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        Bundle calendarData = getIntent().getExtras();
        if (calendarData != null) {
            currentCID = calendarData.getString("calendarID");
            currentUID = calendarData.getString("currentUid");
            currentPermit = calendarData.getString("permit");
            selectedDate = calendarData.getString("selectedDate");
            currentEventsRef = FirebaseDatabase.getInstance().getReferenceFromUrl(calendarData.getString("eventsRef"));
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
                                if (userSnapshot.getKey().equals("ownCalendar"))
                                    Log.d(msggg, "FOUND CID: " + userSnapshot.getValue());
                                ownCalendar = userSnapshot.getValue().toString().trim();
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



    }
}