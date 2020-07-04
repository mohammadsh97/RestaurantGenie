package com.MohammadSharabati.restaurantgenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.MohammadSharabati.restaurantgenie.Common.Common;
import com.MohammadSharabati.restaurantgenie.Database.Database;
import com.MohammadSharabati.restaurantgenie.Helper.RecyclerItemTouchHelper;
import com.MohammadSharabati.restaurantgenie.Interface.RecyclerItemTouchHelperListener;
import com.MohammadSharabati.restaurantgenie.Model.DataMessage;
import com.MohammadSharabati.restaurantgenie.Model.Food;
import com.MohammadSharabati.restaurantgenie.Model.MyResponse;
import com.MohammadSharabati.restaurantgenie.Model.Order;
import com.MohammadSharabati.restaurantgenie.Model.Request;
import com.MohammadSharabati.restaurantgenie.Model.Token;
import com.MohammadSharabati.restaurantgenie.Model.User;
import com.MohammadSharabati.restaurantgenie.Remote.APIService;
import com.MohammadSharabati.restaurantgenie.ViewHolder.CartAdapter;
import com.MohammadSharabati.restaurantgenie.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Mohammad Sharabati.
 */

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference requests;
    private DatabaseReference foods;
    public TextView txtTotalPlace;
    private FButton btnPlace;
    public static List<Order> cart = new ArrayList<>();
    private CartAdapter adapter;
    APIService mService;
    private RelativeLayout rootLayout;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mService = Common.getFCMService();


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Requests");
        foods = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Foods");

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalPlace = (TextView) findViewById(R.id.total);
        btnPlace = (FButton) findViewById(R.id.btnPlaceOrder);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        // Swipe to Delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        /**
         * Clicking Place Order button
         */
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new Request
                if (cart.size() > 0) {
                    showAlertDialog();
                } else
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
        alertDialog.setTitle("One more step");
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
                dialogInterface.dismiss();
                // Create new Resquest
//                Request request = new Request(
//                        Common.currentUser.getPhone(),
//                        Common.currentUser.getName(),
//                        edtNote.getText().toString(),
//                        txtTotalPlace.getText().toString(),
//                        cart
//                );
                Common.request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtNote.getText().toString(),
                        txtTotalPlace.getText().toString(),
                        cart
                );
                // Submit to Firebase
                // We Will using System.CurrentMilli to key

                String order_number = String.valueOf(System.currentTimeMillis());

                requests.child(order_number).setValue(Common.request).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Common.completeListener(foods);

//                        for (int run = 0; run < request.getFoods().size(); run++) {
//                            Order orderRequest = request.getFoods().get(run);
//
//                            foods.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    for (DataSnapshot snapshotFoods : dataSnapshot.getChildren()) {
//
//                                        Food modelFood = snapshotFoods.getValue(Food.class);
//                                        if (modelFood.getName().equals(orderRequest.getProductName())) {
//                                            int changeCount = Integer.parseInt(modelFood.getCounter()) + Integer.parseInt(orderRequest.getQuantity());
//
//                                            Food tempFood = new Food(modelFood.getName(), modelFood.getImage(), modelFood.getDescription(),
//                                                    modelFood.getPrice(), modelFood.getDiscount(), modelFood.getMenuId(), String.valueOf(changeCount));
//
//
//                                            foods.child(snapshotFoods.getKey()).setValue(tempFood)
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            Log.v("mohammad" , "dont wary");
//                                                        }
//                                                    });
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//////////////////////////////////////////////////////////////////////////////////////////////////////
                        Toast.makeText(Cart.this, "Thank you, order place", Toast.LENGTH_SHORT).show();
//                        finish();

                        //Get CategoryId and send to new Activity
                        Intent foodList = new Intent(Cart.this, Home.class);

                        startActivity(foodList);


                        //Delete Cart
                        new Database(Cart.this).cleanCart(Common.currentUser.getPhone());
                        sendNotification(order_number);
                    }
                });

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


    private void sendNotification(final String order_number) {
        DatabaseReference tokens = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Tokens");
        Query data = tokens.orderByChild("serverToken").equalTo(true); // get all node with isServerToken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Token serverToken = null;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    serverToken = postSnapshot.getValue(Token.class);
                }

                Map<String, String> dataSend = new HashMap<>();
                dataSend.put("title", "MohammadSh");
                dataSend.put("message", "Your order came in!" + order_number);
                DataMessage dataMessage = new DataMessage(serverToken != null ? serverToken.getToken() : null, dataSend);

                String test = new Gson().toJson(dataMessage);
                Log.d("Content", test);

                mService.sendNotification(dataMessage)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                //Only run when get result
                                if (response.code() == 200) {
                                    if (response.body().success == 1) {

                                    } else {
                                        Toast.makeText(Cart.this, "Failed !!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.e("ERROR", t.getMessage());
                            }
                        });
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
        cart = new Database(this).getCarts(Common.currentUser.getPhone());
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

        Locale locale = new Locale("en", "ILS");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

//        viewHolder.food_price_item.setText(String.format("₪ %s", model.getPrice()));
//        txtTotalPlace.setText(fmt.format(total));
        txtTotalPlace.setText(String.format("₪ %s", total));
    }

    // Update / Delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());

        return true;
    }

    private void deleteCart(int position) {
        //remove item at List<Order> by position
        cart.remove(position);

        //after that, delete all old data from SQLite
        new Database(this).cleanCart(Common.currentUser.getPhone());

        //after final, update new data from List<Order> to SQLite
        for (Order item : cart)
            new Database(this).addToCart(item);

        //refresh
        loadListFood();

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            final Order deleteItem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()); // item
            final int deleteIndex = viewHolder.getAdapterPosition(); // position

            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId(), Common.currentUser.getPhone());

            //update
            //Update txtTotal
            //Calculate total price
            int total = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
            for (Order item : orders)
                total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));

//            Locale locale = new Locale("en", "ILS");
//            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//            txtTotalPlace.setText(fmt.format(total));

            txtTotalPlace.setText(String.format("₪ %s", total));

            //SnackBar
            Snackbar snackbar = Snackbar.make(rootLayout, name + "Remove from cart", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);
                    //Update txtTotal
                    //Calculate total price
                    int total = 0;
                    List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
                    for (Order item : orders)
                        total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));

//                    Locale locale = new Locale("en", "ILS");
//                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//                    txtTotalPlace.setText(fmt.format(total));

                    txtTotalPlace.setText(String.format("₪ %s", total));
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

}
