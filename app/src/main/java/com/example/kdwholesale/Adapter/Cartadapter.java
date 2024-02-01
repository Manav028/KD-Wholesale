package com.example.kdwholesale.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kdwholesale.CartFragment;
import com.example.kdwholesale.Domain.Products;
import com.example.kdwholesale.Domain.UserCart;
import com.example.kdwholesale.ListProductActivity;
import com.example.kdwholesale.R;
import com.example.kdwholesale.product_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Cartadapter extends RecyclerView.Adapter<Cartadapter.viewholder> {

    Context context;
    ArrayList<UserCart> userCarts = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;;
    ArrayList<Products> productlist = new ArrayList<>();
    CartFragment cartFragment;

    public Cartadapter(Context context, ArrayList<UserCart> userCarts) {
        this.context = context;
        this.userCarts = userCarts;
    }

    public Cartadapter(Context context, ArrayList<UserCart> userCarts,CartFragment cartFragment) {
        this.context = context;
        this.userCarts = userCarts;
        this.cartFragment = cartFragment;
    }


    @NonNull
    @Override
    public Cartadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_products,parent,false);
        viewholder viewholder1 = new viewholder(view);
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull Cartadapter.viewholder holder, int position) {
        Glide.with(context).load(userCarts.get(position).getImagePath())
                .fitCenter()
                .into(holder.productimage);
        holder.producttitle.setText(userCarts.get(position).getProductTitle());
        holder.productqunatitytext.setText(""+userCarts.get(position).getProductQuantity()+" * "+userCarts.get(position).getPrice());
        holder.totalprice.setText("£"+userCarts.get(position).getTotalPrice());

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserCart").child(firebaseUser.getUid());
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Products");

        holder.removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position >= 0 && position < userCarts.size()) {

                    UserCart removedItem = userCarts.get(position);
                    userCarts.remove(position);
                    notifyDataSetChanged();

                    databaseReference.child(String.valueOf(removedItem.getID())).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Item removed!", Toast.LENGTH_SHORT).show();
                                        cartFragment.totalitem();
                                        cartFragment.showcart();
                                    } else {
                                        userCarts.add(position, removedItem);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(context, "Invalid operation", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = databaseReference2.orderByChild("ID").equalTo(userCarts.get(position).getID());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1: snapshot.getChildren()){
                                productlist.clear();
                                Products products = snapshot1.getValue(Products.class);
                                productlist.add(products);
                            }
                            if (productlist.size()>0){
                                Intent intent = new Intent(context, product_details.class);
                                intent.putExtra("object",productlist.get(0));
                                context.startActivity(intent);
                            }
                            else{
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return userCarts.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        ImageView productimage;
        TextView producttitle,productqunatitytext,productquantity,totalprice,removebtn;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            productimage = itemView.findViewById(R.id.productimage);
            producttitle = itemView.findViewById(R.id.producttitle);
            productqunatitytext = itemView.findViewById(R.id.productqunatitytext);
            totalprice = itemView.findViewById(R.id.totalprice);
            removebtn = itemView.findViewById(R.id.remove);
        }
    }
}
