package com.example.mediaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;

public class RegisterDetails extends AppCompatActivity {

    EditText patientName, age, dob, email, gpId, gpName, insurancename, patientPhoneNum, patientID ;
    Button registerBtn;


    // firebase database
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        patientName = (EditText) findViewById(R.id.PatientName);
        age = (EditText) findViewById(R.id.age);
        dob = (EditText) findViewById(R.id.dob);
        email = (EditText) findViewById(R.id.email);
        gpId = (EditText) findViewById(R.id.gpId);
        gpName = (EditText) findViewById(R.id.gpName);
        insurancename = (EditText) findViewById(R.id.Insurancename);
        patientID = (EditText) findViewById(R.id.patientID);
        patientPhoneNum = (EditText) findViewById(R.id.patientPhoneNum);
        registerBtn = (Button) findViewById(R.id.RegisterBtn);

        // getting a database instance
        database = FirebaseDatabase.getInstance();
        // creating reference so the data pushed will be gone into the patients collection
        reference = database.getReference("patients");

        // once the register button is clicked it will push all the data into the database into the patients collection
        // under an unique automated ID generated each time you register a patient
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientname = patientName.getText().toString();
                String patientage = age.getText().toString();
                String patientdob = dob.getText().toString();
                String patientemail = email.getText().toString();
                String patientphonenum = patientPhoneNum.getText().toString();
                String GPID = gpId.getText().toString();
                String GPName = gpName.getText().toString();
                String InsuranceName = insurancename.getText().toString();
                String pID = patientID.getText().toString();

                // pushing the data
                String key = reference.push().getKey();

                reference.child(key).child("name").setValue(patientname);
                reference.child(key).child("age").setValue(patientage);
                reference.child(key).child("dob").setValue(patientdob);
                reference.child(key).child("email").setValue(patientemail);
                reference.child(key).child("phone").setValue(patientphonenum);
                reference.child(key).child("gpId").setValue(GPID);
                reference.child(key).child("gpname").setValue(GPName);
                reference.child(key).child("insurancename").setValue(InsuranceName);
                reference.child(key).child("pID").setValue(pID);

                // once registration is completed then the user will be moved to the paypal page where they can make a payment
                Intent intent = new Intent(RegisterDetails.this, activity_PayPal.class);
                startActivity(intent); // starts the intent to go to the activity_paypal page

            }
        });


    }



    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
