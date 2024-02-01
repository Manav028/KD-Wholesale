package com.example.kdwholesale.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kdwholesale.Domain.Datesdomain;
import com.example.kdwholesale.R;
import com.example.kdwholesale.ordersummary;

import java.util.ArrayList;

public class Datelistadapter extends RecyclerView.Adapter<Datelistadapter.viewholder> {

    ArrayList<Datesdomain> datelist = new ArrayList<>();
    Context context;

    public Datelistadapter(ArrayList<Datesdomain> datelist, Context context) {
        this.datelist = datelist;
        this.context = context;
    }

    @NonNull
    @Override
    public Datelistadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderlists,parent,false);
        viewholder viewholder1 = new viewholder(view);
        return viewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull Datelistadapter.viewholder holder, int position) {
        Log.d("TAg", "onBindViewHolder: "+datelist.get(position).getDate());
        holder.orderdate.setText(datelist.get(position).getDate());
        Log.d("TAG", "onBindViewHolder: "+datelist.get(position).getOrderid());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ordersummary.class);
                intent.putExtra("ordernumber",datelist.get(position).getOrderid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datelist.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        TextView orderdate,orderprice;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            orderdate = itemView.findViewById(R.id.orderdate);
        }
    }
}
