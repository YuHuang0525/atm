package com.atm.mock;

public class Account {

    private String cardNumber;
    private String password;
    private String userName;
    private double balance;
    private double limit;


    public Account() {
    }

    public Account(String cardNumber, String password, String userName, double balance, double limit) {
        this.cardNumber = cardNumber;
        this.password = password;
        this.userName = userName;
        this.balance = balance;
        this.limit = limit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }
}
