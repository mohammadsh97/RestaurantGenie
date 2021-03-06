package com.MohammadSharabati.restaurantgenie.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.MohammadSharabati.restaurantgenie.Interface.ItemClickListener;
import com.MohammadSharabati.restaurantgenie.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Mohammad Sharabati.
 * Building item on Food List
 */
public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView food_name, food_price_item;
    public ImageView food_image, fav_image, quickCart;

    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);

        food_name = (TextView) itemView.findViewById(R.id.food_name);
        food_image = (ImageView) itemView.findViewById(R.id.food_image);
        fav_image = (ImageView) itemView.findViewById(R.id.fav);
        food_price_item = (TextView) itemView.findViewById(R.id.food_price_item);
        quickCart = (ImageView) itemView.findViewById(R.id.btn_quick_cart);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
