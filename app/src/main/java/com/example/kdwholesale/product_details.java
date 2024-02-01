package com.example.kdwholesale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.example.kdwholesale.Adapter.Cartadapter;
import com.example.kdwholesale.Domain.Products;
import com.example.kdwholesale.Domain.UserCart;
import com.example.kdwholesale.databinding.ActivityProductDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class product_details extends AppCompatActivity {

    Products object;
    private ActivityProductDetailsBinding binding;
    int quantity = 1;
    int productQuantity;
    DatabaseReference databaseReference;
    Double totalprice;
    DatabaseReference databaseReference2;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        getdata();
        setvalue();
        backbtn();
        plusbtn();
        subbtn();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("UserCart").child(firebaseUser.getUid()).child(String.valueOf(object.getID()));

        addtocart();
    }

    private void addtocart() {
        binding.cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object.getQuantity()>0){
                    databaseReference.child(String.valueOf(object.getID())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                UserCart userCart = snapshot.getValue(UserCart.class);
                                Map<String,Object> updatedata = new HashMap<>();
                                updatedata.put("ProductQuantity",quantity);
                                updatedata.put("Price",object.getPrice());
                                updatedata.put("TotalPrice",object.getPrice()*quantity);
                            }
                            else{
                                totalprice = Double.valueOf(new DecimalFormat("##.##").format(quantity*object.getPrice()));
                                UserCart userCart = new UserCart(object.getTitle(),object.getImagePath(),object.getID(),quantity,object.getPrice(),totalprice);
                                databaseReference.setValue(userCart);
                            }
                            Toast.makeText(product_details.this, object.getTitle()+" "+quantity+" added to Cart", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(product_details.this, "Out of stock", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void subbtn() {
        binding.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity>1){
                    quantity--;
                    binding.productquantity.setText(""+quantity);
                    totalprice = Double.valueOf(new DecimalFormat("##.##").format(quantity*object.getPrice()));
                    binding.totalprice.setText("£"+totalprice);
                }
            }
        });
    }

    private void plusbtn() {
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity < productQuantity){
                    quantity++;
                    binding.productquantity.setText(""+quantity);
                    totalprice = Double.valueOf(new DecimalFormat("##.##").format(quantity*object.getPrice()));
                    binding.totalprice.setText("£"+totalprice);
                }
                else {
                    Toast.makeText(product_details.this, "Out of stock!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setvalue() {
        Glide.with(product_details.this).load(object.getImagePath()).into(binding.productimage);
        binding.producttitle.setText(object.getTitle());
        binding.productprice.setText("£"+object.getPrice());
        binding.productdesc.setText(object.getDescription());
        productQuantity = object.getQuantity();
        binding.totalprice.setText("£"+new DecimalFormat("##.##").format(object.getPrice()));
    }

    private void backbtn() {
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getdata() {
        object = (Products) getIntent().getSerializableExtra("object");

    }
}