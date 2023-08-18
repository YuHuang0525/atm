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
                        Account account = login(sc, accounts);

                        // after 3 times of failure, go back to main page
                        if (account == null) {
                            continue;
                        }

                        System.out.println("Successfully login!");

                        showUserCommand(sc, account, accounts);

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
     * show user commands
     * @param sc
     * @param account
     * @param accounts arraylist that store all the accounts
     */
    private static void showUserCommand(Scanner sc, Account account, ArrayList<Account> accounts) {
        System.out.println("Welcome VIP " + account.getUserName() + ", your card number is: " + account.getCardNumber() );

        while (true) {
            System.out.println("============ You may choose one of the following operations ============");
            System.out.println("1. Search");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Change password");
            System.out.println("6. Exit");
            System.out.println("7. Delete account");

            int operation = sc.nextInt();
            switch (operation) {
                // search info of the current account
                case 1:
                    showUserInfo(account);
                    break;

                // deposit money into the current account
                case 2:
                    depositMoney(sc, account);
                    break;

                // withdraw money
                case 3:
                    withdrawMoney(sc, account);
                    break;

                // transfer money
                case 4:
                    transferMoney(sc, account, accounts);
                    break;

                // change password
                case 5:
                    changePassword(sc, account);
                    break;

                // exit the current account
                case 6:
                    System.out.println("Successfully log out, see you next time!");
                    return;

                // delete account from the system
                case 7:
                    if (deleteAccount(sc, account, accounts)) {
                        return;
                    }
                    break;

                default:
                    System.out.println("Operation is not valid");
                    break;
            }
        }
    }

    /**
     * delete the account from the system forever
     * @param sc
     * @param account
     * @param accounts
     * @return true if successfully deleted else false
     */
    private static boolean deleteAccount(Scanner sc, Account account, ArrayList<Account> accounts) {
        // confirmation message
        System.out.println("Are you sure you want to delete the account? [Y/N]");
        String confirm = sc.next();
        if (confirm.equals("Y")) {

            // delete the account
            accounts.remove(account);

            System.out.println("Your account has been successfully deleted");

            return true;

        } return false;
    }

    /**
     * change the password of the input account
     * @param sc
     * @param account
     */
    private static void changePassword(Scanner sc, Account account) {
        String oldPassword = account.getPassword();

        while (true) {
            System.out.println("Please enter the new password: ");
            String newPassword = sc.next();

            if (newPassword.equals(oldPassword)){
                System.out.println("New password cannot be same as old password");
            } else {
                account.setPassword(newPassword);
                System.out.println("Password changes successfully");
                return;
            }
        }
    }

    private static void transferMoney(Scanner sc, Account account, ArrayList<Account> accounts) {
        // first check if there are 2 or more accounts in the system
        if (accounts.size() == 1) {
            System.out.println("There's no account in the system besides your account");
            return;
        }

        while (true) {
            System.out.println("Please enter the account's card number that you want to transfer to: ");
            String cardNumber = sc.next();
            // check if cardNumber exists in the system and make sure is not the user's own account
            if (cardNumber.equals(account.getCardNumber())){
                System.out.println("You cannot transfer to the same card");
            } else {
                if (checkCardNumberExist(accounts, cardNumber)) {
                    // get the account object to which the user is transfering to
                    Account transferAccount = getAccountByCard(accounts, cardNumber);

                    // confirmation for transfer
                    confirmTransfer(sc, transferAccount);

                    // once information is confirmed, starts transfering process
                    while (true) {
                        System.out.println("Please enter the amount you want to transfer: ");
                        double amount = sc.nextDouble();
                        if (amount > account.getBalance()) {
                            System.out.println("Sorry your balance is not enough, please try again");
                        } else {
                            // subtract money from the account balance
                            account.subtractBalance(amount);

                            // add money to the transfer account balance
                            transferAccount.addBalance(amount);

                            System.out.println("Successfully transfer!");
                            showUserInfo(account);
                            return;
                        }
                    }

                } else {
                    System.out.println("Card number that you just entered does not exist in the system");
                }
            }
        }
    }

    /**
     * ask the user to type in the first char of the transfer account's username, for confirmation
     * @param sc
     * @param transferAccount
     */
    private static void confirmTransfer(Scanner sc, Account transferAccount) {

        String transferName = transferAccount.getUserName();
        // mask the username to *XY format for later confirmation use
        String maskedTransferName = "*" + transferName.substring(1);

        while (true) {

            // ask for user confirmation
            System.out.println("You are trying to transfer to " + maskedTransferName);
            System.out.println("Please enter the first char of the user's name for confirmation: ");
            String confirmChar = sc.next();

            if (confirmChar.equals("" + transferName.charAt(0))) {
                return;
            } else {
                System.out.println("Please try again");
            }
        }

    }


    /**
     * withdraw money from the account
     * @param sc
     * @param account
     */
    private static void withdrawMoney(Scanner sc, Account account) {
        System.out.println("============ Withdraw Page ============");
        // determine if the account balance is over 100
        if (account.getBalance() < 100) {
            System.out.println("Your balance is too low, please make the deposit first");
            return;
        }

        while (true) {
            System.out.println("Please enter the amount that you want to withdraw: ");
            double amount = sc.nextDouble();
            // check if amount is less than the limit and the balance
            if (amount <= account.getLimit()) {
                if (amount <= account.getBalance()) {
                    // withdraw successful
                    account.subtractBalance(amount);
                    System.out.println("Withdraw successfully!");
                    return;
                } else {
                    // amount is below limit but more than current balance
                    System.out.println("Your balance is not enough, current balance is " + account.getBalance() + ", please try again");
                }

            } else {
                // amount goes over the limit
                System.out.println("Amount goes over limit, please try again");
            }
        }

    }

    /**
     * deposit money to the given account
     * @param sc
     * @param account
     */
    private static void depositMoney(Scanner sc, Account account) {
        System.out.println("============ Deposit Page ============");
        System.out.println("Please enter the amount that you want to deposit: ");
        double amount = sc.nextDouble();

        // add the amount to the account balance
        account.addBalance(amount);

        System.out.println("Successfully deposited!");
        showUserInfo(account);
    }

    /**
     * show the user account's information
     * @param account
     */
    private static void showUserInfo(Account account) {

        System.out.println("============ Below are your current account's information ============");
        System.out.println("Card Number: " + account.getCardNumber());
        System.out.println("Name: " + account.getUserName());
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Current withdraw limit: " + account.getLimit());

    }

    /**
     * login process
     * @param sc
     * @param accounts
     * @return account if successfully login in else null
     */
    private static Account login(Scanner sc, ArrayList<Account> accounts) {
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

        if (success) {
            return account;
        }

        return null;

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
