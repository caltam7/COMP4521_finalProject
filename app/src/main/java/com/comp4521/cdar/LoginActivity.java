package com.comp4521.cdar;

import static com.comp4521.cdar.User.CLIENT;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText emailInput, userPwInput, cidInput;
    private Button createButton, loginButton;
    private Spinner userPermissionInput;
    private DatabaseReference databaseRef = FirebaseDBManager.getInstance().getDatabaseRef();
    private DatabaseReference userRef = databaseRef.child("users");;

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

        // Sign Up New User
        // Adding onClickListener to sign up button
        createButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = userPwInput.getText().toString().trim();
            String permit = userPermissionInput.getSelectedItem().toString().trim();

            // Call createUserWithEmailAndPassword() with the user's email and password
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign up success
                            FirebaseUser userAuth = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();

                            // Use the uid from Auth as the ID for the new user
                            User newUser = new User(userAuth.getUid(), email, email, permit, cidInput.toString().trim());
                            String msg = "";
                            Log.d(msg, newUser.toString());
                            // Save the new user to the database
                            userRef.child(permit).child(userAuth.getUid()).setValue(newUser);
                            // Save current user's id to bundle
                            //userData.putString("uid", userAuth.getUid());

                            //jump to dashboard
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            //intent.putExtras(userData);
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
            String email = emailInput.getText().toString().trim();
            String password = userPwInput.getText().toString().trim();

            // Call signInWithEmailAndPassword() with the user's email and password
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Do something with the signed-in user
                            Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                            // creating new Intent and starting next activity
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                        } else {
                            // Sign in failed
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}