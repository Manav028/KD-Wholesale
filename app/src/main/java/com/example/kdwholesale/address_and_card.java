package com.example.kdwholesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kdwholesale.Domain.Address;
import com.example.kdwholesale.Domain.Card;
import com.example.kdwholesale.Domain.Datesdomain;
import com.example.kdwholesale.Domain.Products;
import com.example.kdwholesale.Domain.UserCart;
import com.example.kdwholesale.databinding.ActivityAddressAndCardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class address_and_card extends AppCompatActivity {

    ArrayList<UserCart> cartlist = new ArrayList<>();
    private ActivityAddressAndCardBinding binding;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("invoices");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserCart");
    DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("Products");
    int invoiceid;
    CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_and_card);

        binding = ActivityAddressAndCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        cartFragment = new CartFragment();

        getdata();
        checkinput();
        backbtn();

        binding.carddatetext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String date = s.toString();
                if (date.length()==2 && !date.toString().contains("/")){
                    s.append("/");
                }
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

    private void getdata() {
        cartlist = (ArrayList<UserCart>) getIntent().getSerializableExtra("objectofcart");
    }

    private void checkinput() {
        binding.paynowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.addresstext.getText().toString())){
                    checkcondition(binding.addresslayout.getId(),"Please enter Street");
                } else if (TextUtils.isEmpty(binding.citytext.getText().toString())) {
                    checkcondition(binding.citylayout.getId(),"Please enter City");
                } else if (TextUtils.isEmpty(binding.postcodetext.getText().toString())) {
                    checkcondition(binding.postcodelayout.getId(),"Please enter PostCode");
                } else if (TextUtils.isEmpty(binding.cardholdertext.getText().toString())) {
                    checkcondition(binding.cardholderlayout.getId(),"Please enter CardHolder Name");
                } else if (TextUtils.isEmpty(binding.cardnumbertext.getText().toString())) {
                    checkcondition(binding.cardnumberlayout.getId(),"Please enter Card Number");
                } else if (binding.cardnumbertext.getText().toString().length()!=16) {
                    checkcondition(binding.cardnumberlayout.getId(),"Please enter correct Card number");
                } else if (TextUtils.isEmpty(binding.carddatetext.getText().toString())) {
                    checkcondition(binding.carddatelayout.getId(),"Please enter Card Expiry Date");
                } else if (TextUtils.isEmpty(binding.cardCVVtext.getText().toString())) {
                    checkcondition(binding.cardCVVlayout.getId(),"Please enter CVV");
                }else {
                    binding.addresslayout.setErrorEnabled(false);
                    binding.citylayout.setErrorEnabled(false);
                    binding.postcodelayout.setErrorEnabled(false);
                    binding.cardholderlayout.setErrorEnabled(false);
                    binding.cardnumberlayout.setErrorEnabled(false);
                    binding.carddatelayout.setErrorEnabled(false);
                    binding.cardCVVlayout.setErrorEnabled(false);
                    processorder();
                }
            }
        });
    }

    private void processorder() {
        Toast.makeText(this, "Order place succesfully", Toast.LENGTH_SHORT).show();
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   invoiceid = (int) (snapshot.getChildrenCount() + 1);
                }
                else {
                    invoiceid = 1;
                }
                createinvoice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void createinvoice() {

        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("invoices").child(String.valueOf(invoiceid)).child(firebaseUser.getUid());

        for (int i=0;i<cartlist.size();i++){
            int finalI = i;
            databaseReference3.child("OrderItems").child(String.valueOf(cartlist.get(finalI).getID())).setValue(cartlist.get(finalI)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        }

        databaseReference.child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(address_and_card.this, "Cart is empty", Toast.LENGTH_SHORT).show();


                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                    String toadydate = simpleDateFormat.format(date);

                    Datesdomain datesdomain = new Datesdomain(toadydate,invoiceid);

                    databaseReference3.child("OrderDetails").setValue(datesdomain).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                    Address address = new Address(binding.addresstext.getText().toString(),binding.citytext.getText().toString(),binding.postcodetext.getText().toString());
                    databaseReference3.child(String.valueOf("Address")).setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Card card = new Card(binding.cardholdertext.getText().toString(),binding.cardnumbertext.getText().toString(),binding.carddatetext.getText().toString(),binding.cardCVVtext.getText().toString());
                                databaseReference3.child(String.valueOf("Card")).setValue(card).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            for (int i=0;i<cartlist.size();i++){
                                                int finalI = i;
                                                databaseReference4.child(String.valueOf(cartlist.get(finalI).getID())).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()){
                                                            Products products = snapshot.getValue(Products.class);
                                                            Map<String,Object> updatedata = new HashMap<>();
                                                            updatedata.put("Quantity",products.getQuantity()-cartlist.get(finalI).getProductQuantity());
                                                            databaseReference4.child(String.valueOf(cartlist.get(finalI).getID())).updateChildren(updatedata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        Toast.makeText(address_and_card.this, "quantity decrease", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                            Intent intent = new Intent(address_and_card.this, Order_confirmation.class);
                                            startActivity(intent);
                                            address_and_card.this.finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    public void checkcondition(Integer layoutid,String layoutmessage){
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(binding.addresslayout.getId());
        arrayList.add(binding.citylayout.getId());
        arrayList.add(binding.postcodelayout.getId());
        arrayList.add(binding.cardholderlayout.getId());
        arrayList.add(binding.cardnumberlayout.getId());
        arrayList.add(binding.cardCVVlayout.getId());
        arrayList.add(binding.carddatelayout.getId());

        arrayList.remove(layoutid);
        TextInputLayout textInputLayout = findViewById(layoutid);

        textInputLayout.setError(layoutmessage);

        for (int i = 0; i < 6; i++) {
            Log.d("TAG", "checkcondition: "+arrayList.get(i));
            TextInputLayout textInputLayout1 = findViewById(arrayList.get(i));
            textInputLayout1.setErrorEnabled(false);
        }
    }
}
