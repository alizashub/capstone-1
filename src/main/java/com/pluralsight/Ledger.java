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

    public Ledger() {
        repository = new TransactionRepository();
        myScanner = new Scanner(System.in);
        transactions = repository.readTransactions();
    }


    public void home() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== HOME SCREEN ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            System.out.print("Choose an option: ");
            String choice = myScanner.nextLine().trim().toUpperCase();

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
                    System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Exiting application. Goodbye!");
    }

    //Add Deposit
    private void addDeposit() {
        System.out.println("\n=== MAKE DEPOSIT ===");
        System.out.print("Enter description: ");
        String depositDescription = (myScanner.nextLine().trim());

        System.out.print("Enter vendor: ");
        String depositVendor = (myScanner.nextLine().trim());

        System.out.print("Enter amount: ");
        double depositAmount = Double.parseDouble(myScanner.nextLine().trim());

        if (depositAmount <= 0) {
            System.out.println("Deposit amount must be positive!");
            return;
        }

        String depositDate = LocalDate.now().toString();
        String depositTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transaction depositTransaction = new Transaction(depositDate, depositTime, depositDescription, depositVendor, depositAmount);

        repository.saveTransaction(depositTransaction);
        transactions.add(depositTransaction);
        System.out.println("Deposit added successfully!");
    }

    // MakePayment
    private void makePayment() {
        System.out.print("Enter description: ");
        String paymentDescription = myScanner.nextLine();

        System.out.print("Enter vendor: ");
        String paymentVendor = myScanner.nextLine();

        System.out.print("Enter amount: ");
        double paymentAmount = -Math.abs(Double.parseDouble(myScanner.nextLine())); // ensure negative

        String paymentDate = LocalDate.now().toString();
        String paymentTime = LocalDate.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transaction paymentTransaction = new Transaction(paymentDate, paymentTime, paymentDescription, paymentVendor, paymentAmount);

        repository.saveTransaction(paymentTransaction);
        transactions.add(paymentTransaction);
        System.out.println("Payment recorded successfully!");
    }

    public void showLedger() {
        boolean inLedger = true;

        while (inLedger) {
            System.out.println("\n=== LEDGER MENU ===");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits Only");
            System.out.println("P) Payments Only");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");

            String choice = myScanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    showAll();
                    break;
                case "D":
                    showDeposits();
                    break;
                case "P":
                    showPayments();
                    break;
                case "R":
                    showReports();
                    break;
                case "H":
                    inLedger = false;
                    home();
                    break;
                default:
                    System.out.println("Invalid option. Try again.");

            }
        }
    }

    @Override
    public String toString() {
        return String.format()
    }

    private void showAll() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }
        System.out.println("=== ALL TRANSACTIONS ===");
        System.out.println("Date       Time     Description       Vendor       Amount");


        }
    }

    private void showDeposits() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() > 0) { // Only positive amounts
                System.out.println(t);
            }
        }
    }

    private void showPayments() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }
        System.out.println("\n=== PAYMENTS ONLY ===");
        System.out.println("Date       Time       Description       Vendor       Amount");
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() < 0) { // Payments are negative
                System.out.println(t);
            }
        }
    }

    private void showReports() {
        boolean inReports = true;

        while (inReports) {
            System.out.println("\n=== REPORTS MENU ===");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back to Ledger");
            System.out.print("Choose an option: ");

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
                case "0":
                    inReports = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showMonthToDate() {
        System.out.println();

    }

    private void showPreviousMonth() {
        System.out.println("\n=== PREVIOUS MONTH TRANSACTIONS ===");
    }

    private void showYearToDate() {
    }

    private void showPreviousYear() {
    }

    private void searchByVendor() {
    }


    }



