package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    // fields for the transaction object
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    // Formatters to keep date/time consistent
    private static final DateTimeFormatter FORMATTED_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATTED_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Constructor
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        // assigning parameter to object's field eg. field ( this.date) = parameter ( date )
        this.date = date;
        this.time = time;
        // using null safety to prevent crash
        // using the conditional operator ( the if-else shorthand )
        this.description = description == null ? "" : description;
        this.vendor = vendor == null ? "" : vendor;
        this.amount = amount;
    }

// Getters to be able to access the private data inside the transaction object

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
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

    // Simple escaping so if someone has '|' in a description or vendor name it won't break the CSV split
    private static String escapePipes(String s) {
        return s.replace("|", "\\|");
    }

    // creating a method to convert the five fields into one line
    public String toCsvLine() {
        return FORMATTED_DATE.format(date) + "|" + FORMATTED_TIME.format(time) + "|" + escapePipes(description) + "|" + escapePipes(vendor) + "|" + amount;

    }

}
