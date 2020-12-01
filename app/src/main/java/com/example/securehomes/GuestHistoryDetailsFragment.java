package com.example.securehomes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class GuestHistoryDetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String name, purpose,vehicle_no, time, telNum, toFlatNumber, imageURL;

    public GuestHistoryDetailsFragment(String name, String purpose, String vehicle_no, String time, String telNum, String toFlatNumber, String imageURL) {
        this.name = name;
        this.purpose = purpose;
        this.vehicle_no = vehicle_no;
        this.time = time;
        this.telNum = telNum;
        this.toFlatNumber = toFlatNumber;
        this.imageURL = imageURL;
    }


    public static GuestHistoryDetailsFragment newInstance(String param1, String param2) {
        GuestHistoryDetailsFragment fragment = new GuestHistoryDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GuestHistoryDetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_history_details, container, false);
        ImageView imgholder = (ImageView)view.findViewById(R.id.imgView);
        TextView nameholder = (TextView)view.findViewById(R.id.name);
        TextView phoneholder = (TextView)view.findViewById(R.id.phone);
        TextView vehholder = (TextView)view.findViewById(R.id.vehTextView);
        TextView timeholder =(TextView) view.findViewById(R.id.TimeView);
        TextView purposeholder = (TextView)view.findViewById(R.id.purpose);
        Button call = (Button)view.findViewById(R.id.Call);

        Glide.with(imgholder.getContext()).load(imageURL).into(imgholder);
        nameholder.setText(name);
        phoneholder.setText(telNum);
        timeholder.setText(time);
        vehholder.setText(vehicle_no);
        purposeholder.setText(purpose);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + telNum));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        return  view;
    }
    public void onBackPressed(){
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new GuestHistoryRecyclerFragment()).addToBackStack(null).commit();
    }
}