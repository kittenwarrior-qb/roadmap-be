import java.util.ArrayList;
import java.util.List;

/**
 * VÍ DỤ 4: BANK SYSTEM - ADVANCED COMPOSITION
 * Mối quan hệ: Bank HAS-A List<Customer>, Customer HAS-A Account
 * Thực hành: Hệ thống ngân hàng với nhiều customers và accounts
 */

class Account {
    private String accountNumber;
    private double balance;
    private String type;  // Savings, Checking
    
    public Account(String accountNumber, String type, double initialBalance) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.balance = initialBalance;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("✅ Deposited $" + amount + " | New balance: $" + balance);
        } else {
            System.out.println("❌ Invalid deposit amount");
        }
    }
    
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("✅ Withdrew $" + amount + " | New balance: $" + balance);
        } else {
            System.out.println("❌ Insufficient funds or invalid amount");
        }
    }
    
    public double getBalance() {
        return balance;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void displayInfo() {
        System.out.println("  Account: " + accountNumber + " (" + type + ")");
        System.out.println("  Balance: $" + balance);
    }
}

class Customer {
    private String name;
    String customerId;
    private List<Account> accounts;  // Customer HAS-A List of Accounts
    
    public Customer(String name, String customerId) {
        this.name = name;
        this.customerId = customerId;
        this.accounts = new ArrayList<>();
    }
    
    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("✅ Account " + account.getAccountNumber() + " added for " + name);
    }
    
    public Account getAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    
    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
    
    public void displayInfo() {
        System.out.println("\n👤 Customer: " + name + " (ID: " + customerId + ")");
        System.out.println("Total Accounts: " + accounts.size());
        for (Account account : accounts) {
            account.displayInfo();
        }
        System.out.println("Total Balance: $" + getTotalBalance());
    }
    
    public String getName() {
        return name;
    }
}

class Bank {
    private String bankName;
    private List<Customer> customers;  // Bank HAS-A List of Customers
    
    public Bank(String bankName) {
        this.bankName = bankName;
        this.customers = new ArrayList<>();
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
        System.out.println("✅ " + customer.getName() + " registered at " + bankName);
    }
    
    public Customer findCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.customerId.equals(customerId)) {
                return customer;
            }
        }
        return null;
    }
    
    public void displayAllCustomers() {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("  " + bankName + " - Customer List");
        System.out.println("╚════════════════════════════════╝");
        
        for (Customer customer : customers) {
            customer.displayInfo();
        }
        
        System.out.println("\nTotal Customers: " + customers.size());
    }
}

public class Example4_BankSystem {
    public static void main(String[] args) {
        // TODO: Thực hành của bạn
        // 1. Tạo Bank "VietcomBank"
        
        
        // 2. Tạo Customer "Nguyen Van A" với ID "C001"
        
        
        // 3. Tạo 2 accounts cho customer:
        //    - Savings account "ACC001" với $5000
        //    - Checking account "ACC002" với $2000
        
        
        
        // 4. Add accounts vào customer
        
        
        
        // 5. Add customer vào bank
        
        
        // 6. Tạo Customer "Tran Thi B" với ID "C002"
        
        
        // 7. Tạo 1 account cho customer B:
        //    - Savings account "ACC003" với $10000
        
        
        // 8. Add account và customer vào bank
        
        
        
        // 9. Thực hiện giao dịch:
        //    - Customer A deposit $1000 vào ACC001
        //    - Customer A withdraw $500 từ ACC002
        //    - Customer B withdraw $2000 từ ACC003
        
        
        
        
        // 10. Hiển thị tất cả customers
        
        
        System.out.println("\n\n--- SOLUTION ---");
        solutionExample();
    }
    
    private static void solutionExample() {
        Bank bank = new Bank("VietcomBank");
        
        // Customer A
        Customer customerA = new Customer("Nguyen Van A", "C001");
        Account accA1 = new Account("ACC001", "Savings", 5000);
        Account accA2 = new Account("ACC002", "Checking", 2000);
        customerA.addAccount(accA1);
        customerA.addAccount(accA2);
        bank.addCustomer(customerA);
        
        // Customer B
        Customer customerB = new Customer("Tran Thi B", "C002");
        Account accB1 = new Account("ACC003", "Savings", 10000);
        customerB.addAccount(accB1);
        bank.addCustomer(customerB);
        
        // Transactions
        System.out.println("\n--- TRANSACTIONS ---");
        accA1.deposit(1000);
        accA2.withdraw(500);
        accB1.withdraw(2000);
        
        // Display all
        bank.displayAllCustomers();
    }
}
