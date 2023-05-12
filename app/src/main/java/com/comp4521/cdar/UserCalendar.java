package com.comp4521.cdar;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserCalendar {
    private String cid, ownerUid, title;
    private Map<LocalDateTime, Event> eventList;

    public UserCalendar(String cid, String uid, String title) {
        this.cid = cid;
        this.ownerUid = uid;
        this.title = title;
        this.eventList = new HashMap<>();
    }

    public UserCalendar(String uid, String title) {
        this.cid = UUID.randomUUID().toString();
        this.ownerUid = uid;
        this.title = title;
        this.eventList = new HashMap<>();
    }

    public interface UserCalendarLoadedCallback {
        void onUserCalendarLoaded(UserCalendar userCalendar);
    }

    public UserCalendar(String cidInput, UserCalendar.UserCalendarLoadedCallback callback) {
        DatabaseReference userDataRef = FirebaseDBManager.getInstance().getDatabaseRef().child("calendars").child(cidInput);
        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cid = dataSnapshot.child("cid").getValue(String.class);
                    ownerUid = dataSnapshot.child("ownerUid").getValue(String.class);
                    title = dataSnapshot.child("title").getValue(String.class);
                    GenericTypeIndicator<Map<LocalDateTime, Event>> typeIndicator = new GenericTypeIndicator<Map<LocalDateTime, Event>>() {};
                    eventList = dataSnapshot.child("eventList").getValue(typeIndicator);

                    if (callback != null) {
                        callback.onUserCalendarLoaded(UserCalendar.this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LocalDateTime millisecondsToLocalDateTime(long milliseconds) {
        Instant instant = Instant.ofEpochMilli(milliseconds);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    // Getter and setter
    public String getCid() {
        return cid;
    }
    public void putNewEvent(LocalDateTime Datetime, Event newEvent){
        eventList.put(Datetime, newEvent);
    }
}