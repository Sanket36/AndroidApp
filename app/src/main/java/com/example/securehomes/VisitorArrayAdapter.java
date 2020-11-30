package com.example.securehomes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class VisitorArrayAdapter extends ArrayAdapter<Visitor> {


    public VisitorArrayAdapter(Activity context, int resource, @NonNull ArrayList<Visitor> vis) {
        super(context, resource , vis);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView = convertView;
        if(listView==null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.visitor_list_item,parent,false);
        }

        Visitor CurrentVisitor = getItem(position);

        TextView Name = (TextView) listView.findViewById(R.id.name);
        Name.setText(String.valueOf(CurrentVisitor.getName()));

        TextView Status = (TextView) listView.findViewById(R.id.purpose);
        Status.setText(CurrentVisitor.getPurpose());

        ImageView img = (ImageView) listView.findViewById(R.id.imgView);
        Glide.with(getContext())
                .load(CurrentVisitor.getImageURL())
                .into(img);
        //Glide.with(getContext())
                //.load("https://www.tutorialspoint.com/images/tp-logo-diamond.png")
                //.into(img);


        return listView;
    }
}


