package com.example.kdwholesale;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kdwholesale.Domain.ReadWriteUserDetials;
import com.example.kdwholesale.databinding.FragmentProfilefragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profilefragment extends Fragment {

    private FragmentProfilefragmentBinding binding;
    private FirebaseAuth AuthProfile;

    String useremail;
    String fullname;
    String userdob;
    String usergender;
    String userphone;
    FirebaseUser firebaseUser;

    public Profilefragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfilefragmentBinding.inflate(getLayoutInflater());

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity!= null){
            activity.setSupportActionBar(binding.toolbar);
            activity.getSupportActionBar().setTitle("Profile");
        }

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AuthProfile = FirebaseAuth.getInstance();
        firebaseUser = AuthProfile.getCurrentUser();
        loadprofile();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadprofile();
    }

    private void loadprofile() {
        if (firebaseUser==null){
            Toast.makeText(getContext(), "Something went wrong user information is not there", Toast.LENGTH_SHORT).show();
        }
        else {
            showUserProfile(firebaseUser);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.common_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.menu_logout){
            AuthProfile = FirebaseAuth.getInstance();
            AuthProfile.signOut();
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_updateprofile) {
            Intent intent = new Intent(getContext(),updateprofile.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetials readWriteUserDetials = snapshot.getValue(ReadWriteUserDetials.class);
                if (readWriteUserDetials != null){
                    useremail = firebaseUser.getEmail();
                    fullname = firebaseUser.getDisplayName();
                    userdob = readWriteUserDetials.userdob;
                    userphone = readWriteUserDetials.userphone;
                    usergender = readWriteUserDetials.usergender;

                    binding.profileName2.setText(fullname);
                    binding.profileEmail.setText(useremail);
                    binding.profileDob.setText(userdob);
                    binding.profileGender.setText(usergender);
                    binding.profilePhone.setText(userphone);
                    binding.profileName.setText(fullname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Release the binding when the fragment is destroyed
    }

}