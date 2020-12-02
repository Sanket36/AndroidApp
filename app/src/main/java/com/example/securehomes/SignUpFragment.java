package com.example.securehomes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView alreadyhaveaccount;
    private FrameLayout parentFrameLayout;

    private EditText email;
    private EditText fullName;
    private EditText password;
    private EditText confirmPassword;
    private EditText phoneNo;
    private EditText flatNo;
    private Button signupBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private RadioGroup radioGroup;
    private RadioButton radioButton,ownerRadio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyhaveaccount = view.findViewById(R.id.already_have_an_account);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);

        email = view.findViewById(R.id.sign_up_email);
        fullName = view.findViewById(R.id.sign_up_fullname);
        password = view.findViewById(R.id.sign_up_password);
        confirmPassword = view.findViewById(R.id.sign_up_confirmpass);
        phoneNo = view.findViewById(R.id.phoneNo);
        flatNo = view.findViewById(R.id.flatNo);
        progressBar = view.findViewById(R.id.progressBar);
        signupBtn = view.findViewById(R.id.sign_up_button);
        radioGroup = view.findViewById(R.id.sign_up_radio_group);
        ownerRadio = view.findViewById(R.id.sign_in_radio_owner);

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.sign_in_radio_owner){
                    flatNo.setVisibility(View.VISIBLE);
                } else {
                    flatNo.setVisibility(View.INVISIBLE);
                }
            }
        });


        alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
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
        fullName.addTextChangedListener(new TextWatcher() {
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
        confirmPassword.addTextChangedListener(new TextWatcher() {
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

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailandpassword();
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.side_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkinputs(){
        if (!TextUtils.isEmpty(email.getText().toString())) {
            if (!TextUtils.isEmpty(fullName.getText().toString())) {
                if (!TextUtils.isEmpty(phoneNo.getText().toString())) {
                    if (!TextUtils.isEmpty(password.getText().toString()) && password.length() >= 8) {
                        if (!TextUtils.isEmpty(confirmPassword.getText().toString())) {
                            signupBtn.setEnabled(true);
                            signupBtn.setTextColor(getResources().getColor(R.color.accent));
                        } else {
                            signupBtn.setEnabled(false);
                            signupBtn.setTextColor(Color.argb(50, 255, 255, 255));
                        }
                    } else {
                        signupBtn.setEnabled(false);
                        signupBtn.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    signupBtn.setEnabled(false);
                    signupBtn.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                signupBtn.setEnabled(false);
                signupBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            signupBtn.setEnabled(false);
            signupBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }
    private void checkemailandpassword(){
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.error_icon);
        customErrorIcon.setBounds(0,0,50,50);

        if(email.getText().toString().matches(emailpattern)){
            if(password.getText().toString().equals(confirmPassword.getText().toString())){
                progressBar.setVisibility(View.VISIBLE);
                signupBtn.setEnabled(false);
                signupBtn.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            int radioInt = radioGroup.getCheckedRadioButtonId();
                            radioButton = getActivity().findViewById(radioInt);
                            Map<Object,String> userdata = new HashMap<>();
                            userdata.put("UID",firebaseAuth.getCurrentUser().getUid());
                            userdata.put("email",email.getText().toString());
                            userdata.put("fullname",fullName.getText().toString());
                            userdata.put("phone",phoneNo.getText().toString());
                            userdata.put("type",radioButton.getText().toString());
                            if(radioButton.getText().toString().equals("Owner")){
                                userdata.put("flatNo",flatNo.getText().toString());
                            }
                            firebaseDatabase.child(firebaseAuth.getCurrentUser().getUid()).setValue(userdata)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                if(radioButton.getText().toString().equals("Owner")) {
                                                    Intent mainIntent = new Intent(getActivity(), OwnerDashboard.class);
                                                    startActivity(mainIntent);
                                                    getActivity().finish();
                                                }
                                                else {
                                                    Intent mainIntent = new Intent(getActivity(), SecurityDashboard.class);
                                                    startActivity(mainIntent);
                                                    getActivity().finish();
                                                }
                                            }else{
                                                progressBar.setVisibility(View.INVISIBLE);
                                                signupBtn.setEnabled(true);
                                                signupBtn.setTextColor(getResources().getColor(R.color.accent));
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            signupBtn.setEnabled(true);
                            signupBtn.setTextColor(getResources().getColor(R.color.accent));
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                confirmPassword.setError("Password dosen't matched!",customErrorIcon);
            }
        }else{
            email.setError("Invalid Email",customErrorIcon);
        }
    }

}