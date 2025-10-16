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

    // Constructor
    public Ledger() {
        repository = new TransactionRepository();
        myScanner = new Scanner(System.in);
        transactions = repository.readTransactions();
    }

    public void home() {
        boolean running = true;

        while (running) {
            System.out.println("*************");
            System.out.println(" HOME MENU ");
            System.out.println("*************");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make A Payment");
            System.out.println("L) Go To Ledger");
            System.out.println("X) Exit The Program");

            System.out.print("Choose An Option: ");
            String choice = "";
            try {
                choice = myScanner.nextLine().trim().toUpperCase();
                if (choice.isEmpty()) {
                    System.out.println("Please Choose A Valid Option.");
                    continue;
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

    //Add Deposit
    private void addDeposit() {
        System.out.println("***********************");
        System.out.println("  DEPOSIT INFORMATION  ");
        System.out.println("***********************");

        System.out.print("Enter Deposit Description : ");
        String depositDescription = (myScanner.nextLine().trim());

        if (depositDescription.isEmpty()) {
            System.out.println("Deposit Description Cannot Be Empty!");
            return;
        }

        System.out.print("Enter Deposit Vendor: ");
        String depositVendor = (myScanner.nextLine().trim());
        if (depositVendor.isEmpty()) {
            System.out.println("Vendor Name Cannot Be Empty!");
            return;
        }
        double depositAmount = 0;
        System.out.print("Please Enter Deposit Amount: ");
        try {
            depositAmount = Double.parseDouble(myScanner.nextLine().trim());

            if (depositAmount <= 0) {
                System.out.println("Deposit Amount Must Be Positive!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Deposit Amount Invalid. Please Enter A Number.");
            return;
        }

        String depositDate = LocalDate.now().toString();
        String depositTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transaction depositTransaction = new Transaction(depositDate, depositTime, depositDescription, depositVendor, depositAmount);
        System.out.println("You Are Despositing $" + depositAmount + " to " + depositVendor + ". \n");
        repository.saveTransaction(depositTransaction);
        transactions.add(depositTransaction);
        System.out.println("Deposit Transaction Was Added Successfully!");
    }

    // MakePayment
    private void makePayment() {
        System.out.println("***********************");
        System.out.println("  PAYMENT INFORMATION  ");
        System.out.println("***********************");

        System.out.print("Enter Payment Description: ");
        String paymentDescription = myScanner.nextLine();

        System.out.print("Enter Payment Vendor: ");
        String paymentVendor = myScanner.nextLine();

        System.out.print("Enter Payment Amount: ");
        double paymentAmount = -Math.abs(Double.parseDouble(myScanner.nextLine().trim()));

        String paymentDate = LocalDate.now().toString();
        String paymentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transaction paymentTransaction = new Transaction(paymentDate, paymentTime, paymentDescription, paymentVendor, paymentAmount);
        System.out.println("You Are Paying $" + paymentAmount + " to " + paymentVendor + ". \n");
        repository.saveTransaction(paymentTransaction);
        transactions.add(paymentTransaction);
        System.out.println("Payment Transaction Was Recorded Successfully!");
    }

    public void showLedger() {
        boolean inLedger = true;

        while (inLedger) {
            System.out.println("**********************");
            System.out.println("      LEDGER MENU     ");
            System.out.println("**********************");
            System.out.println("A) View All Transactions");
            System.out.println("D) View Deposits Only");
            System.out.println("P) View Payments Only");
            System.out.println("R) View My Reports");
            System.out.println("H) Home Menu");
            System.out.print("Choose An Option: ");

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
                    break;
                default:
                    System.out.println("Invalid option. Try again.");

            }
        }
    }

    private void showAll() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }
        System.out.println("**********************");
        System.out.println("   ALL TRANSACTIONS   ");
        System.out.println("**********************");
        for (int i = transactions.size() - 1; i >= 0; i--) {  // newest first
            Transaction t = transactions.get(i);
            System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());

        }
    }

    private void showDeposits() {
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
        System.out.println("""
                        *************************
                               PAYMENTS ONLY     
                        *************************
                        Date  Time  Description   Vendor   Amount
                        """);
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() < 0) { // Payments are negative
                System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount()); }
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
            System.out.println("6) Back to Ledger");
            System.out.println("7) Back Home");
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
        System.out.println("\n=== MONTH TO DATE REPORT ===");
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

        private void showPreviousMonth () {
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

        private void showYearToDate () {
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

        private void showPreviousYear () {
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

        private void searchByVendor () {
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

