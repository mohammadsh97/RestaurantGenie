package com.MohammadSharabati.restaurantgenie.ViewHolder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.MohammadSharabati.restaurantgenie.R;
import androidx.recyclerview.widget.RecyclerView;
/**
 * Created by Mohammad Sharabati.
 */

public class ShowCommentViewHolder extends RecyclerView.ViewHolder  {
    public TextView txtUserName, txtComment;
    public RatingBar ratingBar;

    public ShowCommentViewHolder(View itemView) {
        super(itemView);
        txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
        txtComment = (TextView) itemView.findViewById(R.id.txtComment);
        ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

    }

}
