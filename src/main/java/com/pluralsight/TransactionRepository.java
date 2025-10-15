package com.pluralsight;

import java.io.*;
import java.util.ArrayList;

public class TransactionRepository {

    private static final String FILE_PATH = "src/main/resources/transactions.csv";

    public ArrayList<Transaction> readTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String date = parts[0];
                    String time = parts[1];
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    transactions.add(transaction);
                }
            }

            bufferedReader.close();
            fileReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("No existing transactions found. A new file will be created when you add data.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error processing transactions: " + e.getMessage());
        }

        return transactions;
    }
    // Save a single transaction to the file
    public void saveTransaction(Transaction transaction) {
        try {
            // Create writer objects inside try
            FileWriter fileWriter = new FileWriter(FILE_PATH, true); // append mode
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


            bufferedWriter.write(transaction.toCsvLine());
            bufferedWriter.newLine();

            // Close writers
            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }
}