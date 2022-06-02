package com.example.safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    EditText registerName,registerSurname,registerNumber,registerId,
            registerNextKin,kinNumber,registerAddress,registerEmail,
            registerPassword;

    Button registerBtn,registerLoginBtn;

    RadioButton male,female;

    //database stuff

    FirebaseAuth auth;
    FirebaseUser USER;
    FirebaseDatabase database;
    DatabaseReference myRef;

    //usefull user objects
    User user;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        //for profile
        registerName = findViewById(R.id.registerName);
        registerSurname = findViewById(R.id.registerSurname);
        registerId = findViewById(R.id.registerId);
        registerNumber = findViewById(R.id.registerNumber);
        kinNumber = findViewById(R.id.kinNumber);
        registerNextKin = findViewById(R.id.registerNextKin);
        registerAddress = findViewById(R.id.registerAddress);

        //for account
        registerPassword = findViewById(R.id.registerPassword);
        registerEmail = findViewById(R.id.registerEmail);

        //buttons
        registerBtn = findViewById(R.id.registerBtn);
        registerLoginBtn = findViewById(R.id.registerLoginBtn);

        //gender buttons
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        //database stuff
       database = FirebaseDatabase.getInstance();
       myRef = database.getReference("User_profiles");
       auth = FirebaseAuth.getInstance();
       USER = auth.getCurrentUser();

       //usefull user object
        user = new User();
        profile = new Profile();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting data
                String name,surname,age,gender, number, id, kinName, kinnumber, address,email,password;

                name = registerName.getText().toString().trim();
                surname = registerSurname.getText().toString().trim();
                number = registerNumber.getText().toString().trim();
                id = registerId.getText().toString().trim();
                kinName = registerNextKin.getText().toString().trim();
                kinnumber = kinNumber.getText().toString().trim();
                address = registerAddress.getText().toString().trim();
                email = registerEmail.getText().toString().trim();
                password = registerPassword.getText().toString().trim();


                if (TextUtils.isEmpty(name)){
                    registerName.setError("Please fill name");
                    registerName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(surname)){
                    registerSurname.setError("Please fill name");
                    registerSurname.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(number)&& !TextUtils.isDigitsOnly(number)){
                    registerNumber.setError("Please fill name");
                    registerNumber.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(id)){
                    registerId.setError("Please fill name");
                    registerId.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(kinName)){
                    registerNextKin.setError("Please fill name");
                    registerNextKin.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(kinnumber)){
                    kinNumber.setError("Please fill name");
                    kinNumber.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(address)){
                    registerAddress.setError("Please fill name");
                    registerAddress.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    registerEmail.setError("Please fill name");
                    registerEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    registerPassword.setError("Please fill name");
                    registerPassword.requestFocus();
                    return;
                }

                else{

                    user = new User(email, password);
                    profile = new Profile(name,surname,"20",number,id,kinName, kinnumber,address,"Male");
                   // String userID = null;

                    try{
                        createUser(user, profile);
                        //userID = USER.getUid();

                    }
                    catch (Exception e){
                        Toast.makeText(Registration.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("REGISTRATION", "onClick: "+e.getMessage());
                    }

                   /* if(userID == null){
                        Toast.makeText(Registration.this, "Failled to get id", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        addProfile(userID, profile);
                    }*/

                }

            }
        });
        registerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
    }


    public void createUser(User user, Profile profile){


        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Registration.this, "Registration successful ", Toast.LENGTH_SHORT).show();

                addProfile(authResult.getUser().getUid(),profile);
                //startActivity(new Intent(Registration.this, MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration.this, "Registration failed "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(Registration.this, "Registration cancelled", Toast.LENGTH_SHORT).show();
                return;
            }
        });



    }

    public void addProfile(String id, Profile profile){

        myRef.child(id).setValue(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(Registration.this, "Profile successful", Toast.LENGTH_SHORT).show();

                //start activity here

                startActivity(new Intent(Registration.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration.this, "Profile failed "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("REGISTRATION", "onFailure: "+ e.getMessage());
                return;
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(Registration.this, "Profile cancelled", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }
}