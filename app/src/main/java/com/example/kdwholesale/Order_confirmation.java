package com.example.kdwholesale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kdwholesale.databinding.ActivityOrderConfirmationBinding;

public class Order_confirmation extends AppCompatActivity {

    private ActivityOrderConfirmationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        binding = ActivityOrderConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_confirmation.this, Homepage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}