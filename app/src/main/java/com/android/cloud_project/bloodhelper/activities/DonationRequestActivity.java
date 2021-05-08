package com.android.cloud_project.bloodhelper.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.cloud_project.bloodhelper.R;
import com.android.cloud_project.bloodhelper.models.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DonationRequestActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    EditText textMobile, textLocation;
    Spinner spinnerBloodGroup, spinnerCity;
    Button post_button;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    Calendar calendar;
    String userid;
    String time, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_request);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

        getSupportActionBar().setTitle("Submit Blood Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textMobile = findViewById(R.id.getMobile);
        textLocation = findViewById(R.id.getLocation);

        spinnerBloodGroup = findViewById(R.id.SpinnerBlood);
        spinnerCity = findViewById(R.id.SpinnerDivision);

        post_button = findViewById(R.id.postbtn);

        calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);

        time = "";date = "";
        String ampm="AM";
        month+=1;
        if(calendar.get(Calendar.AM_PM) == 1)
        {
            ampm = "PM";
        }

        if(hour<10)
        {
            time += "0";
        }
        time += hour;
        time +=":";

        if(min<10) {
            time += "0";
        }

        time +=min;
        time +=(" "+ampm);

        date = day+"/"+month+"/"+year;

        FirebaseUser cur_user = FirebaseAuth.getInstance().getCurrentUser();

        if(cur_user == null)
        {
            startActivity(new Intent(DonationRequestActivity.this, LoginActivity.class));
        } else {
            userid = cur_user.getUid();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("posts");

        try {
            post_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    final Query findname = firebaseDatabase.getReference("users").child(userid);

                    if(textMobile.getText().length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Contact number required!",
                                Toast.LENGTH_LONG).show();
                    }
                    else if(textLocation.getText().length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Location required!",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        findname.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    UserData obj;
                                    String name = null;
                                    obj = dataSnapshot.getValue(UserData.class);
                                    if (obj != null) {
                                        name = obj.getName();
                                    }
                                    databaseReference.child(userid).child("Name").setValue(name);
                                    databaseReference.child(userid).child("Contact").setValue(textMobile.getText().toString());
                                    databaseReference.child(userid).child("Address").setValue(textLocation.getText().toString());
                                    databaseReference.child(userid).child("City").setValue(spinnerCity.getSelectedItem().toString());
                                    databaseReference.child(userid).child("BloodGroup").setValue(spinnerBloodGroup.getSelectedItem().toString());
                                    databaseReference.child(userid).child("Time").setValue(time);
                                    databaseReference.child(userid).child("Date").setValue(date);
                                    Toast.makeText(DonationRequestActivity.this, "Your blood request has been submitted",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(DonationRequestActivity.this, MyDashboardActivity.class));
                                }
                                 else {
                                    Toast.makeText(getApplicationContext(), "Database error occured!",
                                            Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("User", databaseError.getMessage());

                            }
                        });
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        progressDialog.dismiss();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
