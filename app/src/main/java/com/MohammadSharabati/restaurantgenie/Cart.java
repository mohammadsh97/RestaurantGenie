package com.MohammadSharabati.restaurantgenie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.MohammadSharabati.restaurantgenie.Common.Common;
import com.MohammadSharabati.restaurantgenie.Database.Database;
import com.MohammadSharabati.restaurantgenie.Model.MyResponse;
import com.MohammadSharabati.restaurantgenie.Model.Notification;
import com.MohammadSharabati.restaurantgenie.Model.Order;
import com.MohammadSharabati.restaurantgenie.Model.Request;
import com.MohammadSharabati.restaurantgenie.Model.Sender;
import com.MohammadSharabati.restaurantgenie.Model.Token;
import com.MohammadSharabati.restaurantgenie.Remote.APIService;
import com.MohammadSharabati.restaurantgenie.ViewHolder.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference requests;
    public TextView txtTotalPlace;
    private FButton btnPlace;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;
    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mService = Common.getFCMService();

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalPlace = (TextView) findViewById(R.id.total);
        btnPlace = (FButton) findViewById(R.id.btnPlaceOrder);
        /**
         * Clicking Place Order button
         */
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new Request
                if (cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your cart is empty !!!", Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();
    }
    /**
     * Showing alert dialog
     */
    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your notes:");

        final EditText edtNote = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtNote.setLayoutParams(lp);
        alertDialog.setView(edtNote); // Add edit Text to alert dialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Create new Resquest
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtNote.getText().toString(),
                        txtTotalPlace.getText().toString(),
                        cart
                );

                // Submit to Firebase
                // We Will using System.CurrentMilli to key

                String order_number = String.valueOf(System.currentTimeMillis());

                requests.child(order_number).setValue(request);
                //Delete Cart
                new Database(getBaseContext()).cleanCart();

                sendNotificationOrder(order_number);

                Toast.makeText(Cart.this, "Thank you, order place", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void sendNotificationOrder(final String order_number) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("isServerToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Token serverToken = postSnapshot.getValue(Token.class);

                    Notification notification = new Notification("Restaurant Genie", "You have new Order" + order_number);
                    Sender content = new Sender(serverToken.getToken(), notification);

                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            Toast.makeText(Cart.this, "Thank you, Order placed", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Cart.this, "Failed !!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("error", t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Loading food list
     */
    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calculate total price
        int total = 0;
        for (Order order : cart) {
            try {
                total += ((Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity())));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPlace.setText(fmt.format(total));
    }

    // Update / Delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)) {
            deleteCart(item.getOrder());
        }

        return true;
    }

    private void deleteCart(int position) {
        // We will remove item at List<Order> by position
        cart.remove(position);

        // After that, we will delete all old data from SQLite
        new Database(this).cleanCart();
        // And final, we will update new data from List<Order> to SQLite
        for (Order item:cart)
            new Database(this).addToCart(item);

        // Refresh
        loadListFood();
    }
}
