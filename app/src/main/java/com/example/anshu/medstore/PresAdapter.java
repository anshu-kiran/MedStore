package com.example.anshu.medstore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PresAdapter extends RecyclerView.Adapter<PresAdapter.ViewHolder> {

    private Context context;
    private List<Pres> pres;

    public PresAdapter(Context context, List<Pres> pres) {
        this.context = context;
        this.pres = pres;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pres_card,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.orderid.setText("Order Id : \t"+pres.get(position).getId());
        holder.username.setText("Ordered By : \t"+pres.get(position).getUsername());
        Glide.with(context).load(pres.get(position).getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return pres.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView orderid;
        public TextView username;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            orderid = (TextView) itemView.findViewById(R.id.ordrid);
            username = (TextView) itemView.findViewById(R.id.user);
            imageView = (ImageView) itemView.findViewById(R.id.presimage);
        }
    }
}

