package com.example.findlostchildren.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.findlostchildren.Fragments.ProfileFragment;
import com.example.findlostchildren.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.add_image_btn)
    ImageView addImageBtn;
    @BindView(R.id.arrow_right_imageView)
    ImageView arrowRightImageView;
    @BindView(R.id.edit_userName)
    EditText editUserName;
    @BindView(R.id.edit_phone_number)
    EditText editPhoneNumber;
    @BindView(R.id.button_reset_password)
    Button buttonResetPassword;
    @BindView(R.id.arrow_right_imageView2)
    ImageView arrowRightImageView2;
    @BindView(R.id.edit_city)
    EditText editCity;
    @BindView(R.id.edit_age)
    EditText editAge;
    @BindView(R.id.edit_facebook_link)
    EditText editFacebookLink;
    @BindView(R.id.edit_about)
    EditText editAbout;
    @BindView(R.id.done_imageView)
    ImageView doneImageView;

    @BindView(R.id.header_view1)
    View accountInformationView;
    @BindView(R.id.header_view2)
    View personalInformationView;




    Uri imageUri;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
     boolean check = false  ; 
    private String userId, userEmail;
    String userName, phone, city, age, facebookLink, about;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = mAuth.getCurrentUser();
        userId = user.getUid(); //"zTK1GFINblNcrGdhF5EUB9XlE5M2";
        userEmail = user.getEmail();//"m@m.com";

    }

    @OnClick({R.id.add_image_btn, R.id.arrow_right_imageView, R.id.button_reset_password, R.id.arrow_right_imageView2, R.id.done_imageView})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.add_image_btn:
                selectImage();
                break;
            case R.id.arrow_right_imageView:
                if(accountInformationView.getVisibility()== View.GONE){
                    accountInformationView.setVisibility(View.VISIBLE);
                    arrowRightImageView.setRotation(90);
                }
                else{
                    accountInformationView.setVisibility(View.GONE);
                    arrowRightImageView.setRotation(360);
                }

                break;
            case R.id.button_reset_password:
                resetPassowrd();
                break;
            case R.id.arrow_right_imageView2:
                if (personalInformationView.getVisibility()== View.GONE){
                    personalInformationView.setVisibility(View.VISIBLE);
                    arrowRightImageView2.setRotation(90);
                }
                else {
                    personalInformationView.setVisibility(View.GONE);
                    arrowRightImageView2.setRotation(360);
                }

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

    private void resetPassowrd() {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Failed Sending Email" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveChange() {

        userName = editUserName.getText().toString();
        phone = editPhoneNumber.getText().toString().trim();
        city = editCity.getText().toString();
        age = editAge.getText().toString().trim();
        facebookLink = editFacebookLink.getText().toString();
        about = editAbout.getText().toString();


        if (imageUri  != null) {
            final String imageName = UUID.randomUUID().toString() + ".jpg";
            
            storageReference.child("Images").child("Users").child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.child("Images").child("Users").child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            databaseReference.child("Users").child(userId).child("imageURL").setValue(imageURL);
                            check = true ;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (!userName.equals("")) {
            databaseReference.child("Users").child(userId).child("userName").setValue(userName);
            check = true ;
        }

        if (!phone.equals("")) {
            databaseReference.child("Users").child(userId).child("phone").setValue(phone);
            check = true ;
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
