package com.pluralsight;


public class Transaction {
    // fields for transaction object
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;


    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date.trim();
        this.time = time.trim();
        this.description = description.trim();
        this.vendor = vendor.trim();
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    public String toCsvFormat() {
        return date + "|" + time + "|" + description + "|" + vendor + "|" + amount;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | $%.2f",
                date, time, description, vendor, amount);
    }
}
