package com.example.kdwholesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.kdwholesale.databinding.ActivityForgotpasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Forgotpassword extends AppCompatActivity {

    FirebaseAuth auth;
    private ActivityForgotpasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        binding = ActivityForgotpasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendemailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.Email.getText().toString().isEmpty()){
                    binding.emailayout.setErrorEnabled(true);
                    binding.emailayout.setError("Please enter email");
                }
                else{
                    binding.emailayout.setErrorEnabled(false);
                    forgotpassword();
                }
            }
        });
    }

    public void forgotpassword(){
        auth = FirebaseAuth.getInstance();


        auth.sendPasswordResetEmail(binding.Email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(Forgotpassword.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException err){
                        binding.emailayout.setErrorEnabled(true);
                        binding.emailayout.setError("User with this email Doesn't exist please register");
                    }
                    catch (Exception e){
                        Log.e("TAG", "Forgot password error ");
                    }
                }
            }
        });
    }

}