package com.example.mobileappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.HardwarePropertiesManager;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    Button logOutbtn, reviewBtn;
    FirebaseAuth logOutFirebaseAuth;
    private FirebaseAuth.AuthStateListener logoutAuthStateListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logOutbtn = findViewById(R.id.logout);
        reviewBtn = findViewById(R.id.reviewBtn);

        // when the button is clicked by the user I used firebase auth to sign out the user
        logOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // after signing out the user will be moved to the MainActivity
                Intent GoBackToMainActivity = new Intent(HomeActivity.this,SignUpAuth.class);
                startActivity(GoBackToMainActivity);
            }
        });

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewPage = new Intent(HomeActivity.this, PatientReviews.class);
                startActivity(reviewPage);
            }
        });



    }
}
