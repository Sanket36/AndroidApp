package com.example.securehomes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowVisitors extends AppCompatActivity {
    DatabaseReference dbRef;
    VisitorArrayAdapter adapter;
    Visitor visitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_visitors);
        visitor = new Visitor();
    }

    public  ArrayList<Visitor> getVisitors(){
        final ArrayList<Visitor> visList = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference("Visitor");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){
                    visitor = ds.getValue(Visitor.class);
                    visList.add(visitor);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return visList;
    }
    void displayinfo() {

        final ArrayList<Visitor> visList = getVisitors();
        adapter = new VisitorArrayAdapter(this, 0,visList);
        ListView listView = (ListView) findViewById(R.id.vistorListView);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(ShowVisitors.this, "Clicked at position: "+position, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),visitor_details.class)
                        .putExtra("visitorList", visList)
                        .putExtra("pos",position));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayinfo();
    }

}

