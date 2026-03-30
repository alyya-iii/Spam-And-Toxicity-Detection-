# Spam And Toxicity Detection System (Java)

A simple spam detection system built using **Java** with a Swing-based GUI.
The project analyzes text messages and classifies them as spam or non-spam.

## Features
- Spam analysis using keyword-based logic
- Toxicity analysis using keyword-based logic
- Store messages in MySQL database
- Search messages by keyword
- Delete records
- View statistics (Spam, Toxic rates)
- Display top frequently used words
- Train system by adding new keywords
- GUI interface using Java Swing
- Clean modular package structure

## Project Structure
src/
└── spam/

├── analyzer/ # Spam detection logic

├── database/ # Message storage

├── main/ # Application entry point

├── message/ # Message model

└── swing/ # UI components

## How to Run the Project
1. Install Java (JDK 17 or later)
2. Install MySQL Server
3. Create the database (see Database Setup section)
4. Open the project in IntelliJ IDEA
5. Update database username and password in Database.java
6. Run `Main.java` from `spam.main`
3. Enter a message and its details and check the spam status

## Database Setup
1. Install MySQL Server and MySQL Workbench.
2. Open MySQL Workbench.
3. Import the database:
- Go to **Server → Data Import**
- Select **Import from Self-Contained File**
- Choose `spam_db.sql`
- Click **Start Import**

## Technologies Used
Programming Language:
- Java
Database:
- MySQL
Tools:
- IntelliJ IDEA
Technologies:
- JDBC (Java Database Connectivity)
