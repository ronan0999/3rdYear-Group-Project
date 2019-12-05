package com.example.mobileappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientReviews extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference commentReference;
    RatingBar ratingBar;
    EditText comment;
    Button commentButton;

    private Review review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_reviews);

        database = FirebaseDatabase.getInstance();
        commentReference = database.getReference("reviews");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        comment = (EditText) findViewById(R.id.commentEdtTxt);
        commentButton = (Button) findViewById(R.id.commentBtn);
        review = new Review();

        patientRatingBar();

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review.setComment(comment.getText().toString());

                commentReference.child(review.getComment()).setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PatientReviews.this,"comment made successfully!",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(PatientReviews.this,"Failed to make comment!",Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }
        });

    }

    private void patientRatingBar(){
        final DatabaseReference reference = database.getReference("reviews").child("stars");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null && dataSnapshot.getValue() != null){
                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
                    ratingBar.setRating(rating);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }


        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser) reference.setValue(rating);
            }
        });
    }
}
