package com.example.kdwholesale;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kdwholesale.Adapter.Datelistadapter;
import com.example.kdwholesale.Domain.Datesdomain;
import com.example.kdwholesale.databinding.FragmentOrderfragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class orderfragment extends Fragment {

    private FragmentOrderfragmentBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<Datesdomain> dateslist = new ArrayList<>();
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderfragmentBinding.inflate(getLayoutInflater());

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null){
            activity.setSupportActionBar(binding.toolbar);
            activity.getSupportActionBar().setTitle("Orders");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("invoices");
        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    if (snapshot1.hasChild(user.getUid())){
                        DataSnapshot snapshot2 = snapshot1.child(user.getUid());
                        Datesdomain datesdomain = snapshot2.child("OrderDetails").getValue(Datesdomain.class);
                        dateslist.add(datesdomain);
                    }
                }
                if (dateslist!=null){
                    binding.orderrecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
                    Datelistadapter datelistadapter = new Datelistadapter(dateslist,context);
                    binding.orderrecycleview.setAdapter(datelistadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}