package com.MohammadSharabati.restaurantgenie.ViewHolder;

import android.view.View;
import android.widget.TextView;
import com.MohammadSharabati.restaurantgenie.Interface.ItemClickListener;
import com.MohammadSharabati.restaurantgenie.R;
import androidx.recyclerview.widget.RecyclerView;


public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderNote;


    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderNote = (TextView) itemView.findViewById(R.id.order_note);
        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

}
