package com.example.mediaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientReviews extends AppCompatActivity {

    FirebaseDatabase database;
    RatingBar ratingBar;
    EditText comment;
    Button commentButton;
    DatabaseReference reference;
    Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_reviews);

        // spinner for the menu
        spinner = (Spinner)findViewById(R.id.Spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onClick(View view){

            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 1: // goes to medical history page
                        Intent intent = new Intent(PatientReviews.this,medical_history.class);
                        startActivity(intent);
                        break;

                    case 2: // goes to Supports Page
                        Intent intent1 = new Intent(PatientReviews.this,SupportsActivity.class);
                        startActivity(intent1);
                        break;

                    case 3: // goes to Sign in page once logged out
                        FirebaseAuth.getInstance().signOut();
                        // after signing out the user will be moved to the medical history page
                        Intent GoBackToMainActivity = new Intent(PatientReviews.this,SignInAuth.class);
                        startActivity(GoBackToMainActivity);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //REVIEWS SECTION -> CUSTOMERS ARE ABLE TO RATE AND COMMENT
        database = FirebaseDatabase.getInstance();
        //commentReference = database.getReference("Reviews");
        reference = database.getReference("reviews");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        comment = (EditText) findViewById(R.id.commentEdtTxt);
        commentButton = (Button) findViewById(R.id.commentBtn);


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sending comment and rating values to the database
                String Comment = comment.getText().toString();
                float rating = ratingBar.getRating();
                String key = reference.push().getKey();
                reference.child(key).child("comment").setValue(Comment);
                reference.child(key).child("stars").setValue(rating);


                
            }
        });

    }



}
