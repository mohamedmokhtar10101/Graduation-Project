package com.example.findlostchildren.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.findlostchildren.Models.UsersModel;
import com.example.findlostchildren.R;
import com.example.findlostchildren.Utils.MyUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    EditText editTextOfemail, editTextOfpassword, editTextOfRepassword,editTextOfUserName
             , editTextOfphone ;


    Button btn_register;
    FirebaseAuth auth;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    LinearLayout parent ;
    ProgressBar progressBar ;

    String email ,  password , repassword , userName   , phone ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        definitions();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }

    private void definitions(){

        auth = FirebaseAuth.getInstance();

        editTextOfemail = findViewById(R.id.edit_email);

        editTextOfpassword = findViewById(R.id.edit_password);

        editTextOfRepassword = findViewById(R.id.edit_repass);

        btn_register = findViewById(R.id.rejester);

        editTextOfUserName = findViewById(R.id.edit_userName);

        progressBar = findViewById(R.id.progressOfRejester);

        editTextOfphone  = findViewById(R.id.edit_phone);



        parent = findViewById(R.id.parentOfRejester);


    }

    private void register() {

         email = editTextOfemail.getText().toString().trim();
         password = editTextOfpassword.getText().toString().trim();
         repassword = editTextOfRepassword.getText().toString().trim();
         userName = editTextOfUserName.getText().toString().trim();
         phone = editTextOfphone.getText().toString().trim();


        if (userName.equals("")){
            editTextOfUserName.setError("Required");




        }else if(email.equals("")){
            editTextOfemail.setError("Required");


        }else if(phone.equals("")){


             editTextOfphone.setError("Required");
        }

        else if(password.equals("")){
            editTextOfpassword.setError("Required");


        }else if(password.length() < 6){
            editTextOfpassword.setError("password should be more than 5 characters");



        }
        else if(!password.equals(repassword)){
            editTextOfRepassword.setError("password is not equals re-password");

        }

        else {
            parent.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            actionOfLogin(email,password);
        }





    }

    private void actionOfLogin(final String email , final String password){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Register is Succeed", Toast.LENGTH_LONG).show();

                            uploadDataTofireBase();

                            startActivity(new Intent( RegistrationActivity.this , CompleteInformationActivity.class));
                            finish();

                            MyUtils.mailforLogin= email ;
                            MyUtils.passwordforLogin = password  ;


                        } else {

                            parent.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            editTextOfemail.setError("please check your email ");


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadDataTofireBase(){

        UsersModel model = new UsersModel();
        model.setEmail(email);
        model.setUserName(userName);
        model.setUserID(auth.getCurrentUser().getUid().trim());

        model.setPhone(phone);

        ref.child("Users").child(auth.getCurrentUser().getUid()).setValue(model);


    }
}
