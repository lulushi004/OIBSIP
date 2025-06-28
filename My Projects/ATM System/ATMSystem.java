import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ATMSystem {
    // Database simulation
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Account> accounts = new HashMap<>();
    private static List<Transaction> transactions = new ArrayList<>();
    
    // Current session
    private static User currentUser = null;
    private static Account currentAccount = null;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Initialize with a sample user for testing
        initializeSampleData();
        
        while (true) {
            System.out.println("\n===== ATM SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    if (currentUser != null) {
                        showATMMenu(scanner);
                    }
                    break;
                case 3:
                    System.out.println("Thank you for using our ATM. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void initializeSampleData() {
        // Create 5 sample users
        for (int i = 1; i <= 5; i++) {
            String userId = "user" + i;
            String name = "User " + i;
            String pin = String.format("%04d", i * 1111); // Simple pattern for sample PINs
            double balance = 1000.0 * i;
            
            User user = new User(userId, name, pin);
            String accountNumber = "ACC" + (1000 + i);
            Account account = new Account(accountNumber, userId, balance);
            
            users.put(userId, user);
            accounts.put(accountNumber, account);
        }
    }
    
    private static void registerUser(Scanner scanner) {
        System.out.println("\n===== REGISTRATION =====");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        
        if (users.containsKey(userId)) {
            System.out.println("User ID already exists. Please choose another.");
            return;
        }
        
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Set PIN (4 digits): ");
        String pin = scanner.nextLine();
        
        if (pin.length() != 4 || !pin.matches("\\d+")) {
            System.out.println("PIN must be 4 digits. Registration failed.");
            return;
        }
        
        System.out.print("Initial Deposit: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        if (initialDeposit < 0) {
            System.out.println("Invalid deposit amount. Registration failed.");
            return;
        }
        
        // Create user and account
        User newUser = new User(userId, name, pin);
        String accountNumber = "ACC" + (1000 + users.size() + 1);
        Account newAccount = new Account(accountNumber, userId, initialDeposit);
        
        users.put(userId, newUser);
        accounts.put(accountNumber, newAccount);
        
        System.out.println("\nRegistration successful!");
        System.out.println("Your Account Number: " + accountNumber);
        System.out.println("Your current balance: $" + initialDeposit);
    }
    
    private static void loginUser(Scanner scanner) {
        System.out.println("\n===== LOGIN =====");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        
        User user = users.get(userId);
        if (user == null || !user.getPin().equals(pin)) {
            System.out.println("Invalid User ID or PIN. Please try again.");
            return;
        }
        
        // Find user's account
        Account account = null;
        for (Account acc : accounts.values()) {
            if (acc.getUserId().equals(userId)) {
                account = acc;
                break;
            }
        }
        
        if (account == null) {
            System.out.println("No account found for this user.");
            return;
        }
        
        currentUser = user;
        currentAccount = account;
        System.out.println("\nLogin successful! Welcome, " + user.getName() + "!");
    }
    
    private static void showATMMenu(Scanner scanner) {
        while (currentUser != null) {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    showTransactionHistory();
                    break;
                case 2:
                    withdrawMoney(scanner);
                    break;
                case 3:
                    depositMoney(scanner);
                    break;
                case 4:
                    transferMoney(scanner);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    currentUser = null;
                    currentAccount = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void showTransactionHistory() {
        System.out.println("\n===== TRANSACTION HISTORY =====");
        System.out.println("Account: " + currentAccount.getAccountNumber());
        System.out.println("Current Balance: $" + currentAccount.getBalance());
        System.out.println("\nRecent Transactions:");
        
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAccountNumber().equals(currentAccount.getAccountNumber())) {
                accountTransactions.add(t);
            }
        }
        
        if (accountTransactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction t : accountTransactions) {
                System.out.println(t);
            }
        }
    }
    
    private static void withdrawMoney(Scanner scanner) {
        System.out.println("\n===== WITHDRAW =====");
        System.out.println("Current Balance: $" + currentAccount.getBalance());
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }
        
        if (amount > currentAccount.getBalance()) {
            System.out.println("Insufficient funds.");
            return;
        }
        
        currentAccount.setBalance(currentAccount.getBalance() - amount);
        Transaction transaction = new Transaction(
            currentAccount.getAccountNumber(), 
            "WITHDRAW", 
            -amount
        );
        transactions.add(transaction);
        
        System.out.println("Withdrawal successful!");
        System.out.println("New Balance: $" + currentAccount.getBalance());
    }
    
    private static void depositMoney(Scanner scanner) {
        System.out.println("\n===== DEPOSIT =====");
        System.out.println("Current Balance: $" + currentAccount.getBalance());
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }
        
        currentAccount.setBalance(currentAccount.getBalance() + amount);
        Transaction transaction = new Transaction(
            currentAccount.getAccountNumber(), 
            "DEPOSIT", 
            amount
        );
        transactions.add(transaction);
        
        System.out.println("Deposit successful!");
        System.out.println("New Balance: $" + currentAccount.getBalance());
    }
    
    private static void transferMoney(Scanner scanner) {
        System.out.println("\n===== TRANSFER =====");
        System.out.println("Current Balance: $" + currentAccount.getBalance());
        
        System.out.println("\nAvailable User IDs:");
        for (String userId : users.keySet()) {
            if (!userId.equals(currentUser.getUserId())) {
                System.out.println("- " + userId);
            }
        }
        
        System.out.print("\nEnter recipient's User ID (type 'random' for random user): ");
        String recipientUserId = scanner.nextLine();
        
        // Handle random user selection
        if (recipientUserId.equalsIgnoreCase("random")) {
            List<String> availableUserIds = new ArrayList<>(users.keySet());
            availableUserIds.remove(currentUser.getUserId());
            if (!availableUserIds.isEmpty()) {
                recipientUserId = availableUserIds.get((int)(Math.random() * availableUserIds.size()));
                System.out.println("Randomly selected user: " + recipientUserId);
            } else {
                System.out.println("No other users available for transfer.");
                return;
            }
        }
        
        // Validate recipient user ID
        if (!users.containsKey(recipientUserId)) {
            System.out.println("User ID not found. Please try again.");
            return;
        }
        
        if (recipientUserId.equals(currentUser.getUserId())) {
            System.out.println("Cannot transfer to yourself.");
            return;
        }
        
        // Find recipient's account
        Account recipientAccount = null;
        for (Account acc : accounts.values()) {
            if (acc.getUserId().equals(recipientUserId)) {
                recipientAccount = acc;
                break;
            }
        }
        
        if (recipientAccount == null) {
            System.out.println("No account found for this user.");
            return;
        }
        
        System.out.println("Recipient Name: " + users.get(recipientUserId).getName());
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }
        
        if (amount > currentAccount.getBalance()) {
            System.out.println("Insufficient funds.");
            return;
        }
        
        // Perform transfer
        currentAccount.setBalance(currentAccount.getBalance() - amount);
        recipientAccount.setBalance(recipientAccount.getBalance() + amount);
        
        // Record transactions
        Transaction senderTransaction = new Transaction(
            currentAccount.getAccountNumber(), 
            "TRANSFER_OUT to " + recipientAccount.getAccountNumber(), 
            -amount
        );
        
        Transaction recipientTransaction = new Transaction(
            recipientAccount.getAccountNumber(), 
            "TRANSFER_IN from " + currentAccount.getAccountNumber(), 
            amount
        );
        
        transactions.add(senderTransaction);
        transactions.add(recipientTransaction);
        
        System.out.println("\nTransfer successful to " + users.get(recipientUserId).getName() + "!");
        System.out.println("New Balance: $" + currentAccount.getBalance());
    }
    
    // Inner classes for data models
    static class User {
        private String userId;
        private String name;
        private String pin;
        
        public User(String userId, String name, String pin) {
            this.userId = userId;
            this.name = name;
            this.pin = pin;
        }
        
        public String getUserId() { return userId; }
        public String getName() { return name; }
        public String getPin() { return pin; }
    }
    
    static class Account {
        private String accountNumber;
        private String userId;
        private double balance;
        
        public Account(String accountNumber, String userId, double balance) {
            this.accountNumber = accountNumber;
            this.userId = userId;
            this.balance = balance;
        }
        
        public String getAccountNumber() { return accountNumber; }
        public String getUserId() { return userId; }
        public double getBalance() { return balance; }
        public void setBalance(double balance) { this.balance = balance; }
    }
    
    static class Transaction {
        private String accountNumber;
        private String type;
        private double amount;
        private String timestamp;
        
        public Transaction(String accountNumber, String type, double amount) {
            this.accountNumber = accountNumber;
            this.type = type;
            this.amount = amount;
            this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %-25s %10.2f", 
                timestamp, type, amount);
        }
        
        public String getAccountNumber() { return accountNumber; }
        public String getType() { return type; }
        public double getAmount() { return amount; }
        public String getTimestamp() { return timestamp; }
    }
}