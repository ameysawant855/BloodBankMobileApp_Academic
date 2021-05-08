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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.cloud_project.bloodhelper.R;
import com.android.cloud_project.bloodhelper.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {

    private EditText inputemail, inputpassword, retypePassword, fullName, address, contact, pincode;
    private FirebaseAuth firebaseAuth;
    private Button signupButton;
    private ProgressDialog progressDialog;
    private Spinner gender, bloodgroup, city;

    private boolean isUpdate = false;
    private DatabaseReference databaseReference, donor_ref;
    private FirebaseDatabase firebaseDatabase;
    private CheckBox isADonor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        setContentView(R.layout.activity_myprofile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        donor_ref = firebaseDatabase.getReference("donors");
        firebaseAuth = FirebaseAuth.getInstance();

        inputemail = findViewById(R.id.input_userEmail);
        inputpassword = findViewById(R.id.input_password);
        retypePassword = findViewById(R.id.input_password_confirm);
        fullName = findViewById(R.id.input_fullName);
        gender = findViewById(R.id.gender);
        address = findViewById(R.id.inputAddress);
        city = findViewById(R.id.inputCity);
        pincode = findViewById(R.id.pincode);
        bloodgroup = findViewById(R.id.inputBloodGroup);
        contact = findViewById(R.id.inputMobile);
        isADonor = findViewById(R.id.checkbox);

        signupButton = findViewById(R.id.button_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (firebaseAuth.getCurrentUser() != null) {

            inputemail.setVisibility(View.GONE);
            inputpassword.setVisibility(View.GONE);
            retypePassword.setVisibility(View.GONE);
            signupButton.setText("Update Profile");
            progressDialog.dismiss();
            getSupportActionBar().setTitle("My Profile");
            findViewById(R.id.image_logo).setVisibility(View.GONE);
            isUpdate = true;

            Query Profile = databaseReference.child(firebaseAuth.getCurrentUser().getUid());
            Profile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserData userData = dataSnapshot.getValue(UserData.class);

                    if (userData != null) {
                        progressDialog.show();
                        fullName.setText(userData.getName());
                        gender.setSelection(userData.getGender());
                        address.setText(userData.getAddress());
                        contact.setText(userData.getContact());
                        bloodgroup.setSelection(userData.getBloodgroup());
                        city.setSelection(userData.getCity());
                        pincode.setText(userData.getPincode());
                        Query donor = donor_ref.child(city.getSelectedItem().toString()).child(bloodgroup.getSelectedItem().toString()).child(firebaseAuth.getCurrentUser().getUid());

                        donor.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {
                                    isADonor.setChecked(true);
                                    isADonor.setText("Stop being a donor");
                                }
                                else
                                {
                                    Toast.makeText(MyProfileActivity.this, "Your are not a donor anymore!",
                                            Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("Debug", databaseError.getMessage());
                            }

                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Debug", databaseError.getMessage());
                }
            });


        } else progressDialog.dismiss();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputemail.getText().toString();
                final String password = inputpassword.getText().toString();
                final String ConfirmPassword = retypePassword.getText().toString();
                final String Name = fullName.getText().toString();
                final int Gender = gender.getSelectedItemPosition();
                final String Contact = contact.getText().toString();
                final int BloodGroup = bloodgroup.getSelectedItemPosition();
                final String Address = address.getText().toString();
                final int City = city.getSelectedItemPosition();
                final String Pincode = pincode.getText().toString();
                final String blood = bloodgroup.getSelectedItem().toString();
                final String cit   = city.getSelectedItem().toString();

                try {

                    if (Name.length() <= 2) {
                        ShowError("Name");
                        fullName.requestFocusFromTouch();
                    } else if (Contact.length() < 10) {
                        ShowError("Contact Number");
                        contact.requestFocusFromTouch();
                    } else if (Address.length() <= 2) {
                        ShowError("Address");
                        address.requestFocusFromTouch();
                    } else if (Pincode.length() > 5) {
                        ShowError("Pincode");
                        address.requestFocusFromTouch();
                    }

                    else {
                        if (!isUpdate) {
                            if (email.length() == 0) {
                                ShowError("Email ID");
                                inputemail.requestFocusFromTouch();
                            } else if (password.length() <= 5) {
                                ShowError("Password");
                                inputpassword.requestFocusFromTouch();
                            } else if (password.compareTo(ConfirmPassword) != 0) {
                                Toast.makeText(MyProfileActivity.this, "Password did not match!", Toast.LENGTH_LONG)
                                        .show();
                                retypePassword.requestFocusFromTouch();
                            } else {
                                progressDialog.show();
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(MyProfileActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(MyProfileActivity.this, "Registration failed! Try again", Toast.LENGTH_LONG)
                                                            .show();
                                                    Log.v("error", task.getException().getMessage());
                                                } else {
                                                    String id = firebaseAuth.getCurrentUser().getUid();
                                                    databaseReference.child(id).child("Name").setValue(Name);
                                                    databaseReference.child(id).child("Gender").setValue(Gender);
                                                    databaseReference.child(id).child("Contact").setValue(Contact);
                                                    databaseReference.child(id).child("BloodGroup").setValue(BloodGroup);
                                                    databaseReference.child(id).child("Address").setValue(Address);
                                                    databaseReference.child(id).child("City").setValue(City);
                                                    databaseReference.child(id).child("Pin").setValue(Pincode);

                                                    if(isADonor.isChecked())
                                                    {
                                                        donor_ref.child(cit).child(blood).child(id).child("UID").setValue(id).toString();
                                                        donor_ref.child(cit).child(blood).child(id).child("LastDonate").setValue("Not a donor yet!");
                                                        donor_ref.child(cit).child(blood).child(id).child("TotalDonate").setValue(0);
                                                        donor_ref.child(cit).child(blood).child(id).child("Name").setValue(Name);
                                                        donor_ref.child(cit).child(blood).child(id).child("Contact").setValue(Contact);
                                                        donor_ref.child(cit).child(blood).child(id).child("Address").setValue(Address);
                                                        donor_ref.child(cit).child(blood).child(id).child("City").setValue(City);
                                                        donor_ref.child(cit).child(blood).child(id).child("Pin").setValue(Pincode);
                                                    }

                                                    Toast.makeText(getApplicationContext(), "Account Created!", Toast.LENGTH_LONG)
                                                            .show();
                                                    Intent intent = new Intent(MyProfileActivity.this, MyDashboardActivity.class);
                                                    startActivity(intent);

                                                    finish();
                                                }
                                                progressDialog.dismiss();

                                            }

                                        });
                            }

                        } else {

                            String id = firebaseAuth.getCurrentUser().getUid();
                            databaseReference.child(id).child("Name").setValue(Name);
                            databaseReference.child(id).child("Gender").setValue(Gender);
                            databaseReference.child(id).child("Contact").setValue(Contact);
                            databaseReference.child(id).child("BloodGroup").setValue(BloodGroup);
                            databaseReference.child(id).child("Address").setValue(Address);
                            databaseReference.child(id).child("City").setValue(City);
                            databaseReference.child(id).child("Pin").setValue(Pincode);

                            if(isADonor.isChecked())
                            {
                                donor_ref.child(cit).child(blood).child(id).child("UID").setValue(id).toString();
                                donor_ref.child(cit).child(blood).child(id).child("LastDonate").setValue("Don't donate yet!");
                                donor_ref.child(cit).child(blood).child(id).child("TotalDonate").setValue(0);
                                donor_ref.child(cit).child(blood).child(id).child("Name").setValue(Name);
                                donor_ref.child(cit).child(blood).child(id).child("Address").setValue(Address);
                                donor_ref.child(cit).child(blood).child(id).child("Contact").setValue(Contact);
                                donor_ref.child(cit).child(blood).child(id).child("Pin").setValue(Pincode);
                                donor_ref.child(cit).child(blood).child(id).child("City").setValue(City);

                            }
                            else
                            {
                                donor_ref.child(cit).child(blood).child(id).removeValue();
                            }
                            Toast.makeText(getApplicationContext(), "Your account has been updated!", Toast.LENGTH_LONG)
                                    .show();
                            Intent intent = new Intent(MyProfileActivity.this, MyDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        progressDialog.dismiss();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void ShowError(String error) {
        Toast.makeText(MyProfileActivity.this, "Invalid "+error,
                Toast.LENGTH_LONG).show();
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
