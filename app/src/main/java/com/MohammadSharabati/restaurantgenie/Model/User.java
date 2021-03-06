package com.MohammadSharabati.restaurantgenie.Model;
/**
 * Created by Mohammad Sharabati.
 */
public class User {

    private String businessNumber;
    private String email;
    private String Phone ;
    private String Name ;
    private String Password ;
    private Object balance;


    public User() {
    }

    public User(String businessNumber, String email, String phone, String name, String password) {
        this.businessNumber = businessNumber;
        this.email = email;
        this.Phone = phone;
        this.Name = name;
        this.Password = password;
    }

    public Object getBalance() {
        return balance;
    }
    public void setBalance(Object balance) {
        this.balance = balance;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
