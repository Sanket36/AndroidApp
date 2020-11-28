package com.example.securehomes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SecurityDashboard extends AppCompatActivity {
    CardView addVisitor,showVisitor;
    long maxId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_dashboard);

        addVisitor = findViewById(R.id.addVisitor);
        showVisitor = findViewById(R.id.showVisitor);

        addVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),addVisitor.class));
            }
        });
        showVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent VisitorLog = new Intent(SecurityDashboard.this, ShowVisitors.class);
                startActivity(VisitorLog);
            }
        });
    }
}