package com.example.findlostchildren.Constrollers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findlostchildren.Activities.VictimActivity;
import com.example.findlostchildren.Constrollers.Holders.VictimHolder;
import com.example.findlostchildren.Models.VictimModel;
import com.example.findlostchildren.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class VictimAdapter extends RecyclerView.Adapter<VictimHolder> {

    private Context context;
    private List<VictimModel> victimModels;

    //Databae
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public VictimAdapter(Context context, List<VictimModel> victimModels) {
        this.context = context;
        this.victimModels = victimModels;
    }

    private Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public VictimHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.victim_item, parent, false);
        return new VictimHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VictimHolder holder, int position) {
        final VictimModel victimModel = victimModels.get(position);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        final String ID = victimModel.getUserId();
        reference.child("Users").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("userName").getValue(String.class);
                String imageURL = dataSnapshot.child("imageURL").getValue(String.class);

                if (name == null || name.isEmpty())
                    holder.sourceName.setText("User");
                else
                    holder.sourceName.setText(name);

                if (imageURL == null) {
                    Picasso.with(context)
                            .load(R.drawable.user)
                            .into(holder.sourceImage);
                } else {
                    Picasso.with(context)
                            .load(imageURL)
                            .into(holder.sourceImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.postTime.setText(victimModel.getPostTime());
        holder.victimDescription.setText(victimModel.getDescription());
        holder.victimName.setText(victimModel.getName());
        holder.detalisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Details Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.with(context)
                .load(victimModel.getImagesURL())
                .into(holder.victimImage);

        holder.detalisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VictimActivity.class);
                intent.putExtra("userId" , victimModel.getUserId()) ;
                intent.putExtra("ID", victimModel.getVictimId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return victimModels.size();
    }
}
