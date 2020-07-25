package com.example.findlostchildren.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.findlostchildren.Fragments.ProfileFragment;
import com.example.findlostchildren.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteInformationActivity extends AppCompatActivity {

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.add_image_btn)
    ImageView addImageBtn;
    @BindView(R.id.edit_city)
    EditText editCity;
    @BindView(R.id.edit_age)
    EditText editAge;
    @BindView(R.id.edit_facebook_link)
    EditText editFacebookLink;
    @BindView(R.id.edit_about)
    EditText editAbout;
    @BindView(R.id.skip_imageView)
    ImageView skipImageView;
    @BindView(R.id.done_imageView)
    ImageView done_imageView;


    boolean check = false  ;
    Uri imageUri;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private String userId, userEmail;
    String userName, phone, city, age, facebookLink, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_information);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = mAuth.getCurrentUser();
        userId = user.getUid(); //"zTK1GFINblNcrGdhF5EUB9XlE5M2";
        userEmail = user.getEmail(); //"m@m.com";

    }

    @OnClick({R.id.profile_image, R.id.add_image_btn, R.id.skip_imageView,R.id.done_imageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_image:
                break;
            case R.id.add_image_btn:
                selectImage();
                break;
            case R.id.skip_imageView:
                Intent intent = new Intent(CompleteInformationActivity.this , LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.done_imageView:
                saveChange();
                break;
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                Picasso.with(getApplicationContext())
                        .load(imageUri)
                        .into(profileImage);

            }
        }
    }

    private void saveChange() {

        city = editCity.getText().toString();
        age = editAge.getText().toString().trim();
        facebookLink = editFacebookLink.getText().toString();
        about = editAbout.getText().toString();


        if (imageUri != null) {
            final String imageName = UUID.randomUUID().toString() + ".jpg";

            storageReference.child("Images").child("Users").child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.child("Images").child("Users").child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            databaseReference.child("Users").child(userId).child("imageURL").setValue(imageURL);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CompleteInformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        if (!city.equals("")) {
            databaseReference.child("Users").child(userId).child("city").setValue(city);
            check = true ;
        }

        if (!age.equals("")) {
            databaseReference.child("Users").child(userId).child("age").setValue(age);
            check = true ;
        }

        if (!facebookLink.equals("")) {
            databaseReference.child("Users").child(userId).child("facebookLink").setValue(facebookLink);
            check = true ;
        }

        if (!about.equals("")) {
            databaseReference.child("Users").child(userId).child("about").setValue(about);
            check = true ;
        }

        if(check) {


            Toast.makeText(this, "Data Changed", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }else {

            Toast.makeText(this, "No thing changed", Toast.LENGTH_SHORT).show();
        }
    }

}
