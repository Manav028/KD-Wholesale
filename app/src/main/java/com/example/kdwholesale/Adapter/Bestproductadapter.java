package com.example.kdwholesale.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.kdwholesale.Domain.Products;
import com.example.kdwholesale.R;
import com.example.kdwholesale.product_details;

import java.util.ArrayList;

public class Bestproductadapter extends RecyclerView.Adapter<Bestproductadapter.viewholder> {

    ArrayList<Products> bestproduct;
    Context context;

    public Bestproductadapter(ArrayList<Products> bestproduct,Context context) {
        this.bestproduct = bestproduct;
        this.context = context;
    }

    @NonNull
    @Override
    public Bestproductadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bestproduct,parent,false);
        viewholder viewholder = new viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Bestproductadapter.viewholder holder, int position) {
        holder.ProductPrice.setText("£"+Double.toString(bestproduct.get(position).getPrice()));
        holder.Producttitle.setText(bestproduct.get(position).getTitle());

        Glide.with(context).load(bestproduct.get(position).getImagePath())
                .error(R.drawable.baseline_shopping_cart_24)
                .fitCenter()
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, product_details.class);
                intent.putExtra("object",bestproduct.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bestproduct.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView Producttitle,ProductPrice;
        ImageView imageView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            Producttitle = itemView.findViewById(R.id.producttitle);
            ProductPrice = itemView.findViewById(R.id.productprice);
            imageView = itemView.findViewById(R.id.productimage);
        }
    }
}
