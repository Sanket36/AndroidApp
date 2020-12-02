package com.example.securehomes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OwnerDashboard extends AppCompatActivity {
    private CardView cardView;
    TextView acc,rej;
    int countAcc =0;
    int countRej = 0;
    String flat,flatStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);
        acc = (TextView)findViewById(R.id.accText);
        rej = (TextView)findViewById(R.id.rejText);

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Visitor");
flatStatus = ((SecureHomes) this.getApplication()).getFlat() + "_"+"Rejected";
        Log.i("name",flatStatus);
        rootRef.orderByChild("flatStatus").equalTo(flatStatus)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       // String status = (String) snapshot.child("status").getValue();
                        //Log.i("msg", String.valueOf(snapshot.child("status").getValue()));
                       // if(status.equalsIgnoreCase("Accepted")) countAcc++;
                        //else if(status.equalsIgnoreCase("Rejected")) countRej++;
                        Log.i("key", String.valueOf(snapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
acc.setText(String.valueOf(countAcc));
rej.setText(String.valueOf(countRej));
//        //cardView = findViewById(R.id.card1);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//            }
//        });

        CardView visHis = (CardView)findViewById(R.id.visHis);
        visHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent visIntent = new Intent(OwnerDashboard.this, GuestHistoryWrapper.class);
                startActivity(visIntent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        MenuItem item = menu.findItem(R.id.profile);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent profile = new Intent(OwnerDashboard.this,Profile.class);
                startActivity(profile);
                //Toast.makeText(DashboardActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}