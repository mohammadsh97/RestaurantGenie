package com.MohammadSharabati.restaurantgenie.Common;

import com.MohammadSharabati.restaurantgenie.Model.User;

public class Common {
    public static User currentUser;

    public static final String DELETE = "Delete";


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
}