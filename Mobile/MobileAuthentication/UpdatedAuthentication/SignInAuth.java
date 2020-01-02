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
import com.google.firebase.auth.FirebaseUser;

public class SignInAuth extends AppCompatActivity {

    EditText emailId, password;
    Button signInBtn;
    TextView signUpTv;
    FirebaseAuth loginFirebaseAuth;
    private FirebaseAuth.AuthStateListener loginAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_auth);

        // gettimg the instance for firebase auth
        loginFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        signInBtn = findViewById(R.id.button);
        signUpTv = findViewById(R.id.textView);

        // im using firebase authentication listner to check if the the user email and pasword exist.
        // if the both fields exist then the user can proceed with the login]
        // if not then i'll just prompt and error message to the user.
        loginAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser loginFirebaseUser = loginFirebaseAuth.getCurrentUser();
                if (loginFirebaseUser != null) { // so im just checking if any user is created or not created (null)
                    Toast.makeText(SignInAuth.this, "You're logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInAuth.this, medical_history.class);
                    startActivity(intent); // if the current user exists and their not null then  im just showing that their logged in.
                    // this message will be displayed in the homeActivity class
                } else {
                    Toast.makeText(SignInAuth.this, "Please Login", Toast.LENGTH_SHORT).show();
                }

            }
        };

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailId.getText().toString();
                String userPassword = password.getText().toString();

                // checking if the email or passsword is empty
                if (userEmail.isEmpty()) {
                    emailId.setError("Please enter your email");
                    emailId.requestFocus();
                } else if (userPassword.isEmpty()) {
                    password.setError("please enter your password");
                    password.requestFocus();
                }
                // if both the email and password is empty Im just going to prompt an error to the user
                else if (userEmail.isEmpty() && userPassword.isEmpty()) {
                    Toast.makeText(SignInAuth.this, "Email and password fields are empty!", Toast.LENGTH_SHORT).show();
                }

                // if the email and password are filled then the user email and password will be created
                // also if the values are provided for email and the password then we can call the method signInWithEmailAndPassword
                // using firebase authentication.
                // im placing an addOnCompleteListener to check if the task is executed or if it's not executed.
                else if (!(userEmail.isEmpty() && userPassword.isEmpty())) {
                    loginFirebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(SignInAuth.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignInAuth.this, "Login Error, Please Try to Login Again", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intentGoingToHome = new Intent(SignInAuth.this, medical_history.class);
                                startActivity(intentGoingToHome);
                            }
                        }
                    });

                } else {                    Toast.makeText(SignInAuth.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            // using an setonclick lisnter on the textview when you want to go into the sign up screen
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(SignInAuth.this,SignUpAuth.class);
                startActivity(signUpIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginFirebaseAuth.addAuthStateListener(loginAuthStateListener);
    }
}


