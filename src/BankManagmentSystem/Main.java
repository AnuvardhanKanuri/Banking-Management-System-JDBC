package BankManagmentSystem;
import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/ banking_system ";
    private static final String user = "root";
    private static final String password = "mySQL@9618";
    public static void main(String[] args){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Loading Drivers....");
        }
        catch(ClassNotFoundException e){
            System.out.println("Error in Loading Drivers::"+e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            Scanner sc = new Scanner(System.in);
            User user = new User(connection, sc);
            Accounts accounts = new Accounts(connection, sc);
            AccountManager Manager = new AccountManager(connection, sc);
            TransactionDetails transactionDetails = new TransactionDetails(connection);
            System.out.println("Welcome to Financial Services");
            System.out.println("----------------------------------------");
            while(true) {
                System.out.println("Choose option below\n1.Login to Account\n2.Register new Account\n3.Exit");
                int choice = sc.nextInt();
                String email;
                long accountNumber;
                switch (choice) {
                    case 1:
                        email = user.login();
                        if (email != null) {
                            System.out.println("User Logged In Successfully");
                            if (!accounts.accountExists(email)) {
                                System.out.println("1.Open new account.\n2.Exit the bank services.");
                                if (sc.nextInt() == 1) {
                                    accounts.createAccount(email);
                                } else {
                                    break;
                                }
                            }
                            accountNumber = accounts.getAccountNumber(email);
                            int choice2;
                            while (true) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Check Transactions");
                                System.out.println("6. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = sc.nextInt();
                                switch (choice2) {
                                    case 1:
                                        Manager.debitMoney(accountNumber);
                                        break;
                                    case 2:
                                        Manager.creditMoney(accountNumber);
                                        break;
                                    case 3:
                                        Manager.transferMoney(accountNumber);
                                        break;
                                    case 4:
                                        Manager.getBalance(accountNumber);
                                        break;
                                    case 5:
                                        transactionDetails.showTransactions(accountNumber);
                                        break;
                                    case 6:
                                        System.out.println("Thank You for Using Bank Services...!");
                                        System.out.println("User logged out successfully..");
                                        return;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }

                        }
                        break;
                    case 2:
                        user.register();
                        break;
                    case 3:
                        System.out.println("Thankyou for using Banking System..!");
                        return;
                    default:
                        System.out.println("Enter a Valid Choice..");
                }
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
