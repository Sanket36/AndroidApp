package com.example.securehomes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class addVisitor extends AppCompatActivity {
    private  static  final  int PERMISSION_CODE = 1000;
    private  static  final  int IMAGE_CAPTURE_CODE = 1001;
    Button btnTakePic;
    Button btnAddVisitor,btnShowVisitors;
    AutoCompleteTextView txtVehicle,txtName,txtPurpose,txtFlatNum,txtTelNum;
    ImageView showPic;
    Uri image_uri;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;
    long maxId;
    Visitor visitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);
        showPic = (ImageView) findViewById(R.id.imgView);
        btnTakePic = (Button) findViewById(R.id.btnTakePic);
        btnAddVisitor = findViewById(R.id.btnAddVisitor);
        txtName = (AutoCompleteTextView) findViewById(R.id.visitor_name);
        txtPurpose = (AutoCompleteTextView) findViewById(R.id.purpose);
        txtVehicle = (AutoCompleteTextView) findViewById(R.id.vehicle_no);
        txtFlatNum = (AutoCompleteTextView) findViewById(R.id.txtownerFlatNumber);
        txtTelNum = (AutoCompleteTextView) findViewById(R.id.visitor_telnum);
        //btnShowVisitors=(Button) findViewById(R.id.showVisitors);


        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Visitor");
        visitor = new Visitor();

        //
        /*btnShowVisitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VisitorLog = new Intent(MainActivity.this, ShowVisitors.class)
                        .putExtra("maxId",maxId);
                startActivity(VisitorLog);
            }
        });*/
        //

        btnAddVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUploader();
                FormUploader();

                Toast.makeText(addVisitor.this, "Visitor Added Successfully", Toast.LENGTH_SHORT).show();

            }
        });



        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //request for permission
                        requestPermissions(permission,PERMISSION_CODE);
                    }
                    else {
                        //permission granted
                        openCamera();
                    }
                }
                else{
                    openCamera();
                }
            }
        });

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId=snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FormUploader() {
        String name = txtName.getText().toString().trim();
        String purpose = txtPurpose.getText().toString().trim();
        String vehicle = txtVehicle.getText().toString().trim();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String toFlatNo =  txtFlatNum.getText().toString().trim();
        String imageId = System.currentTimeMillis()+"."+getExtension(image_uri);
        String phone = txtTelNum.getText().toString().trim();

        visitor.setName(name);
        visitor.setPurpose(purpose);
        visitor.setVehicle_no(vehicle);
        visitor.setTime(timeStamp);
        visitor.setToFlatNumber(toFlatNo);
        visitor.setImageId(imageId);
        visitor.setTelNum(phone);
        mDatabaseRef.child(String.valueOf(maxId)).setValue(visitor);
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileUploader() {
        String imageId;
        imageId = System.currentTimeMillis()+"."+getExtension(image_uri);

        StorageReference Ref = mStorageRef.child(imageId);
        Ref.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(addVisitor.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }


    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //Camera Intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);

    }

    //handling permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //permmission from popup
                    openCamera();
                }
                else {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //called after image captured
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //set captured img to imageView
            showPic.setImageURI(image_uri);
        }
    }
}