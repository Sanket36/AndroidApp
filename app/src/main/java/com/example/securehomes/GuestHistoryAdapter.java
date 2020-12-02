package com.example.securehomes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class GuestHistoryAdapter extends FirebaseRecyclerAdapter<Visitor, GuestHistoryAdapter.viewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GuestHistoryAdapter(@NonNull FirebaseRecyclerOptions<Visitor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final viewHolder holder, final int position, @NonNull final Visitor model) {
        holder.name.setText(model.getName());
        holder.status.setText(model.getStatus());
        Glide.with(holder.img.getContext()).load(model.getImageURL()).into(holder.img);
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new GuestHistoryDetailsFragment(model.getName(), model.getPurpose(),model.getVehicle_no(),model.getTime(), model.getTelNum(),model.getToFlatNumber(),model.getImageURL())).addToBackStack(null).commit();
            }
        });
        if(model.getStatus().equalsIgnoreCase("Accepted") || model.getStatus().equalsIgnoreCase("Rejected")){
            holder.acc.setVisibility(View.GONE);
            holder.rej.setVisibility(View.GONE);
        }


        holder.acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Visitor").child(getRef(position).getKey()).child("status").setValue("Accepted");
            }
        });
        holder.rej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Visitor").child(getRef(position).getKey()).child("status").setValue("Rejected");
            }
        });

    }

    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_history_item,parent,false);

       return new viewHolder(view);
    }

    static class viewHolder extends RecyclerView.ViewHolder{

       ImageView img;
       TextView name;
       TextView status;
       Button details,acc,rej;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.imgView);
            name = (TextView)itemView.findViewById(R.id.nameView);
            status = (TextView)itemView.findViewById(R.id.statusView);
            details = (Button)itemView.findViewById(R.id.detailsView);
            acc = (Button)itemView.findViewById(R.id.Accepted);
            rej = (Button)itemView.findViewById(R.id.Rejected);
        }

    }

}
