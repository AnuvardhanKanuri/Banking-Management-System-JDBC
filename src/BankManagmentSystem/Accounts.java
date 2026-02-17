package BankManagmentSystem;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner sc;
    public Accounts(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }
    public boolean accountExists(String email){
        try{
            String query = "SELECT *FROM accounts WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public long getAccountNumber(String email){
        String query = "Select account_number from accounts WHERE email=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
            else{
                return 0;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Unable to fetch account number...!");
    }
    public void createAccount(String email){
        String query = "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) Values(?, ?,?, ?,?)";
        sc.nextLine();
        System.out.println("Enter Full name::");
        String fullName = sc.nextLine().toUpperCase();
        System.out.println("Enter Initial Deposit::");
        Double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security PIN for Bank Transactions..::");
        String pin = sc.nextLine();
        try{
            long accountNumber = generateAccountNumber();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,accountNumber);
            preparedStatement.setString(2,fullName);
            preparedStatement.setString(3,email);
            preparedStatement.setDouble(4,amount);
            preparedStatement.setString(5,pin);
            int rowsEffected = preparedStatement.executeUpdate();
            if(rowsEffected>0){
                System.out.println("Account created Successfully...!\nAccount number::"+accountNumber);
                String trasactionQuery = "INSERT INTO transactions(account_number, transaction_type, amount, related_account) VALUES(?,'Creation',?, NULL)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(trasactionQuery);
                preparedStatement2.setLong(1,accountNumber);
                preparedStatement2.setDouble(2,amount);
                preparedStatement2.executeUpdate();

                return ;
            }
            else {
                throw new RuntimeException("Account Creation Failed..!");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public long generateAccountNumber(){
        String query = "SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                return resultSet.getLong("account_number") + 1;
            }
            else {
                return 80963570;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 80963570;
    }
}
