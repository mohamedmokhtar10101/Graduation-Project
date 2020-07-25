package com.example.findlostchildren.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findlostchildren.Models.Base64Image;
import com.example.findlostchildren.Models.VictimModel;
import com.example.findlostchildren.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;



public class SearchFragment extends Fragment {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference() ;
    private Uri imageUri;
    private String base64Image = "";
    private String nameForSearch = "";
    private CircleImageView imageView  , resultImage;
    private TextView resultPhone  , shape;
    private TextView resultName  ;
    private Button search ;
    private String victimId ;
    private Base64Image victimModel1 ;
    private   VictimModel victimModel ;
    private EditText editTextForName ;
    private List<VictimModel> victimsArrayList = new ArrayList<>();
    private List<VictimModel> list2 = new ArrayList<>();
    private ProgressBar progressBar ;
    private RelativeLayout parent ;



    ImageView image  , phone ;



    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);


         definitions(view);

         onClick();




        return view;
    }

    private void onClick (){

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameForSearch = editTextForName.getText().toString().trim() ;

                if(!nameForSearch.equals("") && !base64Image.equals("")){

                    search();
                }else if(! base64Image.equals("")){

                    search();



                }else if(!nameForSearch.equals("")){

                    searchByName();
                }else {

                    Toast.makeText(getContext(), "Select Image or enter name to search ", Toast.LENGTH_SHORT).show();
                }



            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(String.format("tel:%s", resultPhone.getText())));
                getActivity().startActivity(intent);
            }
        });


    }

    private void definitions( View view){
        shape = view.findViewById(R.id.shape);
        phone = view.findViewById(R.id.phoneBtn);
        progressBar = view.findViewById(R.id.progOfSearch);
        parent = view.findViewById(R.id.parentOfSearch);
        editTextForName = view.findViewById(R.id.edit_for_search_by_name);
        imageView = view.findViewById(R.id.image_search);
        image = view.findViewById(R.id.add_image);
        search = view.findViewById(R.id.search_btn);
        resultImage = view.findViewById(R.id.result_child_image);
        resultName = view.findViewById(R.id.result_child_name);
        resultPhone  =view.findViewById(R.id.result_child_phone);

    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select"), 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();

                final Uri imageUri = data.getData();
                InputStream imageStream = null;

                try {

                    imageStream = getActivity().getContentResolver().openInputStream(imageUri);

                    Log.d("BitmapImage", "onActivityResult: " + "Done");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                base64Image = encodeToBase64(selectedImage);



                Picasso.with(getContext())
                        .load(imageUri)
                        .into(imageView);

            }
        }
    }
    private static String encodeToBase64(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void search(){
        resultName.setVisibility(View.GONE);
        resultPhone.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        shape.setVisibility(View.GONE);
             parent.setVisibility(View.GONE);
             progressBar.setVisibility(View.VISIBLE);

            ref.child("Base64Images").orderByChild("base64Image").equalTo(base64Image).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               try {


                   for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                       victimModel1 = dataSnapshot1.getValue(Base64Image.class);


                   }

                   victimId = victimModel1.getVictimId();


                   getDataOfChild();

               }catch (Exception i ){


                   if (!nameForSearch.equals("")){
                      base64Image="";
                       imageView.setImageResource(R.drawable.user);

                       searchByName();


                   }else {
                       base64Image = "" ;
                       imageView.setImageResource(R.drawable.user);
                       parent.setVisibility(View.VISIBLE);
                       progressBar.setVisibility(View.GONE);
                       Toast.makeText(getContext(), "No Result", Toast.LENGTH_SHORT).show();
                   }





               }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getContext(), databaseError.getMessage()+"", Toast.LENGTH_SHORT).show();
                    parent.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            });


    }

    private void getDataOfChild(){



        ref.child("Victims").orderByChild(victimId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                    victimModel  = childSnapShot.getValue(VictimModel.class);


                    }
                }

                Picasso.with(getContext())
                        .load(victimModel.getImagesURL())
                        .into(resultImage);
                resultName.setText("Name : "+victimModel.getName());
                resultPhone.setText(victimModel.getNumber());
                phone.setVisibility(View.VISIBLE);
                parent.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                shape.setVisibility(View.VISIBLE);
                resultName.setVisibility(View.VISIBLE);
                resultPhone.setVisibility(View.VISIBLE);
                resultImage.setVisibility(View.VISIBLE);






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage()+"", Toast.LENGTH_SHORT).show();
                parent.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void searchByName(){
        resultName.setVisibility(View.GONE);
        resultPhone.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);

        phone.setVisibility(View.GONE);
        shape.setVisibility(View.GONE);
        parent.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

         list2.clear();

        ref.child("Victims").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                victimsArrayList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                            victimModel = childSnapShot.getValue(VictimModel.class);
                            victimsArrayList.add(victimModel);

                        }
                    }





                for( VictimModel a : victimsArrayList) {
                    if (a.getName().equals(nameForSearch))
                    {

                      list2.add(a);
                    }
                }
              if (list2.isEmpty()){
                   list2.clear();
                  parent.setVisibility(View.VISIBLE);
                  progressBar.setVisibility(View.GONE);
                  Toast.makeText(getContext(), "No Result", Toast.LENGTH_SHORT).show();
              }else {

                  Picasso.with(getContext())
                          .load(list2.get(0).getImagesURL())
                          .into(resultImage);
                  resultName.setText("Name : " + list2.get(0).getName());
                  resultPhone.setText(list2.get(0).getNumber());
                  phone.setVisibility(View.VISIBLE);
                  parent.setVisibility(View.VISIBLE);
                  shape.setVisibility(View.VISIBLE);
                  progressBar.setVisibility(View.GONE);
                  resultName.setVisibility(View.VISIBLE);
                  resultPhone.setVisibility(View.VISIBLE);
                  resultImage.setVisibility(View.VISIBLE);

              }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                parent.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });



    }

}
