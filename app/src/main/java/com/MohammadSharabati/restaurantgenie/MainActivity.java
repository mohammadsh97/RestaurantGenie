package com.MohammadSharabati.restaurantgenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.MohammadSharabati.restaurantgenie.Common.Common;
import com.MohammadSharabati.restaurantgenie.Interface.ItemClickListener;
import com.MohammadSharabati.restaurantgenie.Model.Category;
import com.MohammadSharabati.restaurantgenie.Model.User;
import com.MohammadSharabati.restaurantgenie.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Mohammad Sharabati.
 */

public class MainActivity extends AppCompatActivity {


    private FirebaseRecyclerOptions<Category> options;
    private FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    private FirebaseDatabase database2;
    private DatabaseReference category;
    private RecyclerView recycler_menu;
    private RecyclerView.LayoutManager layoutManager;


    private FirebaseDatabase database;
    private DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);


        String bN = Paper.book().read(Common.USER_BN);
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if (bN != null && user != null && pwd != null) {
            if (!bN.isEmpty() && !user.isEmpty() && !pwd.isEmpty()) {

                database = FirebaseDatabase.getInstance();
                table_user = database.getReference("RestaurantGenie");

                login(bN, user, pwd);
            }
        } else {
            Intent signIn = new Intent(MainActivity.this, SignIn.class);
            startActivity(signIn);
        }
    }

    private void login(final String BusinessNumber, final String Name, final String password) {

        if (Common.isConnectedToInternet(getBaseContext())) {

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please waiting...");
            mDialog.show();
            if (BusinessNumber.trim().length() != 0) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(BusinessNumber).exists()) {

                            mDialog.dismiss();
                            for (DataSnapshot snapshot : dataSnapshot.child(BusinessNumber).child("Worker").child("Table").getChildren()) {

                                User model = snapshot.getValue(User.class);

                                // check Name and password for staff
                                if (model.getName().equals(Name) && model.getPassword().equals(password)) {
                                    // Login ok
                                    // Save user & Password

                                    Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                    Common.currentUser = model;
                                    startActivity(homeIntent);
                                    finish();
                                }
                            }
                            if (Common.currentUser == null) {
                                Toast.makeText(MainActivity.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                mDialog.dismiss();
                Toast.makeText(MainActivity.this, "Complete the blank sentences!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(MainActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("TAG" , "MainActivity => onResume");
    }
}
