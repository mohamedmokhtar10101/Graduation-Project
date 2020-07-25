package com.example.findlostchildren.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findlostchildren.Activities.AddVictimActivity;
import com.example.findlostchildren.Activities.EditProfileActivity;
import com.example.findlostchildren.Activities.LoginActivity;
import com.example.findlostchildren.Models.UsersModel;
import com.example.findlostchildren.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    ImageView profile_imageView;
    TextView name_textView;
    TextView about_textView;
    TextView city_textView;
    TextView phone_textView;
    Button edit_button;
    ImageView add_victim_button;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase database;
    DatabaseReference reference;

    private Button signOut;

    UsersModel user;

    private Context context;
    private FragmentManager fm;

    String userName;
    String phone;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        profile_imageView = view.findViewById(R.id.profile_imageView);
        name_textView = view.findViewById(R.id.name_textView);
        about_textView = view.findViewById(R.id.about_textView);
        city_textView = view.findViewById(R.id.city_textView);
        phone_textView = view.findViewById(R.id.phone_textView);
        edit_button = view.findViewById(R.id.edit_button);
        add_victim_button = view.findViewById(R.id.add_victim_button);
        signOut = view.findViewById(R.id.sign_out_button);

        context = getActivity().getApplicationContext();
        Context mContext = getActivity();
        fm = ((FragmentActivity) mContext).getSupportFragmentManager();

        //Current User
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //get Current User ID
        String userID = firebaseUser.getUid(); //"zTK1GFINblNcrGdhF5EUB9XlE5M2";


        //addListenerForSingleValueEvent to get data to Current User only
        reference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //returned Data and save it into model named user

                userName = dataSnapshot.child("userName").getValue(String.class);
                phone = dataSnapshot.child("phone").getValue(String.class);
                String city = dataSnapshot.child("city").getValue(String.class);
                String about = dataSnapshot.child("about").getValue(String.class);
                String imageURL = dataSnapshot.child("imageURL").getValue(String.class);

                name_textView.setText(userName);
                phone_textView.setText(phone);


                if (imageURL != null) {
                    Picasso.with(context)
                            .load(imageURL)
                            .into(profile_imageView);
                }

                if (city != null) {
                    city_textView.setText(city);
                }

                if (about != null) {
                    about_textView.setText(about);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        add_victim_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddVictimActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

}
