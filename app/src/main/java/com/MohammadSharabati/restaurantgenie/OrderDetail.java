package com.MohammadSharabati.restaurantgenie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.MohammadSharabati.restaurantgenie.Common.Common;
import com.MohammadSharabati.restaurantgenie.ViewHolder.OrderDetailAdapter;

/**
 * Created by Mohammad Sharabati.
 */

public class OrderDetail extends AppCompatActivity {
    private TextView order_id, order_phone, order_note, order_total;
    private String order_id_value = "";
    private RecyclerView lstFood;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id = (TextView) findViewById(R.id.order_id);
        order_phone = (TextView) findViewById(R.id.order_phone);
        order_note = (TextView) findViewById(R.id.order_note);
        order_total = (TextView) findViewById(R.id.order_total);

        lstFood = (RecyclerView) findViewById(R.id.lstFoods);
        lstFood.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstFood.setLayoutManager(layoutManager);

        if (getIntent() != null)
            order_id_value = getIntent().getStringExtra("OrderId");

        //Set Value
        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getPhone());
        order_total.setText(Common.currentRequest.getTotal());
        order_note.setText(Common.currentRequest.getNote());

        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getFoods() , this);
        adapter.notifyDataSetChanged();
        lstFood.setAdapter(adapter);

    }
}