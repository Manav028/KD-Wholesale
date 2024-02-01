package com.example.kdwholesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kdwholesale.Adapter.Ordersummaryadapter;
import com.example.kdwholesale.Domain.Address;
import com.example.kdwholesale.Domain.Card;
import com.example.kdwholesale.Domain.Orderitemsummary;
import com.example.kdwholesale.databinding.ActivityOrdersummaryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ordersummary extends AppCompatActivity {

    private ActivityOrdersummaryBinding binding;
    DatabaseReference databaseReference,databaseReference2;
    String ordernumber;
    ArrayList<Orderitemsummary> arrayList = new ArrayList<>();
    Context context;
    double totalprice=0;
    String cardnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersummary);

        binding = ActivityOrdersummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.e("TAG", "onCreate: "+String.valueOf(getIntent().getExtras().getInt("ordernumber")));
        context = getBaseContext();
        ordernumber = String.valueOf(getIntent().getExtras().getInt("ordernumber"));
        showdataforproducts();
        backbtn();
    }

    private void backbtn() {
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showprice() {
        for (int i=0;i<arrayList.size();i++){
            totalprice = totalprice + arrayList.get(i).getTotalPrice();
        }
        binding.subtotal.setText("£"+new DecimalFormat("##.##").format(totalprice-(totalprice*0.2)));
        binding.Delivery.setText("£0.00");
        binding.totaltax.setText("£"+new DecimalFormat("##.##").format(totalprice*0.2));
        binding.total.setText("£"+new DecimalFormat("##.##").format(totalprice));
    }

    private void showdataforproducts() {
        databaseReference = FirebaseDatabase.getInstance().getReference("invoices");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    for (DataSnapshot snapshot2: snapshot1.getChildren()){
                        DataSnapshot orderdetailsSnapshot = snapshot2.child("OrderDetails");
                        String orderid = String.valueOf(orderdetailsSnapshot.child("orderid").getValue(int.class));
                        if (orderid==ordernumber){
                            DataSnapshot orderitemSnapshot = snapshot2.child("OrderItems");
                            for (DataSnapshot itemSnapshot : orderitemSnapshot.getChildren()) {
                                Orderitemsummary orderitemsummary = itemSnapshot.getValue(Orderitemsummary.class);
                                arrayList.add(orderitemsummary);
                            }
                            if (arrayList!=null){
                                binding.orderitems.setLayoutManager(new LinearLayoutManager(context));
                                Ordersummaryadapter ordersummaryadapter = new Ordersummaryadapter(arrayList,context);
                                binding.orderitems.setAdapter(ordersummaryadapter);
                                showprice();
                            }
                            DataSnapshot cardSnapshot = snapshot2.child("Card");
                            Card card = cardSnapshot.getValue(Card.class);
                            if (card != null) {
                                cardnumber = card.getCardnumber();
                                binding.cardnumberdetails.setText("Cardnumber ends with **"+cardnumber.substring(12));
                            }
                            DataSnapshot addresssnapshot = snapshot2.child("Address");
                            Address address = addresssnapshot.getValue(Address.class);
                            Log.d("TAG", "onDataChange: "+address.getStreet());
                            if (address!=null){
                                binding.Street.setText(address.getStreet());
                                binding.city.setText(address.getCity());
                                binding.postcode.setText(address.getPostcode());
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}