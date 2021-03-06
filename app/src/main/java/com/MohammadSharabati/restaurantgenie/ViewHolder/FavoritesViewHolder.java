package com.MohammadSharabati.restaurantgenie.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.MohammadSharabati.restaurantgenie.Interface.ItemClickListener;
import com.MohammadSharabati.restaurantgenie.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Mohammad Sharabati.
 */
public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView food_name, food_price;
    public ImageView food_image, fav_image, quickCart;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    private ItemClickListener itemClickListener;

    public FavoritesViewHolder(View itemView) {
        super(itemView);

        food_name = (TextView) itemView.findViewById(R.id.food_name);
        food_image = (ImageView) itemView.findViewById(R.id.food_image);
        fav_image = (ImageView) itemView.findViewById(R.id.fav);
        food_price = (TextView) itemView.findViewById(R.id.food_price);
        quickCart = (ImageView) itemView.findViewById(R.id.btn_quick_cart);

        view_background = (RelativeLayout) itemView.findViewById(R.id.view_background);
        view_foreground = (LinearLayout) itemView.findViewById(R.id.view_foreground);

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
