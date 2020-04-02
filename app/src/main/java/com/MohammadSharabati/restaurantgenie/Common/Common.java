package com.MohammadSharabati.restaurantgenie.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.MohammadSharabati.restaurantgenie.Model.User;
import com.MohammadSharabati.restaurantgenie.Remote.APIService;
import com.MohammadSharabati.restaurantgenie.Remote.RetrofitClient;


public class Common {
    public static User currentUser;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String USER_BN = "BusinessNumber";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static String PHONE_TEXT = "userPhone";

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService(){
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
}