package com.example.kdwholesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kdwholesale.Adapter.Productsadapter;
import com.example.kdwholesale.Domain.Products;
import com.example.kdwholesale.databinding.ActivityListProductBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListProductActivity extends AppCompatActivity {

    private ActivityListProductBinding binding;

    private int categoryid;
    private String categoryname,searchtext;
    private Boolean bestproduct;
    private ArrayList<Products> productlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        binding = ActivityListProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initgetextras();
        initsetupforproducts();
    }

    private void initsetupforproducts() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        binding.progressbarproduct.setVisibility(View.VISIBLE);

        Query query;
        if (bestproduct){
            query = databaseReference.orderByChild("BestProduct").equalTo(true);
        }
        else {
            query = databaseReference.orderByChild("CategoryID").equalTo(categoryid);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Products products = dataSnapshot.getValue(Products.class);
                        productlist.add(products);
                    }
                    if (productlist.size()>0){
                        binding.productrecycle.setLayoutManager(new GridLayoutManager(ListProductActivity.this,2));
                        Productsadapter productsadapter = new Productsadapter(productlist,ListProductActivity.this);
                        binding.productrecycle.setAdapter(productsadapter);
                    }
                    binding.progressbarproduct.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void initgetextras() {
        categoryid = getIntent().getExtras().getInt("Categoryid",0);
        categoryname = getIntent().getExtras().getString("Categoryname");
        bestproduct = Boolean.valueOf(getIntent().getExtras().getString("bestproductbollean"));
        Log.d("TAG", "initgetextras: "+bestproduct);

        binding.title.setText(categoryname);

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}