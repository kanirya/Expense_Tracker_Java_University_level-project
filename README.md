# Expense Tracker

## Overview
The Expense Tracker is a Java-based desktop application built using   Swing   for GUI and   SQL database   for data storage. This application helps users manage their expenses by providing functionalities for category management, user management, and transaction tracking.

## Features
### 1.   User Management  
- Users can   register  ,   log in  , and   manage their credentials  .
- Authentication ensures secure access to the application.
- Logged-in users can access the dashboard and manage transactions.

### 2.   Category Management  
- Users can   add new categories   for expense tracking.
- Categories can be   edited or deleted  .
- This helps in better organization of transactions.

### 3.   Transaction Management  
- Users can   add transactions   by selecting a category.
- Transactions include details like   amount, date, and description  .
- The application provides a   history of transactions   to track expenses.

## Installation
###   Prerequisites  
- Java Development Kit (JDK)   17+  
- MySQL or any SQL-based database
- IDE like   IntelliJ IDEA   or   Eclipse  

###   Setup  
1.   Clone the Repository  
   ```sh
   git clone https://github.com/your-repo/expense-tracker.git
   cd expense-tracker
   ```
2.   Configure Database  
   - Import the `expense_tracker.sql` file into your database.
   - Update the database credentials in `DbConnection.java`.

3.   Compile and Run  
   - Open the project in an IDE.
   - Compile and run the `LoginFrame.java` to start the application.

## Usage Guide
###   Login & User Management  
1. Start the application and log in using credentials.
2. If no account exists, register a new user.
3. Upon successful login, the dashboard is displayed.

###   Category Management  
1. Navigate to the   Category   section.
2. Click   Add Category   to create a new category.
3. Click   Edit   to modify an existing category.
4. Click   Delete   to remove an unwanted category.

###   Transaction Management  
1. Navigate to the   Transaction   section.
2. Click   Add Transaction   and select a category.
3. Enter the   amount, date, and description  .
4. Click   Save   to add the transaction.
5. View the transaction history in the   Transaction History   panel.

## Database Schema
The database consists of three main tables:
1.   Users  : Stores user login details (`id, username, password`).
2.   Categories  : Stores expense categories (`id, category_name`).
3.   Transactions  : Stores user transactions (`id, user_id, category_id, amount, date, description`).

## Contributing
If you want to contribute:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature-name`).
3. Commit changes (`git commit -m "Added new feature"`).
4. Push to the branch and submit a   Pull Request  .

License
Opensource 

Developed by Muhammad Danish

