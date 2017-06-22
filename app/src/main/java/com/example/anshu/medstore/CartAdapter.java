package com.example.anshu.medstore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anshu on 6/20/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Myholder> {
    List<Product> products;
    private Context context;
    SQLiteHandler db;

    public CartAdapter(Context context, List<Product> products) {
        this.products = products;
        this.context = context;
    }

    public CartAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card ,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        Product product=products.get(position);
        holder.name.setText("Name : \t"+product.getName());
        holder.price.setText("Price : \tRs."+product.getPrice());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView name, price;

        public Myholder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.medName);
            price = (TextView) itemView.findViewById(R.id.medPrice);
        }
    }
}
