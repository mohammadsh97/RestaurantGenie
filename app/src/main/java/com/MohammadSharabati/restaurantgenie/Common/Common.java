package com.MohammadSharabati.restaurantgenie.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.MohammadSharabati.restaurantgenie.Model.Food;
import com.MohammadSharabati.restaurantgenie.Model.Order;
import com.MohammadSharabati.restaurantgenie.Model.Request;
import com.MohammadSharabati.restaurantgenie.Model.User;
import com.MohammadSharabati.restaurantgenie.Remote.APIService;
import com.MohammadSharabati.restaurantgenie.Remote.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * Created by Mohammad Sharabati.
 */
public class Common {

    public static Request request;
    public static User currentUser;
    public static final String DELETE = "Delete";
    public static final String USER_BN = "BusinessNumber";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static Request currentRequest;

    public static String PHONE_TEXT = "userPhone";

    public static final String INTENT_FOOD_ID = "FoodId";

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }


    public static String convertCodeToStatus(String status) {
        switch (status) {
            case "0":
                return "Placed";
            case "1":
                return "Processing";
            default:
                return "Ready, On the way to you";
        }
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED
                            && info[i].isConnected()) {
                        return true;
                    }
        }
        return false;
    }

    public static String getDate(long time) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.setTimeInMillis(time);

        return android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", calendar).toString();
    }

    public static void completeListener( DatabaseReference foods) {

        for (int run = 0; run < request.getFoods().size(); run++) {
            Order orderRequest = request.getFoods().get(run);

            foods.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshotFoods : dataSnapshot.getChildren()) {

                        Food modelFood = snapshotFoods.getValue(Food.class);
                        if (modelFood.getName().equals(orderRequest.getProductName())) {
                            int changeCount = Integer.parseInt(modelFood.getCounter()) + Integer.parseInt(orderRequest.getQuantity());

                            Food tempFood = new Food(modelFood.getName(), modelFood.getImage(), modelFood.getDescription(),
                                    modelFood.getPrice(), modelFood.getDiscount(), modelFood.getMenuId(), String.valueOf(changeCount));


                            foods.child(snapshotFoods.getKey()).setValue(tempFood)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}