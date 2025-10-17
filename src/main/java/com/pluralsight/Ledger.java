package com.pluralsight;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Ledger {

    private TransactionRepository repository;
    private Scanner myScanner;
    private ArrayList<Transaction> transactions;
    private String userName;

    // Constructor
    public Ledger() {
        repository = new TransactionRepository();
        myScanner = new Scanner(System.in);
        transactions = repository.readTransactions();
    }

    public void home() {

        askUserName();

        boolean running = true;

        while (running) {
            System.out.println("                 -----------                          ");
            System.out.println("                  HOME MENU                           ");
            System.out.println("                 -----------                          ");
            System.out.println("What Shall We Do Today? ");
            System.out.println("D) Add A Deposit ");
            System.out.println("P) Make A Payment ");
            System.out.println("L) Go To The Ledger ");
            System.out.println("X) Exit The Program ");
            System.out.print("\n" + userName + "," + " Type The LETTER Of Your Choice From Above To Get Started : ");

            String choice = "";
            choice = myScanner.nextLine().trim().toUpperCase();

            try {
                choice = myScanner.nextLine().trim().toUpperCase();
                if (choice.isEmpty()) {
                    System.out.println("Please Choose A Valid Option.");
                }
            } catch (Exception e) {
                System.out.println("Input Error" + e.getMessage());
                continue;
            }


            switch (choice) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    showLedger();
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Option. Please Try Again.");
            }
        }
        System.out.println("Exiting Application. Goodbye!");
    }

    private void askUserName() {
        System.out.print("Hey There! What's Your Name?");
        // take user input and save it to the userName variable
        userName = myScanner.next().trim();

        while (userName.isEmpty()) {
            System.out.println("Oops, You Forgot To Type In Your Name! Let's Try That Again.");
            userName = myScanner.nextLine().trim();
        }
        System.out.println("Welcome , " + userName + "! Let's Get Your Finances Organized!");
    }

    //Add Deposit
    private void addDeposit() {
        System.out.println("\n");
        System.out.println("         " + userName.toUpperCase() + "'S " + " DEPOSIT INFORMATION       ");

        System.out.print("\nEnter Deposit's Description : ");
        String depositDescription = (myScanner.nextLine().trim());

        if (depositDescription.isEmpty()) {
            System.out.println(userName + "We Need A Description To Add This Deposit. Let's Try That Again.");
            return;
        }

        System.out.print("Enter Deposit's Vendor Name: ");
        String depositVendor = (myScanner.nextLine().trim());
        if (depositVendor.isEmpty()) {
            System.out.println("Don't Leave Me Hanging! Please Enter A Vendor Name. ");
            return;
        }

        double depositAmount = 0;
        boolean validAmount = false;

        while (!validAmount) {
            System.out.print("Enter Deposit's Amount: ");
            String amountInput = myScanner.nextLine().trim();
            try {
                depositAmount = Double.parseDouble(amountInput);
                if (depositAmount <= 0) {
                    System.out.println("Deposits must be positive. Give it another shot!");
                } else {
                    validAmount = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("That Doesn't Look Like A Valid Number. Let's Try Again. ");
            }
        }

        String depositDate = LocalDate.now().toString();
        String depositTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transaction depositTransaction = new Transaction(depositDate, depositTime, depositDescription, depositVendor, depositAmount);
        System.out.println(userName + " You Are Despositing $ " + depositAmount + " to " + depositVendor + ". \n");
        repository.saveTransaction(depositTransaction);
        transactions.add(depositTransaction);
        System.out.println("Deposit Transaction Was Added Successfully!");
        System.out.println("\nLet's Go Back Home! What Else Would You Like To Do Today?");
    }

    // MakePayment
    private void makePayment() {
        System.out.println("\n");
        System.out.println("         " + userName.toUpperCase() + "'S " + " PAYMENT INFORMATION       ");

        System.out.print("\nEnter Payment's Description: ");
        String paymentDescription = myScanner.nextLine();

        System.out.print("Enter Payment's Vendor: ");
        String paymentVendor = myScanner.nextLine();

        double paymentAmount = 0;
        boolean validPaymentInput = false;

        while (!validPaymentInput) {
            System.out.print("Enter Payment's Amount: ");
            String paymentInput = myScanner.nextLine().trim();
            try {
                paymentAmount = Double.parseDouble(paymentInput);

                if (paymentAmount <= 0) {
                    System.out.println("Payment must be positive. Give it another shot.");
                } else {
                    paymentAmount = -Math.abs(paymentAmount);
                    validPaymentInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("That Does Not Look like A Valid Number. Let's Try Again.");
            }
        }

        String paymentDate = LocalDate.now().toString();
        String paymentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transaction paymentTransaction = new Transaction(paymentDate, paymentTime, paymentDescription, paymentVendor, paymentAmount);
        System.out.println("You Are Paying $" + paymentAmount + " to " + paymentVendor + ". \n");
        repository.saveTransaction(paymentTransaction);
        transactions.add(paymentTransaction);
        System.out.println("Payment Transaction Was Recorded Successfully!");
        System.out.println("\nLet's Go Back Home! What Else Would You Like To Do Today?");

    }

    public void showLedger() {
        boolean inLedger = true;

        while (inLedger) {
            System.out.println("                  -----------                          ");
            System.out.println("                  LEDGER MENU                         ");
            System.out.println("                  -----------                          ");
            System.out.println("A) View All Transactions");
            System.out.println("B) View Deposits Only");
            System.out.println("C) View Payments Only");
            System.out.println("D) View My Reports");
            System.out.println("X) Home Menu");
            System.out.print("\n" + userName + "," + " Type The LETTER Of Your Choice From Above To View Your Transaction History : ");

            String choice = myScanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    showAll();
                    break;
                case "B":
                    showDeposits();
                    break;
                case "C":
                    showPayments();
                    break;
                case "D":
                    showReports();
                    break;
                case "X":
                    inLedger = false;
                    break;
                default:
                    System.out.println("Invalid Option. Please Try Again.");

            }
        }
    }

    private void showAll() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }
        System.out.println("\n");
        System.out.println("   ALL TRANSACTIONS   ");

        for (int i = transactions.size() - 1; i >= 0; i--) {  // newest first
            Transaction t = transactions.get(i);
            System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
        }
    }

    private void showDeposits() {
        System.out.println("\n");
        System.out.println("   ALL DEPOSITS   ");

        if (transactions.isEmpty()) {
            System.out.println("No Deposit Transactions To Display.");
            return;
        }
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() > 0) {
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
            }
        }
    }

    private void showPayments() {
        System.out.println("\n");
        System.out.println("   ALL PAYMENTS   ");

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() < 0) { // Payments are negative
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
            }
        }
    }

    private void showReports() {
        boolean inReports = true;

        while (inReports) {
            System.out.println("                 -----------                          ");
            System.out.println("                  REPORT MENU                         ");
            System.out.println("                 -----------                          ");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search By Vendor");
            System.out.println("6) Back To Ledger");
            System.out.println("7) Back Home");
            System.out.print("\n" + userName + "," + " Type The LETTER Of Your Choice From Above To See Your Report : ");

            String choice = myScanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showMonthToDate();
                    break;
                case "2":
                    showPreviousMonth();
                    break;
                case "3":
                    showYearToDate();
                    break;
                case "4":
                    showPreviousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    inReports = false;
                    return;
                case "7":
                    home();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    // shows transaction from start of the current month up to today
    private void showMonthToDate() {
        System.out.println("\n");
        System.out.println("   MONTH TO DATE REPORT  ");

        LocalDate today = LocalDate.now();
        LocalDate firstDayofMonth = today.withDayOfMonth(1); // create a new object with the same year and month, but day set to 1
        System.out.println("Transactions From " + firstDayofMonth + " " + " to " +today);
        boolean matchFound = false; // the varible matchFound starts with no matches

        // looping backwards
        for (int i = transactions.size() - 1; i >= 0; i--) {
            // gets one transaction object from the array list at index i
            Transaction t = transactions.get(i);
            // turns the string into a localdate object called trasactiondate
            LocalDate transactionDate = LocalDate.parse(t.getDate());
            //checks if the date is on or after the first day of the month && if the date is on or before today ( to find if the transaction happened this month )
            if (!transactionDate.isBefore(firstDayofMonth) && !transactionDate.isAfter(today)) {
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                matchFound = true;
            }
        } // loop finished
        // check if after the loop finsihes no matches are found
        if (!matchFound) {
            System.out.println("No transactions Found For This month To Date.");
        }
    }

    // show transactions from the previous month
    private void showPreviousMonth() {

        System.out.println("\n");
        System.out.println("   PREVIOUS MONTH TRANSACTIONS  ");


        // current date eg. 10/16/2025
        LocalDate today = LocalDate.now();
        // 1st day of the current month eg. 1/6/2015
        LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);
        // subtract 1 day from the 1st of the current month to find the last day of the previous month. eg. 10/30/2025
        LocalDate lastDayOfPreviousMonth = firstDayOfCurrentMonth.minusDays(1);
        // sets day to 1 for the previous month eg. 10/1/2025
        LocalDate firstDayOfPreviousMonth = lastDayOfPreviousMonth.withDayOfMonth(1);

        System.out.println("\nTransactions For " + lastDayOfPreviousMonth.getMonth() + " :");

        boolean matchFound = false;
        // loops backwards through the transactions
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            // converts string to localdate object
            LocalDate transactionDate = LocalDate.parse(t.getDate());

            if (!transactionDate.isBefore(firstDayOfPreviousMonth) && !transactionDate.isAfter(lastDayOfPreviousMonth)) {
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                matchFound = true;

            }
        }
        if (!matchFound) {
            System.out.println("No Transactions Found For " + lastDayOfPreviousMonth.getMonth());
        }

    }

    // show transaction from Jan 1st of the current year to the current date of the current year
    private void showYearToDate() {
        System.out.println("\n");
        System.out.println("   YEAR TO DATE TRANSACTIONS   ");

        // todays date eg. 10/16/2025
        LocalDate today = LocalDate.now();
        //sets firstdayoftheyear to jan 1st of the current year eg. 1/1/2025
        LocalDate firstDayOfYear = today.withDayOfYear(1);
        // creating a header
        System.out.println("\nTransactions From " + firstDayOfYear.getMonth() + " " + firstDayOfYear.getYear() + " to " + today.getMonth() + " " + today.getYear());

        boolean matchFound = false;
        // looping backwards through the transactions
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            // converts date string to localdate object
            LocalDate transactionDate = LocalDate.parse(t.getDate());
            // checks to see if the date is jan 1st or after and if the date if today or earlier ( eg. range created with Minimum : Jan 1st & Maximum : 16th October )
            if ((!transactionDate.isBefore(firstDayOfYear)) && !transactionDate.isAfter(today)) {
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                matchFound = true;
            }
        }
        if (!matchFound) {
            System.out.println("No transactions Found For" + firstDayOfYear.getMonth() + " " + firstDayOfYear.getYear() + " to " + today.getMonth() + " " + today.getYear());
        }
        }

    // show transactions from the previous year
    private void showPreviousYear () {
        System.out.println("\n");
        System.out.println("   PREVIOUS YEAR TRANSACTIONS   ");

            //  todays date eg. 10/16/2025
            LocalDate today = LocalDate.now();
            // changes the year  eg. 10/16/2024 and then changes the day of that year to the 1st day eg. 1/1/2024
            LocalDate firstDayOfThePreviousYear = today.minusYears(1).withDayOfYear(1);
            // changes current date to day 1, month 1 eg. 1/1/2025 and then subtracts one day eg. 12/31/2024
            LocalDate lastDayPreviousYear = today.withDayOfYear(1).minusDays(1);
            System.out.println("Transactions From " + firstDayOfThePreviousYear.getYear());

            boolean matchFound = false;
            // looping backwards
            for (int i = transactions.size() - 1; i >= 0; i--) {
                Transaction t = transactions.get(i);
                // convert string to localdate object
                LocalDate transactionDate = LocalDate.parse(t.getDate());
                // checks if the date is within the previous year, not before and not after the last day of the previous year. eg. between 1st Jan 2024 to 31st Dec 2024.
                if (!transactionDate.isBefore(firstDayOfThePreviousYear) && !transactionDate.isAfter(lastDayPreviousYear)) {
                    System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                    matchFound = true; }

                }
            if (!matchFound){
                System.out.println("No transactions Found For " + firstDayOfThePreviousYear.getYear());
            }

    }

    // show transaction from specifc vendors
        private void searchByVendor () {
            System.out.println("\n");
            System.out.println("   VENDOR TRANSACTIONS   ");
            System.out.println("Enter Vendor Name To Search: ");
            String searchVendor = myScanner.nextLine().trim();
            System.out.println("Transactions From "+ searchVendor );

            boolean matchFound = false;
            // looping backwards
            for (int i = transactions.size() - 1; i >= 0; i--) {
                Transaction t = transactions.get(i);

                if (t.getVendor().equalsIgnoreCase(searchVendor)) {
                    System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                    matchFound = true; }
                }

            if (!matchFound) {
                System.out.println("No transactions Found For " + searchVendor);

            }
        }
    }

