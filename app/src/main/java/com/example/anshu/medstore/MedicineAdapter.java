package com.example.anshu.medstore;


import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder>   {

    private Context context;
    private List<Medicine> medicines;

    public MedicineAdapter(Context context, List<Medicine> medicines) {
        this.context = context;
        this.medicines = medicines;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.med_card, parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.medName.setText(medicines.get(position).getMedName());
        holder.medPrice.setText(medicines.get(position).getMedPrice());
        Glide.with(context).load(medicines.get(position).getMedLink()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView medName;
        public TextView medPrice;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            medName = (TextView) itemView.findViewById(R.id.medname);
            imageView = (ImageView) itemView.findViewById(R.id.medimage);
            medPrice = (TextView) itemView.findViewById(R.id.medprice);
            imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            showPopupMenu(v,position);
        }
    }

    private void showPopupMenu(View view, int poaition) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.med_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MenuClickListener(poaition));
        popup.show();
    }


    class MenuClickListener implements PopupMenu.OnMenuItemClickListener {
        Integer pos;
        public MenuClickListener(int pos) {
            this.pos=pos;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add:
                    Toast.makeText(context, medicines.get(pos).getMedName()+" is added to cart", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.action_remove:
                    Toast.makeText(context, medicines.get(pos).getMedName()+" is removed from cart", Toast.LENGTH_SHORT).show();
                    return true;

                default:
            }
            return false;
        }


    }
}
