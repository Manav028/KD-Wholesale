package com.example.kdwholesale.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kdwholesale.Domain.Orderitemsummary;
import com.example.kdwholesale.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Ordersummaryadapter extends RecyclerView.Adapter<Ordersummaryadapter.viewholder> {

    ArrayList<Orderitemsummary> arrayList = new ArrayList<>();
    Context context;

    public Ordersummaryadapter(ArrayList<Orderitemsummary> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Ordersummaryadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ordersummaryproduct,parent,false);
        viewholder viewholder1 = new viewholder(view);
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull Ordersummaryadapter.viewholder holder, int position) {
        holder.producttitle.setText(arrayList.get(position).getProductTitle());
        holder.productqunatitytext.setText(arrayList.get(position).getProductQuantity()+" * £"+arrayList.get(position).getPrice());
        holder.totalprice.setText("£"+arrayList.get(position).getTotalPrice());
        Glide.with(context).load(arrayList.get(position).getImagePath()).fitCenter().into(holder.productimage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView producttitle,productqunatitytext,totalprice;
        ImageView productimage;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            producttitle = itemView.findViewById(R.id.producttitle);
            productqunatitytext = itemView.findViewById(R.id.productqunatitytext);
            totalprice = itemView.findViewById(R.id.totalprice);
            productimage = itemView.findViewById(R.id.productimage);
        }
    }
}
