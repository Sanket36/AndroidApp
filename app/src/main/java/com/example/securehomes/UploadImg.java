package com.example.securehomes;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class UploadImg extends AppCompatActivity {
    Button upload,browse;
    ImageView img;
    StorageReference mStorageRefernce;
    public Uri imguri;
    private StorageTask uploadTask;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imgupload);

        mStorageRefernce = FirebaseStorage.getInstance().getReference("Images/");
        upload = (Button)findViewById(R.id.upload);
        browse = (Button)findViewById(R.id.browse);
        img = (ImageView)findViewById(R.id.imgView);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fileuplader();
            }
        });
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(),"Upload in Progress",Toast.LENGTH_SHORT).show();
                }else {
                    Filechooser();
                }
            }
        });
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void Fileuplader(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();
        StorageReference Ref = mStorageRefernce.child(System.currentTimeMillis()+"."+getExtension(imguri));

        uploadTask =Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Image Uploaded successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });

    }

    private void Filechooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri = data.getData();
            img.setImageURI(imguri);
        }
    }
}
