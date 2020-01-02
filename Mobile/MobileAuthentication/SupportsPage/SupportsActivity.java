package com.example.mediaiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SupportsActivity extends AppCompatActivity {
    TextView displayGpNum;
    Button gpDetails, backBtn, callBtn;
    DatabaseReference reference;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supports);

        // this is the spinner used for the dropdown menu
        spinner = (Spinner)findViewById(R.id.Spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onClick(View view){

            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //final Intent intent;
                switch (position){
                    case 1: // goes to medical history page
                        Intent intent = new Intent(SupportsActivity.this,medical_history.class);
                        startActivity(intent);
                        break;

                    case 2: // goes to Reviews Page
                        Intent intent1 = new Intent(SupportsActivity.this,PatientReviews.class);
                        startActivity(intent1);
                        break;

                    case 3: // goes to Sign in page once logged out
                        FirebaseAuth.getInstance().signOut();
                        // after signing out the user will be moved to the medical history page
                        Intent GoBackToMainActivity = new Intent(SupportsActivity.this,SignInAuth.class);
                        startActivity(GoBackToMainActivity);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        displayGpNum = (TextView) findViewById(R.id.displayGpPhoneNum);
        gpDetails = (Button) findViewById(R.id.GpNumBtn);
        callBtn = (Button) findViewById(R.id.callBtn);



        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = displayGpNum.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:displayGpNum"));
                startActivity(intent);

            }
        });

        gpDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("professionals/Gordon Gibsor");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = (String) dataSnapshot.child("gpname").getValue();
                        String email = (String) dataSnapshot.child("gpemail").getValue();
                        String phone = (String) dataSnapshot.child("gpPhoneNumber").getValue();
                        //displayGpNum.setText(name);
                        //displayGpNum.setText(email);
                        displayGpNum.setText(phone);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

}
