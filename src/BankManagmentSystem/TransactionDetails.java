package BankManagmentSystem;

import java.sql.Connection;
import java.util.Scanner;

public class TransactionDetails {
    private Connection connection;
    private Scanner sc;
    public TransactionDetails(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }
    public void showTransactions(Long accountNumber){
        System.out.println("showing transaction details of "+accountNumber);
    }
}
