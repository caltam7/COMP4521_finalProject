package com.comp4521.cdar;

import static com.comp4521.cdar.User.SERVICE_PROVIDER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText emailInput, userPwInput, cidInput;
    private Button createButton, loginButton;
    private Spinner userPermissionInput;
    private DatabaseReference databaseRef = FirebaseDBManager.getInstance().getDatabaseRef();
    private DatabaseReference userRef = databaseRef.child("users");
    private DatabaseReference calendarRef = databaseRef.child("calendars");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Assign ViewIds to var
        userPermissionInput = findViewById(R.id.role_select);
        cidInput = findViewById(R.id.cid_input);
        emailInput = findViewById(R.id.email_input);
        userPwInput = findViewById(R.id.password_input);
        createButton = findViewById(R.id.signUp_btn);
        loginButton = findViewById(R.id.login_btn);

        //dynamically show cidInput
        userPermissionInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (!selected.equals(SERVICE_PROVIDER)) {
                    cidInput.setVisibility(View.VISIBLE); // show the EditText
                } else {
                    cidInput.setVisibility(View.GONE); // hide the EditText
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {cidInput.setVisibility(View.GONE);}
        });

        Bundle calendarData = new Bundle();

        // Sign Up New User
        // Adding onClickListener to sign up button
        createButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim() + "@cdar.com";
            String password = userPwInput.getText().toString().trim();
            String permit = userPermissionInput.getSelectedItem().toString().trim();
            String cid = cidInput.toString().trim();

            // Call createUserWithEmailAndPassword() with the user's email and password
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (inputValidation(cid) && task.isSuccessful()) {
                            // Sign up success
                            FirebaseUser userAuth = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();

                            // Use the uid from Auth as the ID for the new user
                            User newUser = new User(userAuth.getUid(), email, permit);
                            // Save the new user to the database
                            userRef.child(permit).child(userAuth.getUid()).setValue(newUser);

                            // if creating a SP account, create new calendar too
                            if (permit.equals(SERVICE_PROVIDER)){
                                UserCalendar newCalendar = new UserCalendar(userAuth.getUid(),newUser.getUsername()+"'s Calendar");
                                String msg="newCDAR:";
                                Log.d(msg, String.valueOf(newCalendar));
                                calendarRef.child(userAuth.getUid()).setValue(newCalendar);
                                Event createdAt = new Event("Created At", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "n/a", "n/a", "n/a", Event.APPROVED);
                                calendarRef.child(userAuth.getUid()).child("events").child(createdAt.getStartTime_str()).setValue(createdAt);
                                calendarData.putString("calendarID", newCalendar.getCid());
                            }
                            else {
                                calendarData.putString("calendarID", cid);
                            }
                            calendarData.putString("currentUid", userAuth.getUid());
                            calendarData.putString("permit", permit);

                            //jump to dashboard intent
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtras(calendarData);
                            startActivity(intent);
                        } else {
                            // Sign up failed
                            Toast.makeText(LoginActivity.this, "Account creation failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Login Current User
        // Set click listener for the login button
        loginButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim() + "@cdar.com";
            String password = userPwInput.getText().toString().trim();
            String permit = userPermissionInput.getSelectedItem().toString().trim();
            String cid = cidInput.toString().trim();

            // Call signInWithEmailAndPassword() with the user's email and password
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser userAuth = mAuth.getCurrentUser();
                            calendarData.putString("currentUid", userAuth.getUid());
                            calendarData.putString("permit", permit);
                            String msgg="signed In";
                            Log.d(msgg, userAuth.getUid());
                            // Do something with the signed-in user
                            Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                            // if SP, goto own calendar, else, specified by cidInput
                            if (permit.equals(SERVICE_PROVIDER)){
                                Task<DataSnapshot> cidTask  = calendarRef.child(userAuth.getUid()).get();
                                cidTask.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        String msg = "cidTask";
                                        if (task.isSuccessful()) {
                                            String cid = task.getResult().child("cid").getValue().toString().trim();
                                            Log.d(msg, cid);
                                            calendarData.putString("calendarID", cid);

                                            // creating new Intent and starting next activity
                                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                            intent.putExtras(calendarData);
                                            startActivity(intent);
                                        } else {
                                            Log.d(msg, "cidTask not completed");
                                        }
                                    }
                                });
                            }
                            else {
                                calendarData.putString("calendarID", cid);
                                // creating new Intent and starting next activity
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                intent.putExtras(calendarData);
                                startActivity(intent);
                            }
                        } else {
                            // Sign in failed
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }

    public boolean inputValidation(String cid){
        return true;
    }

}