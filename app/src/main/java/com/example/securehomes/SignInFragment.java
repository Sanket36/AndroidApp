package com.example.securehomes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAccount;
    private FrameLayout parentFrameLayout;
    private EditText email,password;
    private Button signinBtn;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        dontHaveAccount = view.findViewById(R.id.already_have_an_account);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);

        email = view.findViewById(R.id.sign_in_email);
        password = view.findViewById(R.id.sign_in_password);
        signinBtn = view.findViewById(R.id.sign_in_button);
        progressBar = view.findViewById(R.id.progressBar2);
        radioGroup = view.findViewById(R.id.sign_in_radio_group);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailpassword();
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkinputs(){
        if (!TextUtils.isEmpty(email.getText().toString())){
            if(!TextUtils.isEmpty(password.getText().toString())){
                signinBtn.setEnabled(true);
                signinBtn.setTextColor(Color.rgb(255,255,255));
            }else{
                signinBtn.setEnabled(false);
                signinBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            signinBtn.setEnabled(false);
            signinBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private void checkemailpassword(){
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if(email.getText().toString().matches(emailpattern)){
            if (password.length() >= 8){

                progressBar.setVisibility(View.VISIBLE);
                signinBtn.setEnabled(false);
                signinBtn.setTextColor(Color.argb(50,255,255,255));
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = getActivity().findViewById(radioId);

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    firebaseDatabase.child(firebaseAuth.getCurrentUser().getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    Log.i("object", String.valueOf(snapshot));
                                                    if(snapshot.child("type").getValue().toString().equals(radioButton.getText().toString())) {
                                                        if(snapshot.child("type").getValue().toString().equals("Owner")) {
                                                            Intent mainIntent = new Intent(getActivity(), OwnerDashboard.class);
                                                            startActivity(mainIntent);
                                                            getActivity().finish();
                                                        } else {
                                                            Intent mainIntent = new Intent(getActivity(), SecurityDashboard.class);
                                                            startActivity(mainIntent);
                                                            getActivity().finish();
                                                        }
                                                    } else{
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        signinBtn.setEnabled(true);
                                                        signinBtn.setTextColor(Color.rgb(255,255,255));
                                                        FirebaseAuth.getInstance().signOut();
                                                        Toast.makeText(getActivity(),"No Such User",Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                }else{
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signinBtn.setEnabled(true);
                                    signinBtn.setTextColor(Color.rgb(255,255,255));
                                    String error =task.getException().getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(getActivity(),"Incorrect Email or password!",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(),"Incorrect Email or password!",Toast.LENGTH_SHORT).show();
        }
    }
}