package BankManagmentSystem;

import java.sql.*;


public class TransactionDetails {
    private final Connection connection;

    public TransactionDetails(Connection connection){
        this.connection = connection;

    }
    public void showTransactions(Long accountNumber){
        String query = "SELECT *FROM transactions WHERE account_number = ? ORDER BY transaction_time DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id=0;
            System.out.println("+----+----------------+------------------+--------+-----------------+---------------------+");
            System.out.println("| id | account_number | transaction_type | amount | related_account | transaction_time    |");
            System.out.println("+----+----------------+------------------+--------+-----------------+---------------------+");
            while(resultSet.next()) {
                id++;
                String type = resultSet.getString("transaction_type");
                double amount = resultSet.getDouble("amount");
                String related = resultSet.getString("related_account");
                Timestamp time = resultSet.getTimestamp("transaction_time");
                System.out.printf("|%-4s|%-16s|%-18s|%-8s|%-17s|%-21s|\n",id,accountNumber,type, amount,related,time);
                System.out.println("+----+----------------+------------------+--------+-----------------+---------------------+");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("showing transaction details of "+accountNumber);
    }
}
