package com.example.findlostchildren.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.findlostchildren.Constrollers.Adapters.VictimAdapter;
import com.example.findlostchildren.Models.VictimModel;
import com.example.findlostchildren.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView victimRecyclerView;
    ProgressBar progressBar;
    private VictimAdapter victimAdapter;
    private TextView noDataTV;

    private List<VictimModel> victimsArrayList = new ArrayList<>();

    //Firebase Database
    private FirebaseDatabase database;
    private DatabaseReference victimReference;

    private Context context;
    private FragmentManager fm;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        noDataTV = view.findViewById(R.id.no_data_tv);
        progressBar = view.findViewById(R.id.victims_progress_bar);
        context = getActivity().getApplicationContext();
        Context mContext = getActivity();
        fm = ((FragmentActivity) mContext).getSupportFragmentManager();
        victimRecyclerView = (RecyclerView) view.findViewById(R.id.victim_RecyclerView);
        victimRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        getVictimsData();

        return view;
    }

    private void getVictimsData() {
        progressBar.setVisibility(View.VISIBLE);
        database = FirebaseDatabase.getInstance();
        victimReference = database.getReference();


        victimReference.child("Victims").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                victimsArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                        VictimModel victimModel = childSnapShot.getValue(VictimModel.class);
                        victimsArrayList.add(victimModel);
                        victimAdapter.notifyDataSetChanged();
                    }
                }
                Collections.reverse(victimsArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (victimsArrayList != null) {
            victimAdapter = new VictimAdapter(context, victimsArrayList);
            victimRecyclerView.setAdapter(victimAdapter);
        } else {
            progressBar.setVisibility(View.GONE);
            noDataTV.setVisibility(View.VISIBLE);
        }



    }


}
