package com.example.kdwholesale.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kdwholesale.Domain.Products;
import com.example.kdwholesale.R;
import com.example.kdwholesale.product_details;

import java.util.ArrayList;

public class Productsadapter extends RecyclerView.Adapter<Productsadapter.Viewholder> {

    ArrayList<Products> productlist = new ArrayList<>();
    Context context;

    public Productsadapter(ArrayList<Products> productlist, Context context) {
        this.productlist = productlist;
        this.context = context;
    }

    @NonNull
    @Override
    public Productsadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.productlsit,parent,false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Productsadapter.Viewholder holder, int position) {
        holder.producttitle.setText(productlist.get(position).getTitle());
        holder.productprice.setText(String.valueOf(productlist.get(position).getPrice()));
        Glide.with(context).load(productlist.get(position).getImagePath()).error(R.drawable.baseline_shopping_cart_24).fitCenter().into(holder.productimage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, product_details.class);
                intent.putExtra("object",productlist.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView productimage;
        TextView producttitle,productprice;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productimage = itemView.findViewById(R.id.productimage);
            producttitle = itemView.findViewById(R.id.producttitle);
            productprice = itemView.findViewById(R.id.productprice);
        }
    }
}
