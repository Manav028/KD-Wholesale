package com.example.kdwholesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.kdwholesale.Adapter.Productsadapter;
import com.example.kdwholesale.Domain.Products;
import com.example.kdwholesale.databinding.ActivityItemSearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Item_Search extends AppCompatActivity {

    private ActivityItemSearchBinding binding;
    private ArrayList<Products> productlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);

        binding = ActivityItemSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initbackbtn();

        binding.searchedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!= null){
                    initsearch(s.toString());
                }
                else{
                    Toast.makeText(Item_Search.this, "Please search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initbackbtn() {
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initsearch(String searchtext){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        binding.progressbarsearchprodut.setVisibility(View.VISIBLE);

        productlist.clear();
        Query query = databaseReference.orderByChild("Title").startAt(searchtext).endAt(searchtext+'\uf8ff');
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Products products = dataSnapshot.getValue(Products.class);
                        productlist.add(products);
                    }
                    if (productlist.size()>0){
                        binding.searchproduct.setLayoutManager(new GridLayoutManager(Item_Search.this,2));
                        Productsadapter productsadapter = new Productsadapter(productlist,Item_Search.this);
                        binding.searchproduct.setAdapter(productsadapter);
                    }
                    binding.progressbarsearchprodut.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}