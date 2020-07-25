package com.example.findlostchildren.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findlostchildren.R;
import com.example.findlostchildren.Utils.MyUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    FirebaseAuth auth ;

    EditText email_login , pass_login ;

    Button btn_login ;

    TextView btn_register ;

    FrameLayout parent  ;

    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        definitions();
        seadDataAuto();
        onClick();
    }


    private void onClick (){


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    login();


            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });


    }

    private void definitions(){
        auth = FirebaseAuth.getInstance();

        email_login = findViewById(R.id.enter_email);

        pass_login = findViewById(R.id.enter_pass);

        btn_register = findViewById(R.id.signUp_btn);

        btn_login = findViewById(R.id.btn_login);

        parent = findViewById(R.id.parentOfLogin);

        progressBar = findViewById(R.id.progressOfLogin);






    }

    private void login(){

        String account = email_login.getText().toString();
        String pass = pass_login.getText().toString();

        if(account.length()> 0  && pass.length()>0) {

            progressBar.setVisibility(View.VISIBLE);
            parent.setVisibility(View.GONE);

            btn_register.setVisibility(View.GONE);

            auth.signInWithEmailAndPassword(account , pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        startActivity(new Intent(LoginActivity.this , MainActivity.class));
                        finish();

                    }else {
                        progressBar.setVisibility(View.GONE);
                        parent.setVisibility(View.VISIBLE);

                        btn_register.setVisibility(View.VISIBLE);

                        email_login.setError("please check your email ");
                        pass_login.setError("please check your password");
                        Toast.makeText(getApplicationContext(), "email or password is wrong ", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }else{
            email_login.setError("please enter your email ");
            pass_login.setError("please enter your password");
            email_login.setHintTextColor(Color.RED);
            pass_login.setHintTextColor(Color.RED);


        }

    }


    private void  seadDataAuto(){


        if(!MyUtils.mailforLogin.equals("")){
            email_login.setText(MyUtils.mailforLogin);
            pass_login.setText(MyUtils.passwordforLogin);



        }



    }


}
