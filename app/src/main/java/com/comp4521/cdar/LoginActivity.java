package com.comp4521.cdar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText userNameInput, userPwInput;
    private Button createButton, loginButton;

    //private FirebaseDBManager fypDB = FirebaseDBManager.getInstance();
    //private DatabaseReference databaseRef = fypDB.getDatabaseRef();
    //private DatabaseReference userRef = databaseRef.child("users");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}