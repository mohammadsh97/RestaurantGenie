package com.MohammadSharabati.restaurantgenie.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.MohammadSharabati.restaurantgenie.Interface.ItemClickListener;
import com.MohammadSharabati.restaurantgenie.R;
import androidx.recyclerview.widget.RecyclerView;
/**
 * Created by Mohammad Sharabati.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderDate, txtOrderStatus, order_table, txtOrderNote;
    public Button btnDetail;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);


        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);
        txtOrderDate = (TextView) itemView.findViewById(R.id.order_date);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
        order_table = (TextView) itemView.findViewById(R.id.order_table);
        txtOrderNote = (TextView) itemView.findViewById(R.id.order_note);

        btnDetail = (Button) itemView.findViewById(R.id.btnDetail);

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
