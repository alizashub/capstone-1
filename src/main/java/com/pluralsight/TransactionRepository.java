package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransactionRepository {

    public ArrayList<Transaction> readTransactions() {
        ArrayList<Transaction> listOfTransactions = new ArrayList<Transaction>();
        try {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String eachLine;
            bufferedReader.readLine();
            while ((eachLine = bufferedReader.readLine()) != null) {
                String[] parts = eachLine.split("\\|");

                String date = parts[0];
                String time = parts[1];
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                Transaction t = new Transaction(date, time, description, vendor, amount);
                listOfTransactions.add(t);
            }

            bufferedReader.close();
            fileReader.close();
        } catch (Exception e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }

        return listOfTransactions;
    }

    public void saveTransaction(Transaction transaction) {
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(transaction.toCsvFormat());
            bufferedWriter.newLine();

            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
}
