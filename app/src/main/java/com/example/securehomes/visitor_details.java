package com.example.securehomes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class visitor_details extends AppCompatActivity {
    Button showDetails;
    ArrayList<Visitor> visList;
    Visitor vis;
    int position;
    TextView txtName,txtPurpose,txtPhone,txtToFlatNum,txtTime,txtVehicleNum;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_details);


        txtName = findViewById(R.id.personName);
        txtPurpose = findViewById(R.id.purpose);
        imageView = findViewById(R.id.imgView);
        txtPhone = findViewById(R.id.txtTelNum);
        txtTime = findViewById(R.id.txtInDateTime);
        txtToFlatNum = findViewById(R.id.txtFlatNum);
        txtVehicleNum = findViewById(R.id.txtVehicleNum);

        visList = new ArrayList<Visitor>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position= bundle.getInt("pos",0);

        visList = (ArrayList) bundle.getParcelableArrayList("visitorList");
        vis = visList.get(position);

        String name = vis.getName();
        String purpose = vis.getPurpose();
        String time = vis.getTime();
        String vehicleNum = vis.getVehicle_no();
        String telNum = vis.getTelNum();
        String toFlatNum = vis.getToFlatNumber();

        Glide.with(this)
                .load(vis.getImageURL())
                .into(imageView);

        txtName.setText(name);
        txtPurpose.setText(purpose);
        txtVehicleNum.setText(vehicleNum);
        txtTime.setText(time);
        txtPhone.setText(telNum);
        txtToFlatNum.setText(toFlatNum);



    }


}
