package com.MohammadSharabati.restaurantgenie.ViewHolder;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.MohammadSharabati.restaurantgenie.FoodList;
import com.MohammadSharabati.restaurantgenie.Interface.ItemClickListener;
import com.MohammadSharabati.restaurantgenie.Model.Category;
import com.MohammadSharabati.restaurantgenie.R;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;


public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    private Context mContext;
    private List<Category> categoryList;
    private List<String> keysList;

    public MenuAdapter(Context context, List<Category> categoryList , List<String> keysList) {
        this.mContext = context;
        this.categoryList = categoryList;
        this.keysList = keysList;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(categoryList.get(position).getImage())
                .into(holder.imageView);
        holder.txtMenuName.setText(categoryList.get(position).getName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //Get CategoryId and send to new Activity
                Intent foodList = new Intent(mContext, FoodList.class);
                //Because CategoryId is key, so we just get key of this item
                foodList.putExtra("CategoryId", keysList.get(position));
//                Log.d("CategoryId", adapter.getRef(position).getKey());
                foodList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(foodList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public Category getItem(int position) {
        return categoryList.get(position);
    }

    public void removeItem(int position) {
        categoryList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Category item, int position) {
        categoryList.add(position, item);
        notifyItemInserted(position);
    }
}