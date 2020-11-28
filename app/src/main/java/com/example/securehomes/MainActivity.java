package com.example.securehomes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
        SystemClock.sleep(3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = firebaseAuth.getCurrentUser();

        if(currentuser==null){
            Intent loginIntent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else {
            Intent mainIntent = new Intent(MainActivity.this,DashboardActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}