package com.example.kdwholesale.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kdwholesale.Domain.Category;
import com.example.kdwholesale.ListProductActivity;
import com.example.kdwholesale.R;

import java.util.ArrayList;

public class Categoryadapter extends RecyclerView.Adapter<Categoryadapter.Viewholder> {

    ArrayList<Category> categorylist;
    Context context;

    public Categoryadapter(ArrayList<Category> categorylist, Context context) {
        this.categorylist = categorylist;
        this.context = context;
    }

    @NonNull
    @Override
    public Categoryadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category,parent,false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Categoryadapter.Viewholder holder, int position) {
        holder.categoryText.setText(categorylist.get(position).getName());

        Glide.with(context).load(categorylist.get(position).getImagePath())
                .error(R.drawable.baseline_shopping_cart_24)
                .fitCenter()
                .into(holder.categoryimg);
        ;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListProductActivity.class);
                intent.putExtra("Categoryid",categorylist.get(position).getId());
                intent.putExtra("Categoryname",categorylist.get(position).getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView categoryimg;
        TextView categoryText;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            categoryimg = itemView.findViewById(R.id.categoryimg);
            categoryText = itemView.findViewById(R.id.categoryname);
        }
    }
}
