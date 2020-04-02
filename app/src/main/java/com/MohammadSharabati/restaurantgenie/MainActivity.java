package com.MohammadSharabati.restaurantgenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.MohammadSharabati.restaurantgenie.Common.Common;
import com.MohammadSharabati.restaurantgenie.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

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
                login(bN, user, pwd);
            }
        }
        else{
            Intent signIn = new Intent(MainActivity.this,SignIn.class);
            startActivity(signIn);
        }
    }

    private void login(final String BusinessNumber, final String Name, final String password) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("RestaurantGenie");

        if (Common.isConnectedToInternet(getBaseContext())) {

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please waiting...");
            mDialog.show();
            if (BusinessNumber.trim().length() != 0) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Chech if user not exist in databases
                        if (dataSnapshot.child(BusinessNumber).exists()) {
                            // Get user information
                            mDialog.dismiss();
                            User user = dataSnapshot.child(BusinessNumber).child("Worker").child("Table").getValue(User.class);
                            user.setBusinessNumber(BusinessNumber);
                            if (user.getName().equals(Name) && user.getPassword().equals(password)) {
                                {
                                    Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                }
                            } else
                                Toast.makeText(MainActivity.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
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
}
