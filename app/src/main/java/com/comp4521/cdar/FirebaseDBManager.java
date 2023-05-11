package com.comp4521.cdar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDBManager {
    private static FirebaseDBManager instance;
    private DatabaseReference dBRef;

    FirebaseDBManager() {
        dBRef = FirebaseDatabase.getInstance("https://comp4521project-smartcalendar-default-rtdb.firebaseio.com/").getReference();
    }

    public static synchronized FirebaseDBManager getInstance() {
        if (instance == null) {
            instance = new FirebaseDBManager();
        }
        return instance;
    }

    public DatabaseReference getDatabaseRef() {
        return dBRef;
    }

    public void writeNewCalendar(Calendar cdar) {
        DatabaseReference newCdarRef = dBRef.child("calendars").child(cdar.getCid());
        newCdarRef.setValue(cdar);
    }

    public void readCalendar(String cid, ValueEventListener listener) {
        DatabaseReference newCdarRef = dBRef.child("calendars").child(cid);
        newCdarRef.addValueEventListener(listener);
    }

    public void writeUserData(String uid, User userData){
        DatabaseReference userDataRef = dBRef.child("users").child(uid);
        userDataRef.setValue(userData);
    }

    public void readUserData(String uid, ValueEventListener listener){
        DatabaseReference userDataRef = dBRef.child("users").child(uid);
        userDataRef.addValueEventListener(listener);
    }
}