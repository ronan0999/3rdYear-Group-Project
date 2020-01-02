package com.example.mediaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

public class SignUpAuth extends AppCompatActivity {

    EditText emailId, password;
    Button signUpBtn;
    TextView signInTv;
    FirebaseAuth loginFirebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    //FirebaseDatabase database;
    //DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_auth);

        // gettimg the instance for firebase auth
        loginFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        signUpBtn = findViewById(R.id.button);
        signInTv = findViewById(R.id.textView);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("patientAccounts");







        // when you click this button you will get the valued such as the email and pas sword
        // from the form that the user has filled in.
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sending email and password details to the patientAccounts collection
                String userEmail = emailId.getText().toString();
                String userPassword = password.getText().toString();
                String key = reference.push().getKey();
                reference.child(key).child("email").setValue(userEmail);
                reference.child(key).child("password").setValue(userPassword);
                //reference.push().child("email").setValue(userEmail);
                //reference.push().child("passowrd").setValue(userPassword);

                // checking if the email or passsword is empty
                if (userEmail.isEmpty()){
                    emailId.setError("Please enter your email");
                    emailId.requestFocus();
                }
                else if(userPassword.isEmpty()){
                    password.setError("please enter your password");
                    password.requestFocus();
                }
                // if both the email and password is empty Im just going to prompt an error to the user
                else if(userEmail.isEmpty() && userPassword.isEmpty()){
                    Toast.makeText(SignUpAuth.this,"Email and password fields are empty!",Toast.LENGTH_SHORT).show();
                }

                // if the email and password are filled then the user email and password will be created
                else if(!(userEmail.isEmpty() && userPassword.isEmpty())){
                    loginFirebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(SignUpAuth.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpAuth.this,"Sign up was unsuccessful, Please try again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // if the task is sucessful will go to the register page
                                startActivity(new Intent(SignUpAuth.this,RegisterDetails.class));
                            }

                        }
                    });

                }
                else{
                    Toast.makeText(SignUpAuth.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }


        });

        signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpAuth.this,SignInAuth.class);
                startActivity(intent);
            }
        });
    }



    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }







}
