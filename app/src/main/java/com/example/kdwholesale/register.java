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
import com.example.kdwholesale.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    int genderID;
    String gendertext;
    String mobileRegex = "[7-9][0-9]{9}";
    Matcher matcher1;
    Pattern patternMatcher = Pattern.compile(mobileRegex);
    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(register.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.dob.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year , month , day);
                picker.show();
            }
        });

        binding.signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matcher1 = patternMatcher.matcher(binding.phone.getText().toString());
                if (TextUtils.isEmpty(binding.username.getText().toString())){
                    Toast.makeText(register.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                    checkcondition(binding.usernameayout.getId(),"Username is required");
                } else if (TextUtils.isEmpty(binding.Email.getText().toString())) {
                    Toast.makeText(register.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                    checkcondition(binding.emailayout.getId(),"Email is required");
                } else if (TextUtils.isEmpty(binding.dob.getText().toString())) {
                    Toast.makeText(register.this, "Please enter your DOB", Toast.LENGTH_SHORT).show();
                    binding.dob.setError("Enter your date of birth");
                    binding.dob.setFocusable(true);
                } else if (binding.radiogroup.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(register.this, "Please select gender", Toast.LENGTH_SHORT).show();
                } else if (binding.phone.getText().toString().length() != 10) {
                    Toast.makeText(register.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    checkcondition(binding.phonelayout.getId(),"Phone number is required");
                } else if(!matcher1.matches()){
                    Toast.makeText(register.this, "Number is in wrong format", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(binding.password.getText().toString())) {
                    Toast.makeText(register.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    checkcondition(binding.passwordlayout.getId(),"Password is required");
                } else if (!binding.password.getText().toString().equals(binding.passwordc.getText().toString())) {
                    Toast.makeText(register.this, "Password Doesn't match", Toast.LENGTH_SHORT).show();
                    checkcondition(binding.passwordclayout.getId(),"Password Doesn't match");
                }else {
                    binding.usernameayout.setErrorEnabled(false);
                    binding.emailayout.setErrorEnabled(false);
                    binding.phonelayout.setErrorEnabled(false);
                    binding.passwordlayout.setErrorEnabled(false);
                    binding.passwordclayout.setErrorEnabled(false);
                    genderID = binding.radiogroup.getCheckedRadioButtonId();
                    if (binding.radiobtn1.getId()==genderID){
                        gendertext = "male";
                    } else if (binding.radiobtn2.getId() == genderID) {
                        gendertext = "female";
                    } else if (binding.radiobtn3.getId() == genderID) {
                        gendertext = "other";
                    }
                    registerUser(binding.username.getText().toString(),binding.Email.getText().toString(),binding.dob.getText().toString(),gendertext,binding.phone.getText().toString(),binding.password.getText().toString());
                }
            }
        });

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void checkcondition(Integer layoutid,String layoutmessage){
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(binding.usernameayout.getId());
        arrayList.add(binding.emailayout.getId());
        arrayList.add(binding.phonelayout.getId());
        arrayList.add(binding.passwordlayout.getId());
        arrayList.add(binding.passwordclayout.getId());

        arrayList.remove(layoutid);
        TextInputLayout textInputLayout = findViewById(layoutid);

        textInputLayout.setError(layoutmessage);

        for (int i = 0; i < 4; i++) {
            Log.d("TAG", "checkcondition: "+arrayList.get(i));
            TextInputLayout textInputLayout1 = findViewById(arrayList.get(i));
            textInputLayout1.setErrorEnabled(false);
        }
    }

    public void registerUser(String username, String useremail, String userdob , String usergender , String userphone , String userpassword){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(register.this, "Register succesful", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    ReadWriteUserDetials readWriteUserDetials = new ReadWriteUserDetials(userdob,usergender,userphone);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");

                    reference.child(firebaseUser.getUid()).setValue(readWriteUserDetials).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(register.this, "user information used", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(register.this, Homepage.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Log.e("TAG", "Error in registration " );
                                Toast.makeText(register.this, "User registration is fail please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthUserCollisionException e){
                        Toast.makeText(register.this, "Email is already register", Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        Log.e("TAG", "onComplete: "+e.getMessage());
                    }
                }
            }
        });
    }

}