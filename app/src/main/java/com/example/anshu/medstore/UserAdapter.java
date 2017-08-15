package com.example.anshu.medstore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> modelUser;

    public UserAdapter(Context context, List<User> modelUser) {
        this.context = context;
        this.modelUser = modelUser;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent,false);
        return new UserAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        holder.username.setText("Username : \t"+modelUser.get(position).getUsername());
        holder.address.setText("Address : \t"+modelUser.get(position).getAddress());
        holder.phone.setText("Phone Number : \t"+modelUser.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return modelUser.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView username;
        public TextView address;
        public TextView phone;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            address = (TextView) itemView.findViewById(R.id.address);
            phone = (TextView) itemView.findViewById(R.id.phone);
        }
    }
}
