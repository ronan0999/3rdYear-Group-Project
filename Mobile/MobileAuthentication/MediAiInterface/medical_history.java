package com.example.mediaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class medical_history extends AppCompatActivity {

    private Button commit;
    private Button button;

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;

    //database
    private diabetes bloodpressure;
    private diabetes skinfold;
    private diabetes insulin;
    private diabetes bmi;
    private diabetes age;
    Spinner spinner;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        //database
        bloodpressure = new diabetes();
        skinfold = new diabetes();
        insulin = new diabetes();
        bmi = new diabetes();
        age = new diabetes();
        database= FirebaseDatabase.getInstance();
        reference = database.getReference("Diabetes");


        button = findViewById(R.id.button10);
        editText1 = findViewById(R.id.editText10);
        editText2 = findViewById(R.id.editText11);
        editText3 = findViewById(R.id.editText12);
        editText4 = findViewById(R.id.editText13);
        editText5 = findViewById(R.id.editText14);

        spinner = (Spinner)findViewById(R.id.Spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onClick(View view){

            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //final Intent intent;
                switch (position){
                    case 1: // goes to supports page
                        Intent intent = new Intent(medical_history.this,SupportsActivity.class);
                        startActivity(intent);
                        break;

                    case 2: // goes to Reviews Page
                        Intent intent1 = new Intent(medical_history.this,PatientReviews.class);
                        startActivity(intent1);
                        break;

                    case 3: // goes to Sign in page once logged out
                        FirebaseAuth.getInstance().signOut();
                        // after signing out the user will be moved to the MainActivity
                        Intent GoBackToMainActivity = new Intent(medical_history.this,SignInAuth.class);
                        startActivity(GoBackToMainActivity);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        commit = findViewById(R.id.button9);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bloodpressure.setBloodpressure(editText1.getText().toString());
                reference.child(bloodpressure.getBloodpressure()).setValue(bloodpressure);

                skinfold.setSkinfold(editText2.getText().toString());
                reference.child(skinfold.getSkinfold()).setValue(skinfold);

                insulin.setInsulin(editText3.getText().toString());
                reference.child(insulin.getInsulin()).setValue(insulin);

                bmi.setBmi(editText4.getText().toString());
                reference.child(bmi.getBmi()).setValue(bmi);

                age.setAge(editText5.getText().toString());
                reference.child(age.getAge()).setValue(age).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(medical_history.this,"added to database",Toast.LENGTH_LONG).show();
                        }

                        else{
                            Toast.makeText(medical_history.this,"error has occured",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHeartDisease();
            }
        });
    }

    public void openHeartDisease(){
        Intent intent = new Intent(this, HeartDisease.class);
        startActivity(intent);
    }
}
