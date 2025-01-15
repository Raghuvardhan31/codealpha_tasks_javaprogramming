# Banking Application

This is a simple console-based banking application developed in Java. It allows users to perform basic banking operations such as depositing money, withdrawing money, and checking account balances. The application uses MySQL as the database for storing account information.

## Features
- **Deposit**: Add money to an account.
- **Withdraw**: Withdraw money from an account, ensuring sufficient funds are available.
- **Check Balance**: View the current balance of an account.

## Prerequisites
- **Java Development Kit (JDK)**: Ensure you have JDK 8 or later installed.
- **MySQL Database**: The application connects to a MySQL database to manage account data.
- **Database Setup**: Create a database and table as described below.

## Database Setup
1. Create a database named `bank`:
   ```sql
   CREATE DATABASE bank;

  USE bank;
  CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    balance DOUBLE DEFAULT 0.0
    );
Usage
Start the application: Follow the menu options displayed in the console.

Deposit: Enter the account number and the amount to deposit.
Withdraw: Enter the account number and the amount to withdraw. Ensure sufficient funds are available.
Check Balance: Enter the account number to view the current balance.
Exit: Select option 4 to exit the application.

Error Handling
If an invalid account number is entered, the application will notify the user.
If insufficient funds are available during a withdrawal, the application will display an error message.
Dependencies
MySQL Connector for Java: Ensure the MySQL JDBC driver (mysql-connector-java) is included in your classpath.
Limitations
No user authentication is implemented.
Transactions are not logged.
The application is single-threaded and not suitable for concurrent users.