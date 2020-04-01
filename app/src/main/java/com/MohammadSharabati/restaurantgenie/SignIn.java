package com.MohammadSharabati.restaurantgenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.MohammadSharabati.restaurantgenie.Common.Common;
import com.MohammadSharabati.restaurantgenie.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Checking when user sign in app
 */
public class SignIn extends AppCompatActivity {

    private MaterialEditText edtBusinessNumber, edtName, edtPassword;
    private Button btnSignIn;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtBusinessNumber = (MaterialEditText) findViewById(R.id.edtBusinessNumber);
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //Int Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("RestaurantGenie");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();
                    if (edtBusinessNumber.getText().toString().trim().length() != 0) {
                        table_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Chech if user not exist in databases
                                if (dataSnapshot.child(edtBusinessNumber.getText().toString()).exists()) {
                                    // Get user information
                                    mDialog.dismiss();
                                    user = dataSnapshot.child(edtBusinessNumber.getText().toString()).child("Worker").child("Table").getValue(User.class);
                                    user.setBusinessNumber(edtBusinessNumber.getText().toString());
                                    if (user.getName().equals(edtName.getText().toString()) && user.getPassword().equals(edtPassword.getText().toString())) {
                                        {
                                            Intent homeIntent = new Intent(SignIn.this, Home.class);
                                            Common.currentUser = user;
                                            startActivity(homeIntent);
                                            finish();
                                        }
                                    } else
                                        Toast.makeText(SignIn.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignIn.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        mDialog.dismiss();
                        Toast.makeText(SignIn.this, "Complete the blank sentences!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(SignIn.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
