package com.MohammadSharabati.restaurantgenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.MohammadSharabati.restaurantgenie.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtBusinessNumber, edtEmail, edtPhone, edtName, edtPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtBusinessNumber = (MaterialEditText) findViewById(R.id.edtBusinessNumber);
        edtEmail = (MaterialEditText) findViewById(R.id.edtEmail);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //Int Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("RestaurantGenie");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check if already Business Number
                        if (dataSnapshot.child(edtBusinessNumber.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Business Number already register", Toast.LENGTH_SHORT).show();
                        } else {
                            mDialog.dismiss();
                            User user = new User(edtBusinessNumber.getText().toString(), edtEmail.getText().toString(), edtPhone.getText().toString(), edtName.getText().toString(), edtPassword.getText().toString());
                            table_user.child(edtBusinessNumber.getText().toString()).child("Worker").child("Manager").setValue(user);
                            Toast.makeText(SignUp.this, "sign up successfully !", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}

