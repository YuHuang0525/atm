package com.atm.mock;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LoginPage {
    public static void main(String[] args) {

        // arraylist to store all accounts in the system
        ArrayList<Account> accounts = new ArrayList<>();

        // ask the user to choose operation
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("============ Welcome to the ATM system ============");
            System.out.println("1. Login");
            System.out.println("2. Register");

            int operation = sc.nextInt();

            switch (operation) {

                // 1. Login
                case 1:

                    // first check if there's any account in the system
                    if (accounts.size() == 0) {
                        System.out.println("There's no account in the system, please register first");
                        continue;
                    } else {
                        // login process starts
                        boolean success = login(sc, accounts);

                        // after 3 times of failure, go back to main page
                        if (!success) {
                            continue;
                        } else {
                            System.out.println("Successfully login!");
                        }

                    }
                    break;

                // 2. Register
                case 2:
                    register(sc, accounts);
                    break;

                default:
                    System.out.println("Please choose between 1 and 2");
                    break;
            }
        }

    }

    /**
     * login process
     * @param sc
     * @param accounts
     * @return true if successfully login in else false
     */
    private static boolean login(Scanner sc, ArrayList<Account> accounts) {
        // login process
        System.out.println("Please enter the card number: ");
        String cardNumber = sc.next();

        // check if card number exists
        Account account = getAccountByCard(accounts, cardNumber);

        while (account == null) {
            System.out.println("Card Number entered is not found, please try again");

            cardNumber = sc.next();
            account = getAccountByCard(accounts, cardNumber);
        }

        boolean success = false;

        // ask for the password, 3 chances in total
        for (int i = 0; i < 3; i++) {
            System.out.println("Please enter the password: ");
            String password = sc.next();

            if (!password.equals(account.getPassword())) {
                System.out.println("Password incorrect, you have " + (2-i) + " chance(s) left");
            } else {
                success = true;
                break;
            }
        }

        return success;

    }

    /**
     * register the new account
     * @param sc
     * @param accounts
     */
    private static void register(Scanner sc, ArrayList<Account> accounts) {

        System.out.println("Please enter your name: ");
        String name = sc.next();
        System.out.println("Please enter your password: ");
        String firstPassword = sc.next();
        System.out.println("Please confirm your password: ");
        String secondPassword = sc.next();

        // make sure the confirm password is same as first time entered
        while (!secondPassword.equals(firstPassword)) {
            System.out.println("Password entered is different, please try again");
            secondPassword = sc.next();
        }

        System.out.println("Please enter the limit of withdraw money");
        double limit = sc.nextDouble();

        // randomly generate the 16-digits card number for the user

        Random rc = new Random();
        String newCardNumber = "";
        for (int i = 0; i < 10; i++) {
            int digit = rc.nextInt(10);
            newCardNumber += digit;
        }

        // we need to ensure the new cardnumber does not exist, otherwise repeat
        while (checkCardNumberExist(accounts, newCardNumber)){
            // randomly generate the 16-digits card number for the user
            newCardNumber = "";
            for (int i = 0; i < 10; i++) {
                int digit = rc.nextInt(10);
                newCardNumber += digit;
            }
        }

        // successfully registered the account, we need to create account object for the user
        Account newAccount = new Account(newCardNumber, secondPassword, name, 0, limit);

        // add Account object to the accounts arraylist
        accounts.add(newAccount);

        System.out.println("Successfully created account, your card number is: " + newCardNumber);
    }

    /**
     * loop through the array and check if cardnumber exists
     * @param accounts
     * @param newCardNumber
     * @return true if exists false otherwise
     */
    public static boolean checkCardNumberExist(ArrayList<Account> accounts, String newCardNumber) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getCardNumber().equals(newCardNumber)) {
                return true;
            }
        } return false;

    }

    /**
     * loop through the accounts variable and search the account by card number
     * @param accounts
     * @param cardNumber
     * @return Account object
     */
    public static Account getAccountByCard(ArrayList<Account> accounts, String cardNumber) {
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.getCardNumber().equals(cardNumber)) {
                return account;
            }
        } return null;
    }
}
