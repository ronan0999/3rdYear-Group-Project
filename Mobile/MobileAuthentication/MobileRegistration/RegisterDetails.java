package com.example.mobileappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterDetails extends AppCompatActivity {

    EditText patientName;
    EditText age;
    EditText dob;
    EditText email;
    EditText gpId;
    EditText password;
    EditText insurancename;
    Button registerBtn;
    //  FirebaseAuth loginFirebaseAuth;
    private Patient patient;

    // firebase database
    FirebaseDatabase database;
    DatabaseReference reference;
    //DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        patientName = (EditText) findViewById(R.id.PatientName);
        age = (EditText) findViewById(R.id.age);
        dob = (EditText) findViewById(R.id.dob);
        email = (EditText) findViewById(R.id.email);
        gpId = (EditText) findViewById(R.id.gpId);
        registerBtn = (Button) findViewById(R.id.RegisterBtn);
        password = (EditText) findViewById(R.id.password);
        insurancename = (EditText) findViewById(R.id.Insurancename);

        patient = new Patient();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("patients");





    }

    public void btnRegister_Click(View view) {
        patient.setName(patientName.getText().toString());
        patient.setAge(age.getText().toString());
        patient.setDob(dob.getText().toString());
        patient.setEmail(email.getText().toString());
        patient.setGpId(gpId.getText().toString());
        patient.setPassword(password.getText().toString());
        patient.setInsurancename(insurancename.getText().toString());

        reference.child(patient.getName()).setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterDetails.this, "Patient Created Successfully!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(RegisterDetails.this, "Failed to Create Patient!", Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
