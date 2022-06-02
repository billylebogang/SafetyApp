package com.example.safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText loginUsername,loginPassword;

    Button loginBtn,loginRegisterBtn;

    //database stuff
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

        //buttons
        loginBtn = findViewById(R.id.loginBtn);
        loginRegisterBtn = findViewById(R.id.loginRegisterBtn);

        //database stuff
        auth = FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = loginUsername.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    loginUsername.setError("Please enter username");
                    loginUsername.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    loginPassword.setError("Please enter password");
                    loginPassword.requestFocus();
                    return;
                }
                else{

                    auth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();

                            }
                            else{
                                Toast.makeText(Login.this, "Could not log in", Toast.LENGTH_SHORT).show();
                            }                        }
                    });

                }


            }
        });
        loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Login.this, Registration.class));
                finish();
            }
        });

    }
}