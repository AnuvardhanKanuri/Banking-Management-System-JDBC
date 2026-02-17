package BankManagmentSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class User {
    private Scanner sc;
    private Connection connection;
    public User(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }
    public void register(){
        sc.nextLine();
        System.out.println("Enter Email To Register::");
        String email = sc.nextLine();

        if(emailExists(email)){
            System.out.println("Oops...!Email already Registered....!");
            return;
        }
        System.out.println("Enter Full Name::");
        String fullName = sc.nextLine();
        System.out.println("Enter Password::");
        String password = sc.nextLine();
        try{
            String query = "INSERT INTO user(full_name, email, password) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int rowsEffected = preparedStatement.executeUpdate();
            if(rowsEffected>0){
                System.out.println("User Registered Successfully....!");
            }
            else{
                System.out.println("Opps.. Error :: Failed to register user...!");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public String login(){
        sc.nextLine();
        System.out.println("Enter Email to Login::");
        String email = sc.nextLine();
        if(!emailExists(email)){
            System.out.println("Opps..Check Email, Email not Registered..\nTo continue please Register with Email..");
            return null;
        }
        System.out.println("Enter Password::");
        String password = sc.nextLine();
        String query = "SELECT *FROM user WHERE email = ? and password = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            }
            else {
                System.out.println("Incorrect Password..Please enter correct one to continue..");
                return null;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean emailExists(String email){
        try{
            String query = "SELECT *FROM user WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
