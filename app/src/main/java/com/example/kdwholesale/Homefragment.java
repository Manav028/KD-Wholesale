package com.example.kdwholesale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kdwholesale.Adapter.Bestproductadapter;
import com.example.kdwholesale.Adapter.Categoryadapter;
import com.example.kdwholesale.Domain.Products;
import com.example.kdwholesale.Domain.Category;
import com.example.kdwholesale.databinding.FragmentHomefragmentBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Homefragment extends Fragment {


    private FragmentHomefragmentBinding binding;
    Context context;
    ArrayList<SlideModel> sliderimg = new ArrayList<SlideModel>();

    private BottomNavigationView bottombar;

    public Homefragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        bottombar = requireActivity().findViewById(R.id.bottomnav);

        binding = FragmentHomefragmentBinding.inflate(getLayoutInflater());
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null){
            appCompatActivity.setSupportActionBar(binding.toolbar);
            appCompatActivity.getSupportActionBar().setTitle("HomePage");
        }
        context = getContext();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initbestproducts();
        initcategort();
        initslideimg();
        initsearch();
        initbestproductsall();
    }

    private void initbestproductsall() {
        binding.bestproductviewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListProductActivity.class);
                intent.putExtra("bestproductbollean",true);
                startActivity(intent);
            }
        });
    }

    private void initsearch() {
        binding.searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Item_Search.class);
                startActivity(intent);
            }
        });
    }

    private void initslideimg() {
        sliderimg.add(new SlideModel(R.drawable.cleaning, ScaleTypes.FIT));
        sliderimg.add(new SlideModel(R.drawable.christmas,ScaleTypes.FIT));

        binding.imageslider.setImageList(sliderimg,ScaleTypes.FIT);
    }

    private void initcategort() {
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Category");
        binding.progreebarcategory.setVisibility(View.VISIBLE);
        ArrayList<Category> categorylist = new ArrayList<Category>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Category category = dataSnapshot.getValue(Category.class);
                        categorylist.add(category);
                    }
                    if (categorylist.size()>0){
                        binding.recycleviewcategory.setLayoutManager(new GridLayoutManager(context,3));
                        Categoryadapter categoryadapter = new Categoryadapter(categorylist,context);
                        binding.recycleviewcategory.setAdapter(categoryadapter);
                    }
                    binding.progreebarcategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initbestproducts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");
        binding.progressbarbestprodut.setVisibility(View.VISIBLE);
        ArrayList<Products> bestproductslist = new ArrayList<Products>();
        Query query = reference.orderByChild("BestProduct").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Products products = dataSnapshot.getValue(Products.class);
                        bestproductslist.add(products);
                    }
                    if (bestproductslist.size()>0){
                        binding.recycleviewBestproduct.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                        Bestproductadapter adapter = new Bestproductadapter(bestproductslist,context);
                        binding.recycleviewBestproduct.setAdapter(adapter);
                    }
                    binding.progressbarbestprodut.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}