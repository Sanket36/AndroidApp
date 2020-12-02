package com.example.securehomes;

        import android.Manifest;
        import android.app.ProgressDialog;
        import android.content.ContentResolver;
        import android.content.ContentValues;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.util.Log;
        import android.view.View;
        import android.webkit.MimeTypeMap;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.cardview.widget.CardView;

        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.OnProgressListener;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.StorageTask;
        import com.google.firebase.storage.UploadTask;

        import org.w3c.dom.Text;

public class Profile extends AppCompatActivity implements BottomSheetProfile.BottomSheetListener {
    private static final int PERMISSION_CODE= 1000;
    private  static  final  int IMAGE_CAPTURE_CODE = 1001;
    StorageReference mStorageRefernce;
    public Uri imguri;
    private StorageTask uploadTask;
    ImageView clicked_image_id;
    Button save;
    private int requestCode;
    private int resultCode;
    // OwnerModel user;
    ImageView profile;
    FirebaseUser user;
    TextView name,phone,flatNo,email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mStorageRefernce = FirebaseStorage.getInstance().getReference("Images/");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        name = (TextView)findViewById(R.id.name);
        phone = (TextView)findViewById(R.id.phone);
        flatNo = (TextView)findViewById(R.id.flatNo);
        email = (TextView)findViewById(R.id.email);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        if(user!=null) {
            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            flatNo.setText(snapshot.child("flatNo").getValue(String.class));
                            name.setText(snapshot.child("fullname").getValue(String.class));
                            phone.setText(snapshot.child("phone").getValue(String.class));
                            email.setText(snapshot.child("email").getValue(String.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(),"User not logged in",Toast.LENGTH_SHORT).show();
        }


        user = FirebaseAuth.getInstance().getCurrentUser();
        CardView profilePhoto = (CardView)findViewById(R.id.profilePhoto);
        clicked_image_id = (ImageView)findViewById(R.id.profile);
        save = (Button) findViewById(R.id.save);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetProfile bottomSheetProfile = new BottomSheetProfile();
                bottomSheetProfile.show(getSupportFragmentManager(),"exampleBottomSheet");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"called",Toast.LENGTH_SHORT).show();
                Fileuplader();

            }
        });
    }
    @Override
    public void onButtonClicked(String text) {
        if(text.equals("Camera")){
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
            Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
        }
        else{
            if(uploadTask!=null && uploadTask.isInProgress()){
                Toast.makeText(getApplicationContext(),"Upload in Progress",Toast.LENGTH_SHORT).show();
            }else {
                Filechooser();
            }
        }
    }
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void Fileuplader(){
        String imageId;
        imageId = System.currentTimeMillis()+"."+getExtension(imguri);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        //user = new OwnerModel();

        final StorageReference Ref = mStorageRef.child(imageId);

        uploadTask = (UploadTask) Ref.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Profile Added Successfully",Toast.LENGTH_SHORT).show();
                //
                Task<Uri> downloadURL = Ref.getDownloadUrl();
                downloadURL.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageReference = uri.toString();
                        mDatabaseRef.child("Users").child(user.getUid()).child("profile").setValue(imageReference);

                    }
                });
                //
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                dialog.setMessage("Uploaded :"+(int)percent+"%");
            }
        });

        //
    }

    private void Filechooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }



    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        imguri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //Camera Intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imguri);
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
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri = data.getData();
            clicked_image_id.setImageURI(imguri);
        }
    }
}
