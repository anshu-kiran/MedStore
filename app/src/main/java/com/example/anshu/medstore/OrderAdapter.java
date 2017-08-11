package com.example.anshu.medstore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<Order> modelOrder;

    public OrderAdapter(Context context, List<Order> modelOrder) {
        this.context = context;
        this.modelOrder = modelOrder;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent,false);
        return new OrderAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        holder.id.setText("Order Id : \t"+modelOrder.get(position).getId());
        holder.username.setText("Ordered By : \t"+modelOrder.get(position).getUsername());
        holder.orders.setText("Medicine Ordered : \t"+modelOrder.get(position).getOrders());
        holder.cost.setText("Total Cost : \tRs. "+modelOrder.get(position).getCost());
    }

    @Override
    public int getItemCount() {
        return modelOrder.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView id;
        public TextView username;
        public TextView orders;
        public TextView cost;

        public ViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.orderid);
            username = (TextView) itemView.findViewById(R.id.userName);
            orders = (TextView) itemView.findViewById(R.id.orders);
            cost = (TextView) itemView.findViewById(R.id.tcost);
        }

    }
}
