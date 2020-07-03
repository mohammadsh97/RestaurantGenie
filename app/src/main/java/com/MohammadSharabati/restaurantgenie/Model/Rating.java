package com.MohammadSharabati.restaurantgenie.Model;
/**
 * Created by Mohammad Sharabati.
 */
public class Rating {
    private String userName;
    private String foodId;
    private String rateValue;
    private String comment;

    public Rating(String userName, String foodId, String rateValue, String comment) {
        this.userName = userName;
        this.foodId = foodId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public Rating() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userPhone) {
        this.userName = userPhone;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
