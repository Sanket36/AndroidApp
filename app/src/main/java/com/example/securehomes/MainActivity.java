package com.example.securehomes;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
String flat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();


        String UID = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        if(user!=null) {
            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            flat = snapshot.child("flatNo").getValue(String.class);
                            Log.i("On data change",flat);
                            ((SecureHomes)getApplication()).setFlat(flat);
                            Log.i("after setting",flat);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(),"User not logged in",Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_main);
        SystemClock.sleep(1000);
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
            firebaseAuth.getCurrentUser().getUid();

            Intent mainIntent = new Intent(MainActivity.this,OwnerDashboard.class);
            startActivity(mainIntent);
            finish();
        }
    }
}
