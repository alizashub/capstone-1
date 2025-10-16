package com.pluralsight;

import java.io.*;
import java.util.ArrayList;

public class TransactionRepository {
    private static final String FILE_PATH = "transactions.csv";

    public ArrayList<Transaction> readTransactions() {
        // creating an empty list to store transactions read from the file
        ArrayList<Transaction> listOfTransactions = new ArrayList<Transaction>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH))) {
            String eachLine;
            bufferedReader.readLine(); //Skips header
            while ((eachLine = bufferedReader.readLine()) != null) {
                if (eachLine.trim().isEmpty()) continue; // skip blank lines

                String[] parts = eachLine.split("\\|");
                if (parts.length < 5) {
                    System.out.println("Skipping invalid line : " + eachLine);
                    continue;
                }

                String date = parts[0];
                String time = parts[1];
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                Transaction t = new Transaction(date, time, description, vendor, amount);
                listOfTransactions.add(t);
            }
        } catch (Exception e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }

        return listOfTransactions;
    }

    public void saveTransaction(Transaction transaction) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH, true))){
                bufferedWriter.write(transaction.toCsvFormat());
                bufferedWriter.newLine(); // ensure seperation
            } catch(IOException e){
                System.out.println("Error saving transaction: " + e.getMessage());
            }
        }
    }
