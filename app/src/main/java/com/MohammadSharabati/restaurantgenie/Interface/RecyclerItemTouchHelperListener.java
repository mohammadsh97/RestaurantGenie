package com.MohammadSharabati.restaurantgenie.Interface;


import androidx.recyclerview.widget.RecyclerView;
/**
 * Created by Mohammad Sharabati.
 */
public interface RecyclerItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
