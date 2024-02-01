package com.example.kdwholesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.kdwholesale.Domain.ReadWriteUserDetials;
import com.example.kdwholesale.databinding.ActivityUpdateprofileBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class updateprofile extends AppCompatActivity {

    private ActivityUpdateprofileBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String gendertext,oldgendertext;
    String mobileRegex = "[7-9][0-9]{9}";
    Matcher matcher1;
    Pattern patternMatcher = Pattern.compile(mobileRegex);
    DatePickerDialog picker;
    ReadWriteUserDetials readWriteUserDetials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);

        binding = ActivityUpdateprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backbtn();

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid());

        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(updateprofile.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.dob.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year , month , day);
                picker.show();
            }
        });

        retriveprofiledata();
        checkdata();
    }

    private void retriveprofiledata() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    readWriteUserDetials = snapshot.getValue(ReadWriteUserDetials.class);
                    binding.username.setText(firebaseUser.getDisplayName());
                    binding.dob.setText(readWriteUserDetials.userdob);
                    binding.phone.setText(readWriteUserDetials.userphone);

                    if (readWriteUserDetials.usergender.equals("male")){
                        binding.radiobtn1.setChecked(true);
                        oldgendertext = "male";
                    } else if (readWriteUserDetials.usergender.equals("female")) {
                        binding.radiobtn2.setChecked(true);
                        oldgendertext = "female";
                    }else {
                        binding.radiobtn3.setChecked(true);
                        oldgendertext = "other";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void backbtn() {
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkdata() {
        binding.updateprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                matcher1 = patternMatcher.matcher(binding.phone.getText().toString());

                if (TextUtils.isEmpty(binding.username.getText().toString())) {
                    Toast.makeText(updateprofile.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                    checkcondition(binding.usernameayout.getId(),"Username is required");
                } else if (TextUtils.isEmpty(binding.dob.getText().toString())) {
                    Toast.makeText(updateprofile.this, "Please enter your DOB", Toast.LENGTH_SHORT).show();
                    binding.dob.setError("Enter your date of birth");
                    binding.dob.setFocusable(true);
                } else if (binding.radiogroup.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(updateprofile.this, "Please select gender", Toast.LENGTH_SHORT).show();
                }else if (binding.phone.getText().toString().length() != 10) {
                    Toast.makeText(updateprofile.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    checkcondition(binding.phonelayout.getId(),"Phone number is of length 10");
                } else if(!matcher1.matches()){
                    Toast.makeText(updateprofile.this, "Number is in wrong format", Toast.LENGTH_SHORT).show();
                }else {
                    binding.usernameayout.setErrorEnabled(false);
                    binding.phonelayout.setErrorEnabled(false);
                    int genderID = binding.radiogroup.getCheckedRadioButtonId();
                    if (binding.radiobtn1.getId()==genderID){
                        gendertext = "male";
                    } else if (binding.radiobtn2.getId() == genderID) {
                        gendertext = "female";
                    } else if (binding.radiobtn3.getId() == genderID) {
                        gendertext = "other";
                    }
                    updateprofiledata();
                }
            }
        });
    }

    private void updateprofiledata() {
        if (checkpreviousdata()){
            Toast.makeText(this, "Data is not change", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Data changed", Toast.LENGTH_SHORT).show();
            pushdata();
        }
    }

    private void pushdata() {
        HashMap<String,Object> updatedata = new HashMap<>();
        updatedata.put("userdob",binding.dob.getText().toString());
        updatedata.put("usergender",gendertext);
        updatedata.put("userphone",binding.phone.getText().toString());
        databaseReference.setValue(updatedata);
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(binding.username.getText().toString()).build();
        firebaseUser.updateProfile(profileChangeRequest);
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        finish();
    }

    public boolean checkpreviousdata(){
        return binding.username.getText().toString().equals(firebaseUser.getDisplayName()) && binding.phone.getText().toString().equals(readWriteUserDetials.userphone) && binding.dob.getText().toString().equals(readWriteUserDetials.userdob) && oldgendertext.equals(gendertext);
    }

    public void checkcondition(Integer layoutid,String layoutmessage){
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(binding.usernameayout.getId());
        arrayList.add(binding.phonelayout.getId());

        arrayList.remove(layoutid);
        TextInputLayout textInputLayout = findViewById(layoutid);

        textInputLayout.setError(layoutmessage);

        for (int i = 0; i < 1; i++) {
            Log.d("TAG", "checkcondition: "+arrayList.get(i));
            TextInputLayout textInputLayout1 = findViewById(arrayList.get(i));
            textInputLayout1.setErrorEnabled(false);
        }
    }

}