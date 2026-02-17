package BankManagmentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Scanner sc;
    private Connection connection;
    public AccountManager(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }
    public void debitMoney(long accountNumber) throws SQLException {
        if(accountNumber ==0){
            throw new RuntimeException("Error in fetching AccountNumber ");
        }
        double realBalance = 0;
        sc.nextLine();
        System.out.println("Enter Amount to Withdraw::");
        double debitAmount = sc.nextDouble();
        if(debitAmount<=0){
            System.out.println("Amout is too Small..! Please try with Larger Amounts..!");
            return;
        }
        sc.nextLine();
        System.out.println("Enter Security PIN::");
        String pin = sc.nextLine();
        String DebitQuery = "UPDATE accounts SET balance = balance-? WHERE account_number = ?";
        String balanceQuery = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin =?";
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(balanceQuery);
            PreparedStatement preparedStatement1 = connection.prepareStatement(DebitQuery);
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2,pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                realBalance = resultSet.getDouble("balance");
                if(realBalance>=debitAmount){
                    preparedStatement1.setDouble(1,debitAmount);
                    preparedStatement1.setLong(2,accountNumber);
                    int rowsEffected = preparedStatement1.executeUpdate();
                    if(rowsEffected>0){
                        System.out.println("Success!!! Amount Withdrawn:: $"+debitAmount);
                    }
                }
                else{
                    System.out.println("Withdrawn Failed...! INSUFFICIENT BALANCE...!");
                }
            }
            else{
                System.out.println("Incorrect Security PIN....!");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);



    }
    public void creditMoney(long accountNumber) throws SQLException {
        if(accountNumber ==0){
            throw new RuntimeException("Error in fetching AccountNumber ");
        }
        double realBalance = 0;
        sc.nextLine();
        System.out.println("Enter Amount to be Credited::");
        double creditAmount = sc.nextDouble();
        if(creditAmount<=0){
            System.out.println("Amout is too Small..! Please try with Larger Amounts..!");
            return;
        }
        sc.nextLine();
        System.out.println("Enter Security PIN::");
        String pin = sc.nextLine();
        String creditQuery = "UPDATE accounts SET balance = balance+? WHERE account_number = ? AND security_pin=?";
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(creditQuery);
            preparedStatement.setDouble(1,creditAmount);
            preparedStatement.setLong(2, accountNumber);
            preparedStatement.setString(3,pin);
            int rowsEffected = preparedStatement.executeUpdate();
            if(rowsEffected>0){
                System.out.println("Amount Credited Successfully..!");
            }
            else{
                System.out.println("Incorrect Security PIN....!");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);

    }
    public void transferMoney(long accountNumber) throws  SQLException{
        System.out.println("!.......Transfer Money.......!");
        sc.nextLine();
        System.out.println("Enter Receivers Account Number::");
        long receiverAccount = sc.nextLong();
        System.out.println("Enter Amount::");
        double sendAmount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin::");
        String pin = sc.nextLine();
        if(accountNumber ==0 || receiverAccount==0){
            throw  new RuntimeException("Error in Fetching account Details..!");
        }
        String receiverNameQuery = "SELECT full_name from accounts WHERE account_number=?";
        String balanceCheckQuery = "SELECT *FROM accounts WHERE account_number = ? AND security_pin = ?";
        String debitQuery = "UPDATE accounts SET balance = balance-? WHERE  account_number = ? AND security_pin = ?";
        String creditQuery = "UPDATE accounts SET balance = balance+? WHERE  account_number = ?";
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(balanceCheckQuery);
            preparedStatement.setLong(1,accountNumber);
            preparedStatement.setString(2,pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getDouble("balance")>=sendAmount){
                    PreparedStatement preparedStatement1 = connection.prepareStatement(receiverNameQuery);
                    preparedStatement1.setLong(1,receiverAccount);
                    ResultSet resultSet1 = preparedStatement1.executeQuery();
                    if(resultSet1.next()){
                        System.out.println("Receiver Name:: "+resultSet1.getString("full_name"));
                        PreparedStatement preparedStatement2 = connection.prepareStatement(debitQuery);
                        preparedStatement2.setDouble(1,sendAmount);
                        preparedStatement2.setLong(2,accountNumber);
                        preparedStatement2.setString(3,pin);
                        int rowsEffected1 = preparedStatement2.executeUpdate();
                        PreparedStatement preparedStatement3 = connection.prepareStatement(creditQuery);
                        preparedStatement3.setDouble(1,sendAmount);
                        preparedStatement3.setLong(2,receiverAccount);
                        int rowsEffected2 = preparedStatement3.executeUpdate();
                        if(rowsEffected2>0 && rowsEffected1>0){
                            System.out.println("!.....Transfer Successfull......!");
                            System.out.println("Amount : "+sendAmount+" sent Successfully to "+resultSet1.getString("full_name"));
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                    }
                    else{
                        System.out.println("Receiver not exits in Databases..Please check details..!");
                    }
                }
                else{
                    System.out.println("Insufficient Balance..! Try with smaller Amounts.");
                }
            }
            else{
                System.out.println("Incorrect Security PIN...");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }



    }
    public void getBalance(long accountNumber){
        if(accountNumber ==0){
            throw new RuntimeException("Error in fetching AccountNumber ");
        }
        sc.nextLine();
        System.out.println("!.......Balance Enquiry......!");
        System.out.println("Enter Security PIN::");
        String pin = sc.nextLine();
        String balanceQuery = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin =?";
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(balanceQuery);
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2,pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                System.out.println("Remaining Balance::"+resultSet.getDouble("balance"));
            }
            else{
                System.out.println("Incorrect Security PIN....!");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }



    }
}
