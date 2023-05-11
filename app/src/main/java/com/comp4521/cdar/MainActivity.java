package com.comp4521.cdar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private String msg = "DEBUG: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Log.d(msg, "Main");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}