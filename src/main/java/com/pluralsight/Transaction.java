package com.pluralsight;

public class Transaction {

    // declaring the fields
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    // passing the parameters for the constructor
    public Transaction(String date, String time, String description, String Vendor, double amount) {

        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
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
        return date + " | " + time + " | " + description + " | " + vendor + " | " + amount;
    }

}
