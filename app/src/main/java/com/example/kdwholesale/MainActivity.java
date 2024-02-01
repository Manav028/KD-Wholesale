package com.example.kdwholesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.example.kdwholesale.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseUser user;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
            }
        });

        binding.signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.Email.getText().toString())){
                    binding.emailayout.setErrorEnabled(true);
                    binding.emailayout.setError("Email is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.Email.getText().toString()).matches()){
                    binding.emailayout.setErrorEnabled(true);
                    binding.emailayout.setError("Email is Incorrect");
                } else if (TextUtils.isEmpty(binding.passwordtext.getText().toString())) {
                    binding.passwordlayout.setErrorEnabled(true);
                    binding.passwordlayout.setError("password is required");
                    binding.emailayout.setErrorEnabled(false);
                }else {
                    binding.emailayout.setErrorEnabled(false);
                    binding.passwordlayout.setErrorEnabled(false);
                    signin(binding.Email.getText().toString(),binding.passwordtext.getText().toString());
                }
            }
        });

        binding.forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Forgotpassword.class);
                startActivity(intent);
            }
        });
    }

    public void signin(String useremail,String userpasswrod) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(useremail,userpasswrod).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = auth.getCurrentUser();
                    Intent intent = new Intent(MainActivity.this, Homepage.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
                else {
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException err){
                        binding.emailayout.setErrorEnabled(true);
                        binding.emailayout.setError("User doesn't exist please SIGN UP");
                        binding.emailayout.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException err) {
                        binding.emailayout.setErrorEnabled(true);
                        binding.emailayout.setError("User and password are incorrect");
                        binding.emailayout.requestFocus();
                    } catch (Exception err) {
                        Log.e("TAG", "Catch in try" + err);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, Homepage.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }
}