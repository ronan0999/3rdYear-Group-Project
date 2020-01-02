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

public class HeartDisease extends AppCompatActivity {

    private Button commit;
    private Button button;

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;

    //database
    private heartrisk age;
    private heartrisk gender;
    private heartrisk chestpain;
    private heartrisk bloodpressure;
    private heartrisk cholesterol;

    FirebaseDatabase database;
    DatabaseReference reference;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_disease);

        spinner = (Spinner)findViewById(R.id.Spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onClick(View view){

            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //final Intent intent;
                switch (position){
                    case 1: // goes to supports page
                        Intent intent = new Intent(HeartDisease.this,medical_history.class);
                        startActivity(intent);
                        break;

                    case 2: // goes to Reviews Page
                        Intent intent1 = new Intent(HeartDisease.this,SupportsActivity.class);
                        startActivity(intent1);
                        break;

                    case 3: // goes to Reviews Page
                        Intent intent2 = new Intent(HeartDisease.this,PatientReviews.class);
                        startActivity(intent2);
                        break;

                    case 4: // goes to Sign in page once logged out
                        FirebaseAuth.getInstance().signOut();
                        // after signing out the user will be moved to the MainActivity
                        Intent GoBackToMainActivity = new Intent(HeartDisease.this,SignInAuth.class);
                        startActivity(GoBackToMainActivity);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //database
        age = new heartrisk();
        gender = new heartrisk();
        chestpain = new heartrisk();
        bloodpressure = new heartrisk();
        cholesterol = new heartrisk();
        database= FirebaseDatabase.getInstance();
        reference = database.getReference("Heart_Disease");


        editText1 = findViewById(R.id.editText15);
        editText2 = findViewById(R.id.editText16);
        editText3 = findViewById(R.id.editText17);
        editText4 = findViewById(R.id.editText18);
        editText5 = findViewById(R.id.editText19);

        commit = findViewById(R.id.button11);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age.setAge(editText1.getText().toString());
                reference.child(age.getAge()).setValue(age);

                gender.setGender(editText2.getText().toString());
                reference.child(gender.getGender()).setValue(gender);

                chestpain.setChestpain(editText3.getText().toString());
                reference.child(chestpain.getChestpain()).setValue(chestpain);

                bloodpressure.setBloodpressure(editText4.getText().toString());
                reference.child(bloodpressure.getBloodpressure()).setValue(bloodpressure);

                cholesterol.setCholesterol(editText5.getText().toString());
                reference.child(cholesterol.getCholesterol()).setValue(cholesterol).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(HeartDisease.this,"added to database",Toast.LENGTH_LONG).show();
                        }

                        else{
                            Toast.makeText(HeartDisease.this,"error has occured",Toast.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });


    }


}
