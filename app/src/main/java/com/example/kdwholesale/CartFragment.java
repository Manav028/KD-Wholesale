package com.example.kdwholesale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kdwholesale.Adapter.Cartadapter;
import com.example.kdwholesale.Domain.UserCart;
import com.example.kdwholesale.databinding.FragmentCartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = auth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserCart");
    ArrayList<UserCart> cartlist = new ArrayList<>();
    Context context;
    private double subtotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(getLayoutInflater());

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity!=null){
            appCompatActivity.setSupportActionBar(binding.toolbar);
            appCompatActivity.getSupportActionBar().setTitle("Cart");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        showcart();
        orderclick();
    }

    private void orderclick() {
        binding.orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartlist.size()>0){
                    Intent intent = new Intent(context, address_and_card.class);
                    intent.putExtra("objectofcart",cartlist);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Cart is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showcart(){
        cartlist.clear();
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                        UserCart userCart = cartItemSnapshot.getValue(UserCart.class);
                        cartlist.add(userCart);
                    }
                    binding.recycleproductcart.setLayoutManager(new LinearLayoutManager(context));
                    Cartadapter cartadapter = new Cartadapter(context,cartlist,CartFragment.this);
                    binding.recycleproductcart.setAdapter(cartadapter);
                    totalitem();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void totalitem() {

        subtotal = 0;

        for (int i=0;i<cartlist.size();i++){
            subtotal = subtotal + cartlist.get(i).getTotalPrice();
        }
        binding.subtotal.setText("£"+new DecimalFormat("##.##").format(subtotal-(subtotal*0.2)));
        binding.totaltax.setText("£"+new DecimalFormat("##.##").format(subtotal*0.2));
        binding.Delivery.setText("£"+0.00);
        binding.total.setText("£"+new DecimalFormat("##.##").format(subtotal));
    }
}