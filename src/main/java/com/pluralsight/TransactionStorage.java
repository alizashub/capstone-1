package com.pluralsight;

import java.io.*;

public class TransactionStorage {

    // the path is now fixed
    private static final String filePath = "src/main/resources/transactions.csv";

    public void readTransactions() {

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            bufferedReader.readLine();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line); // read and print each line in a loop
            }

            bufferedReader.close();

        } catch (IOException e) {
            System.out.println(" Error reading file: " + e.getMessage());
        }

    }
    public void writeTransaction(String transactionLine) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(transactionLine);
            bufferedWriter.newLine();

            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
}
