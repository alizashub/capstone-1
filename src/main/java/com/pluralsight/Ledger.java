package com.pluralsight;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Ledger {

    private TransactionRepository repository;
    private Scanner myScanner;

    public Ledger() {
        repository = new TransactionRepository();
        myScanner = new Scanner(System.in);
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
                case "D" -> addDeposit();
                case "P" -> makePayment();
                case "L" -> showLedger();
                case "X" -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }

        System.out.println("Exiting application. Goodbye!");
    }

    private void addDeposit() {
        System.out.print("Enter description: ");
        String description = myScanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = myScanner.nextLine();
        double amount;
        try {
            System.out.print("Enter amount: ");
            amount = Double.parseDouble(myScanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Transaction cancelled.");
            return;
        }

        String date = java.time.LocalDate.now().toString();
        String time = Transaction.getCurrentTimeString();

        Transaction t = new Transaction(date, time, description, vendor, amount);
        repository.saveTransaction(t);
        System.out.println("Deposit added successfully!");
    }

    private void makePayment() {
        System.out.print("Enter description: ");
        String description = myScanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = myScanner.nextLine();
        double amount;
        try {
            System.out.print("Enter amount: ");
            amount = -Math.abs(Double.parseDouble(myScanner.nextLine())); // negative for payment
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Transaction cancelled.");
            return;
        }

        String date = java.time.LocalDate.now().toString();
        String time = Transaction.getCurrentTimeString();

        Transaction t = new Transaction(date, time, description, vendor, amount);
        repository.saveTransaction(t);
        System.out.println("Payment recorded successfully!");
    }

    private void showLedger() {
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
            List<Transaction> allTransactions = repository.readTransactions();

            switch (choice) {
                case "A" -> allTransactions.forEach(System.out::println);
                case "D" -> allTransactions.stream().filter(t -> t.getAmount() > 0).forEach(System.out::println);
                case "P" -> allTransactions.stream().filter(t -> t.getAmount() < 0).forEach(System.out::println);
                case "R" -> System.out.println("Reports not implemented yet."); // placeholder
                case "H" -> inLedger = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
