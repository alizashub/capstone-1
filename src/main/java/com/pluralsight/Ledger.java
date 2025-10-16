package com.pluralsight;

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
            System.out.println("A) Add A Deposit ");
            System.out.println("B) Make A Payment ");
            System.out.println("C) Go To The Ledger ");
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
                case "A":
                    addDeposit();
                    break;
                case "B":
                    makePayment();
                    break;
                case "C":
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

    private void showMonthToDate() {
        System.out.println("\n");
        System.out.println("   MONTH TO DATE REPORT  ");

        LocalDate today = LocalDate.now();
        LocalDate firstDayofMonth = today.withDayOfMonth(1);

        boolean matchFound = false;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            LocalDate transactionDate = LocalDate.parse(t.getDate());
            if ((transactionDate.isEqual(firstDayofMonth) || transactionDate.isAfter(firstDayofMonth)) && (transactionDate.isEqual(today) || transactionDate.isBefore(today)))
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
            matchFound = true;
        }
        if (!matchFound) {
            System.out.println("No transactions found for this month to date.");
        }
    }

    private void showPreviousMonth() {
        System.out.println("\n=== PREVIOUS MONTH TRANSACTIONS ===");
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfPreviousMonth = firstDayOfCurrentMonth.minusDays(1);
        LocalDate firstDayOfPreviousMonth = lastDayOfPreviousMonth.withDayOfMonth(1);

        boolean matchFound = false;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            LocalDate transactionDate = LocalDate.parse(t.getDate());

            boolean onOrAfterStartMonth = transactionDate.isEqual(firstDayOfPreviousMonth) || transactionDate.isAfter(firstDayOfPreviousMonth);
            boolean onOrBeforeEndMonth = transactionDate.isEqual(lastDayOfPreviousMonth) || transactionDate.isBefore(lastDayOfPreviousMonth);

            if (onOrAfterStartMonth && onOrBeforeEndMonth) {
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
            }
        }

    }

    private void showYearToDate() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfYear = today.withDayOfYear(1);

        boolean matchFound = false;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            LocalDate transactionDate = LocalDate.parse(t.getDate());

            boolean onOrAfterStartYear = transactionDate.isEqual(firstDayOfYear) || transactionDate.isAfter(firstDayOfYear);
            boolean onOrBeforeTodayYear = transactionDate.isEqual(today) || transactionDate.isBefore(today);

            if (onOrAfterStartYear & onOrBeforeTodayYear) {
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());

                matchFound = true;
            }
        }

        if (!matchFound) {
            System.out.println("No transactions found for Year to Date.");
        }
    }

    private void showPreviousYear() {
        System.out.println("\n=== PREVIOUS YEAR TRANSACTIONS ===");

        LocalDate today = LocalDate.now();

        LocalDate firstDayOfThePreviousYear = today.minusYears(1).withDayOfYear(1);
        LocalDate lastDayPreviousYear = today.withDayOfYear(1).minusDays(1);

        boolean matchFound = false;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            LocalDate transactionDate = LocalDate.parse(t.getDate());

            boolean onOrAfterStartPreviousYear = transactionDate.isEqual(firstDayOfThePreviousYear) || transactionDate.isAfter(firstDayOfThePreviousYear);
            boolean onOrBeforeEndPreviousYear = transactionDate.isEqual(lastDayPreviousYear) || transactionDate.isBefore(lastDayPreviousYear);

            if (onOrAfterStartPreviousYear & onOrBeforeEndPreviousYear) {
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());

                matchFound = true;
            }
        }

        if (!matchFound) {
            System.out.println("No transactions found for previous year.");
        }
    }

    private void searchByVendor() {
        System.out.println("Enter vendor name to search: ");
        String searchVendor = myScanner.nextLine().trim();

        boolean matchFound = false;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getVendor().equalsIgnoreCase(searchVendor)) {
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());

                matchFound = true;


            }

        }
    }
}

