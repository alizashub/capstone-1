package com.pluralsight;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Ledger {
    // repository object to handle reading & writing transactions into the csv file
    private TransactionRepository repository;
    // scanner object to take user input
    private Scanner myScanner;
    private ArrayList<Transaction> transactions;
    // variable to store name of user for personalized messages
    private String userName;

    // constructor
    public Ledger() {
        repository = new TransactionRepository();
        myScanner = new Scanner(System.in);
        transactions = repository.readTransactions();
    }


    // takes user input, shows home menu, asks user for home menu option, keeps user in loop is no name or option is choosen
    public void home() {
        System.out.println(getRandomGreeting());
        // prompts the user for their name
        askUserName();
        // true keeps the loop running until the user presses X to exit
        boolean running = true;
        // displays the menu with options
        while (running) {
            System.out.println("                 -----------                          ");
            System.out.println("                  HOME MENU                           ");
            System.out.println("                 -----------                          ");
            System.out.println("What Would You Like To Do Today? ");
            System.out.println("B) Find Current Balance ");
            System.out.println("D) Add A Deposit ");
            System.out.println("P) Make A Payment ");
            System.out.println("L) Go To The Ledger ");
            System.out.println("X) Exit The Program ");
            // prints user name with text to choose a valid option form the home menu
            System.out.print("\n" + userName + "," + " Type The LETTER Of Your Choice From Above To Get Started : ");

            // decalring a choice string to take input from user
            // trims extra space from start and end, converts to uppercase
            String choice = myScanner.nextLine().trim().toUpperCase();
            // if the user does not input their name, the loop is keep running until they do
            if (choice.isEmpty()) {
                System.out.println("\nPlease Choose A Valid Option.");
                // take the user back to home menu to choose an option
                continue;
            }

            switch (choice) {
                case "B" :
                    System.out.printf("%s, your current balance is: $%.2f%n", userName, showCurrentBalance());
                    break;
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
                default: // if user enters a char that is not D,P,L or X, the defult would be printed
                    System.out.println("Invalid Option. Please Try Again.");
            }
        }
        // once the user presses x they will be taken out of the whileloop
        System.out.println("Exiting Application. Goodbye!");
    }

    // a string of array for getting random greetings
    private String[] greetings = {
            "Hey there, superstar!",
            "Hey, money maestro!",
            "Good to see you, financial wizard!",
            "Howdy, ledger legend!",
            "Greetings, number cruncher!",
            "Ahoy! Ready to track your finances?",
            "Hello there — let’s tame those numbers!",
            "Welcome! Your ledger awaits." };

    // method to randomize the greetings recived
    private String getRandomGreeting(){
        // pick a random index in the array
        // multiplying math.random and lenght creates the range for the random number eg. if greetings.lenght is 6, the range becomes 0-6 and then generate a random decimal number.
        // then we cast it into an int cause index number can only be an int
        int index = (int) (Math.random()*greetings.length);
        // return the greeting at that random index
        return greetings[index];
    }

    // takes user name and saves it into userName variable
    private void askUserName() {
        System.out.print("Let's get to know you. What's Your Name?");
        // take user input and save it to the userName variable and converts to uppercase
        userName = myScanner.nextLine().trim();

        while (userName.isEmpty()) {
            // if left empty prompts the user to add input
            System.out.println("Oops, You Forgot To Type In Your Name! Let's Try That Again.");
            userName = myScanner.nextLine().trim();
        }
        System.out.println("Welcome , " + userName + "! Let's Get Your Finances Organized!");
    }

    // adds all the values in the csv to get the current balance
    private double showCurrentBalance(){
        double balance = 0;
        for (Transaction t : transactions) {
            balance = balance + t.getAmount();
        }
        return balance;
    }

    // guides user to add deposit and create a new transaction object and saves to repository
    private void addDeposit() {
        System.out.println("\n");
        // prints the title with username in uppercase
        System.out.println("         " + userName.toUpperCase() + "'S " + " DEPOSIT INFORMATION       ");

        // initilize depositDescription with empty string
        String depositDescription = "";

        // while loops keeps repeating until depositDescription is empty
        while (depositDescription.isEmpty()) {
            System.out.print("\nEnter Deposit's Description : ");
            // takes user input for deposit description and remove extra spaces from start and end
            depositDescription = (myScanner.nextLine().trim());
            // is checked each time the loop runs
            if (depositDescription.isEmpty()) {
                System.out.println("\n" + userName + "," + " We Need A Description To Add This Deposit. Let's Try That Again.");
            }
        }

        // initilize depositVendor with empty string
        String depositVendor = "";

        // depositVendor is empty so user enters the loop until they enter valid input
        while (depositVendor.isEmpty()) {
            System.out.print("Enter Deposit's Vendor Name: ");
            depositVendor = (myScanner.nextLine().trim());

            //prints only if the the user input is still empty
            if (depositVendor.isEmpty()) {
                System.out.println("Don't Leave Me Hanging! Please Enter A Vendor Name : ");
            }
        }

        // initilize depositAmount with default zero
        double depositAmount;

        while (true) {
            System.out.print("Enter Deposit's Amount: ");
            String amountInput = myScanner.nextLine().trim();

            try {
                // converts the string of amountInput into a double and saves it into depositAmount
                depositAmount = Double.parseDouble(amountInput);

                if (depositAmount > 0) {
                    // exits loop
                    break;
                } else {
                    System.out.println("Deposits must be positive. Give it another shot!");
                }
            } catch (NumberFormatException e) { // throws an exception when user enters input that is not numbers
                System.out.println("That Doesn't Look Like A Valid Number. Let's Try Again. ");
            }
        }
        // gets the current date and formats it using the datetimeformatter eg. 2025/16/10
        String depositDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // gets the current time and formats it using the datetimeformatter where a represents the am/pm eg. 11:42:59 AM
        String depositTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // creates a transaction object using the constructor
        Transaction depositTransaction = new Transaction(depositDate, depositTime, depositDescription, depositVendor, depositAmount);
        System.out.println(userName + " You Are Depositing $ "  + depositAmount  +  " to " + depositVendor + ". \n");

        // writes the transaction object called 'depositTransaction' to transactions.csv file
        repository.saveTransaction(depositTransaction);

        // adds the object at the end of the arraylist - if we did not add to arraylist we will have to reload from csv eg. transactionsv = repository.readTransactions();
        transactions.add(depositTransaction);
        System.out.println("Deposit Transaction Was Added Successfully!");
        System.out.println("Let's Go Back Home! What Else Would You Like To Do Today?");
    }

    // guides user to add a payment and create a new transaction object, then saves it to repository
    private void makePayment() {
        System.out.println("\n");
        System.out.println("         " + userName.toUpperCase() + "'S " + " PAYMENT INFORMATION       ");

        String paymentDescription = "";
        while (paymentDescription.isEmpty()) {
            System.out.print("\nEnter Payment's Description: ");
            paymentDescription = myScanner.nextLine().trim();

            if (paymentDescription.isEmpty()) {
                System.out.println("\n" + userName + "," + " We Need A Description To Add This Payment. Let's Try That Again.");
            }
        }

        String paymentVendor = "";

        while ((paymentVendor.isEmpty())) {
            System.out.print("Enter Payment's Vendor: ");
            paymentVendor = myScanner.nextLine();

            if (paymentVendor.isEmpty()) {
                System.out.println("Don't Leave Me Hanging! Please Enter A Vendor Name.");

            }
        }

        double paymentAmount;

        while (true) {
            System.out.print("Enter Payment's Amount: ");
            String paymentInput = myScanner.nextLine().trim();

            try {
                paymentAmount = Double.parseDouble(paymentInput);
                if (paymentAmount > 0) {
                    paymentAmount = -Math.abs(paymentAmount);
                    break; // exit loop
                } else {
                    System.out.println("Payment must be positive. Give it another shot.");
                }

            } catch (NumberFormatException e) {
                System.out.println("That Does Not Look like A Valid Number. Let's Try Again.");
            }
        }

        String paymentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String paymentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transaction paymentTransaction = new Transaction(paymentDate, paymentTime, paymentDescription, paymentVendor, paymentAmount);
        System.out.println("You Are Paying $" + paymentAmount + " to " + paymentVendor + ". \n");
        repository.saveTransaction(paymentTransaction);
        transactions.add(paymentTransaction);
        System.out.println("Payment Transaction Was Recorded Successfully!");
        System.out.println("\nLet's Go Back Home! What Else Would You Like To Do Today?");

    }

    // show ledger menu options, take input
    public void showLedger() {
        boolean inLedger = true;

        while (inLedger) {
            System.out.println("                  -----------                          ");
            System.out.println("                  LEDGER MENU                         ");
            System.out.println("                  -----------                          ");
            System.out.println("A) View All Transactions");
            System.out.println("D) View Deposits Only");
            System.out.println("P) View Payments Only");
            System.out.println("R) View My Reports");
            System.out.println("X) Home Menu");
            System.out.print("\n" + userName + "," + " Type The LETTER Of Your Choice From Above To View Your Transaction History : ");

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
                case "X":
                    inLedger = false;
                    break;
                default:
                    System.out.println("Invalid Option. Please Try Again.");

            }
        }
    }

    // show all transactions
    private void showAll() {
        System.out.println("\n");
        System.out.println("ALL TRANSACTIONS : ");

        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }
        for (int i = transactions.size() - 1; i >= 0; i--) {  // newest first
            Transaction t = transactions.get(i);
            System.out.printf("%s | %s | %s | %s | %.2f%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }

    // shows all deposits
    private void showDeposits() {
        System.out.println("\n");
        System.out.println("ALL DEPOSITS : ");

        if (transactions.isEmpty()) {
            System.out.println("No Deposit Transactions To Display.");
            return;
        }
        boolean matchFound = false;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() > 0) {
                System.out.printf("%s | %s | %s | %s | %.2f%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                matchFound = true;
            }
        }
        if (!matchFound) {
            System.out.println("No Deposit Transactions To Display");
        }
    }

    // shows all payments
    private void showPayments() {
        System.out.println("\n");
        System.out.println("ALL PAYMENTS :");

        boolean matchFound = false;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getAmount() < 0) { // Payments are negative
                System.out.printf("%s | %s | %s | %s | %.2f%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                matchFound = true;
            }
        }
        if (!matchFound) {
            System.out.println("No Payment Transactions To Display");
        }
    }

    // report menu
    private void showReports() {
        boolean inReports = true;

        while (inReports) {
            System.out.println("                 -----------                          ");
            System.out.println("                  REPORT MENU                         ");
            System.out.println("                 -----------                          ");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search By Vendor");
            System.out.println("6) Back To Ledger");
            System.out.println("0) Back Home");
            System.out.print("\n" + userName + "," + " Type The LETTER Of Your Choice From Above To See Your Report : ");

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
                case "0":
                    inReports = false;
                    home();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // shows transaction from start of the current month up to today
    private void showMonthToDate() {
        System.out.println("\n");
        System.out.println("MONTH TO DATE REPORT :");

        LocalDate today = LocalDate.now();
        LocalDate firstDayofMonth = today.withDayOfMonth(1); // create a new object with the same year and month, but day set to 1
        System.out.println("Transactions From " + firstDayofMonth + " " + " to " + today);
        boolean matchFound = false; // the varible matchFound starts with no matches
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // looping backwards
        for (int i = transactions.size() - 1; i >= 0; i--) {
            // gets one transaction object from the array list at index i
            Transaction t = transactions.get(i);
            // turns the string into a localdate object called trasactiondate
            LocalDate transactionDate = LocalDate.parse(t.getDate(), formatter);
            //checks if the date is on or after the first day of the month && if the date is on or before today ( to find if the transaction happened this month )
            if (!transactionDate.isBefore(firstDayofMonth) && !transactionDate.isAfter(today)) {
                System.out.printf("%s | %s | %s | %s | %.2f%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                matchFound = true;
            }
        } // loop finished
        // check if after the loop finsihes no matches are found
        if (!matchFound) {
            System.out.println("No transactions Found For This month To Date.");
        }
    }

    // show transactions from the previous month
    private void showPreviousMonth() {

        System.out.println("\n");
        System.out.println("REVIOUS MONTH TRANSACTIONS : ");


        // current date eg. 10/16/2025
        LocalDate today = LocalDate.now();
        // 1st day of the current month eg. 1/6/2015
        LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);
        // subtract 1 day from the 1st of the current month to find the last day of the previous month. eg. 10/30/2025
        LocalDate lastDayOfPreviousMonth = firstDayOfCurrentMonth.minusDays(1);
        // sets day to 1 for the previous month eg. 10/1/2025
        LocalDate firstDayOfPreviousMonth = lastDayOfPreviousMonth.withDayOfMonth(1);

        System.out.println("\nTransactions For " + lastDayOfPreviousMonth.getMonth() + " :");

        boolean matchFound = false;
        // loops backwards through the transactions
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // converts string to localdate object
            LocalDate transactionDate = LocalDate.parse(t.getDate(), formatter);

            if (!transactionDate.isBefore(firstDayOfPreviousMonth) && !transactionDate.isAfter(lastDayOfPreviousMonth)) {
                System.out.printf("%s | %s | %s | %s | %.2f%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                matchFound = true;

            }
        }
        if (!matchFound) {
            System.out.println("No Transactions Found For " + lastDayOfPreviousMonth.getMonth());
        }

    }

    // show transaction from Jan 1st of the current year to the current date of the current year
    private void showYearToDate() {
        System.out.println("\n");
        System.out.println("YEAR TO DATE TRANSACTIONS : ");

        // todays date eg. 10/16/2025
        LocalDate today = LocalDate.now();
        //sets firstdayoftheyear to jan 1st of the current year eg. 1/1/2025
        LocalDate firstDayOfYear = today.withDayOfYear(1);
        // creating a header
        System.out.println("\nTransactions From " + firstDayOfYear.getMonth() + " " + firstDayOfYear.getYear() + " to " + today.getMonth() + " " + today.getYear());

        boolean matchFound = false;
        // looping backwards through the transactions
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // converts date string to localdate object
            LocalDate transactionDate = LocalDate.parse(t.getDate(), formatter);
            // checks to see if the date is jan 1st or after and if the date if today or earlier ( eg. range created with Minimum : Jan 1st & Maximum : 16th October )
            if ((!transactionDate.isBefore(firstDayOfYear)) && !transactionDate.isAfter(today)) {
                System.out.printf("%s | %s | %s | %s | %.2f%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                matchFound = true;
            }
        }
        if (!matchFound) {
            System.out.println("No transactions Found For" + firstDayOfYear.getMonth() + " " + firstDayOfYear.getYear() + " to " + today.getMonth() + " " + today.getYear());
        }
    }

    // show transactions from the previous year
    private void showPreviousYear() {
        System.out.println("\n");
        System.out.println("REVIOUS YEAR TRANSACTIONS : ");

        //  todays date eg. 10/16/2025
        LocalDate today = LocalDate.now();
        // changes the year  eg. 10/16/2024 and then changes the day of that year to the 1st day eg. 1/1/2024
        LocalDate firstDayOfThePreviousYear = today.minusYears(1).withDayOfYear(1);
        // changes current date to day 1, month 1 eg. 1/1/2025 and then subtracts one day eg. 12/31/2024
        LocalDate lastDayPreviousYear = today.withDayOfYear(1).minusDays(1);
        System.out.println("Transactions From " + firstDayOfThePreviousYear.getYear());

        boolean matchFound = false;
        // looping backwards
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            // convert string to localdate object
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate transactionDate = LocalDate.parse(t.getDate(), formatter);
            // checks if the date is within the previous year, not before and not after the last day of the previous year. eg. between 1st Jan 2024 to 31st Dec 2024.
            if (!transactionDate.isBefore(firstDayOfThePreviousYear) && !transactionDate.isAfter(lastDayPreviousYear)) {
                System.out.printf("%s | %s | %s | %s | %.2f%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                matchFound = true;
            }

        }
        if (!matchFound) {
            System.out.println("No transactions Found For " + firstDayOfThePreviousYear.getYear());
        }

    }

    // show transaction from specifc vendors
    private void searchByVendor() {
        System.out.println("\n");
        System.out.println("VENDOR TRANSACTIONS :");
        System.out.print("Enter Vendor Name To Search: ");
        String searchVendor = myScanner.nextLine().trim();
        System.out.println("Transactions From " + searchVendor);

        boolean matchFound = false;
        // looping backwards
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            if (t.getVendor().equalsIgnoreCase(searchVendor)) {
                System.out.printf("%s | %s | %s | %s | %.2f%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                matchFound = true;
            }
        }

        if (!matchFound) {
            System.out.println("No transactions Found For " + searchVendor);

        }
    }

    // defining ANSI codes to control color and formatting - enhancing console output
    // default everything
//    public static final String DEFULT = "\u001B[0m";
//
//    // text colours
//    // \OO1B is the escape character and then [31m part is the color itself
//    private static final String RED = "\u001B[31m";
//    public static final String GREEN = "\u001B[32m";
//    public static final String YELLOW = "\u001B[33m";
//
//    // bold text
//    private static final String BOLD = "\u001B[1m";
}

