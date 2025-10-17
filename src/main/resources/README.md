# Accounting Ledger CLI Application 

This is a Java command-line application for tracking financial transactions.

---

## 📋 Project Description

This application allows users to track deposits and payments. 
All transactions are stored in a CSV file and can be viewed with various filters and pre-defined reports.

---

## ✨ Features

### Home Screen
- **D) Add Deposit** - Record incoming transactions
- **P) Make Payment** - Record outgoing payments
- **L) Ledger** - View transaction history
- **X) Exit** - Close the application

### Ledger Screen
- **A) All** - Display all transactions (newest first)
- **D) Deposits** - Show deposits only
- **P) Payments** - Show payments only
- **R) Reports** - Access report menu

### Reports Menu
- **1) Month To Date** - Current month transactions to Date
- **2) Previous Month** - Last month's transactions
- **3) Year To Date** - Current year transactions to Date
- **4) Previous Year** - Last year's transactions
- **5) Search by Vendor** - Filter by vendor name

---

## 🚀 How to Run

1. Open the project in IntelliJ IDEA
2. Run the `Main.java` file
3. Follow the on-screen prompts to navigate menus
4. Transactions are automatically saved to `transactions.csv`

---

## 💾 Data Format

Transactions are stored in CSV format with pipe delimiters:
```
date|time|description|vendor|amount
2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
2023-04-15|11:15:00|Invoice 1001 paid|Joe|1500.00
```

**Note:** Deposits are positive values, payments are negative values.

---

## 📸 Screenshots

### Home Menu
![Home Menu](screenshots/home-menu.png)
*Main navigation with deposit, payment, and ledger options*

### Adding a Deposit
![Add Deposit](screenshots/add-deposit.png)
*User input form with validation*

### Ledger View
![Ledger View](screenshots/ledger-view.png)
*Transaction history displayed newest first*

### Reports Menu
![Reports Menu](screenshots/reports-menu.png)
*Report filtering options*

---

## 📝 Notes

- Application validates all user inputs
- Payments are automatically converted to negative values
- Date format: yyyy/dd/MM
- Time format: HH:mm:ss a
- Empty transactions are skipped during file reading