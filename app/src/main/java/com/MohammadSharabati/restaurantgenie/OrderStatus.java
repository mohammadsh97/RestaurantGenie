package com.MohammadSharabati.restaurantgenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.MohammadSharabati.restaurantgenie.Common.Common;
import com.MohammadSharabati.restaurantgenie.Interface.ItemClickListener;
import com.MohammadSharabati.restaurantgenie.ViewHolder.OrderViewHolder;
import com.MohammadSharabati.restaurantgenie.Model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderStatus extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference requests;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.recyclerOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Fix click back on FoodDetail and get no item in FoodList
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    /**
     * Loading order status
     */
    private void loadOrders(String phone) {

        Query getOrderByUser = requests.orderByChild("phone").equalTo(phone);

        FirebaseRecyclerOptions<Request> orderOptions = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(getOrderByUser, Request.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(orderOptions) {
            @Override
            public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);

                return new OrderViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position, @NonNull final Request model) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderNote.setText(model.getNote());
                viewHolder.txtOrderPhone.setText(model.getPhone());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(OrderStatus.this, "your order status is: " + Common.convertCodeToStatus(model.getStatus()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
