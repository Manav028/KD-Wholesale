package com.example.kdwholesale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kdwholesale.databinding.ActivityHomepageBinding;
import com.google.android.material.navigation.NavigationBarView;

public class Homepage extends AppCompatActivity {

    private ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.nav_account){
                    loadfragment(new Profilefragment());
                }
                else if (id==R.id.nav_cart) {
                    loadfragment(new CartFragment());
                } else if (id==R.id.nav_order) {
                    loadfragment(new orderfragment());
                } else {
                    loadfragment(new Homefragment());
                }
                return true;
            }
        });

        binding.bottomnav.setSelectedItemId(R.id.nav_home);
    }

    public void loadfragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.commit();
    }
}