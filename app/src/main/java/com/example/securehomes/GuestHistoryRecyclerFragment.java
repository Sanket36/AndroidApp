package com.example.securehomes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.database.FirebaseDatabase.*;
import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class GuestHistoryRecyclerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String flat;

    private String mParam1;
    private String mParam2;

    RecyclerView recview;
    GuestHistoryAdapter adapter;

    public GuestHistoryRecyclerFragment() {
    }

    public static GuestHistoryRecyclerFragment newInstance(String param1, String param2) {
        GuestHistoryRecyclerFragment fragment = new GuestHistoryRecyclerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.i("2 param cons",flat);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.guesthistory_recycler_fragment, container, false);

       recview = (RecyclerView)view.findViewById(R.id.recView);
       recview.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Visitor>options = new FirebaseRecyclerOptions.Builder<Visitor>()
                .setQuery(getInstance().getReference().child("Visitor").orderByChild("toFlatNumber").equalTo(((SecureHomes)getActivity().getApplication()).getFlat()), Visitor.class)
                .build();
        //Log.i("After firebase",((SecureHomes)getActivity().getApplication()).getFlat());
        adapter = new GuestHistoryAdapter(options);
        recview.setAdapter(adapter);
       return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processSearch(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void processSearch(String s) {
        FirebaseRecyclerOptions<Visitor>options = new FirebaseRecyclerOptions.Builder<Visitor>()
                .setQuery(getInstance().getReference().child("Visitor").orderByChild("flatStatus").startAt(((SecureHomes)getActivity().getApplication()).getFlat()+"_"+s).endAt(((SecureHomes)getActivity().getApplication()).getFlat()+"_"+s+"\uf8ff"), Visitor.class)
                .build();
        adapter = new GuestHistoryAdapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }
}