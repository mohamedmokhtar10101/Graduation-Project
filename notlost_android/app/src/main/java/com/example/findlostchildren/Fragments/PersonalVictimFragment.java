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
import android.widget.Toast;

import com.example.findlostchildren.Constrollers.Adapters.VictimAdapter;
import com.example.findlostchildren.Models.VictimModel;
import com.example.findlostchildren.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalVictimFragment extends Fragment {


    private RecyclerView victimRecyclerView;
    ProgressBar progressBar;
    private VictimAdapter victimAdapter;

    private FirebaseAuth auth  =FirebaseAuth.getInstance() ;
    private List<VictimModel> victimsArrayList = new ArrayList<>();

    //Firebase Database

    private DatabaseReference victimReference  = FirebaseDatabase.getInstance().getReference();

    private Context context;
    private FragmentManager fm;

    public PersonalVictimFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_victim, container, false);



        progressBar = view.findViewById(R.id.victims_progress_bar2);

        context = getActivity().getApplicationContext();

        Context mContext = getActivity();
        fm = ((FragmentActivity) mContext).getSupportFragmentManager();

        victimRecyclerView = (RecyclerView) view.findViewById(R.id.victim_RecyclerView2);
        victimRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

      check();

        return view;
    }

    private void check(){


        victimReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Victims").hasChild(auth.getCurrentUser().getUid())){

                    getVictimsData();

                }else {


                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getVictimsData() {

        progressBar.setVisibility(View.VISIBLE);




        victimReference.child("Victims").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                victimsArrayList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    victimsArrayList.add(dataSnapshot1.getValue(VictimModel.class));

                }
                victimAdapter.notifyDataSetChanged();

                Collections.reverse(victimsArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

        if (victimsArrayList != null) {
            victimAdapter = new VictimAdapter(context, victimsArrayList);
            victimRecyclerView.setAdapter(victimAdapter);
        } else
            progressBar.setVisibility(View.GONE);


    }

}