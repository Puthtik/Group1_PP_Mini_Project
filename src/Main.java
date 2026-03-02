import java.util.Scanner;
import java.sql.*;

interface Management {

}

public class Main {
    public static void main(String[] args){


        //Database Connection
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("File found!");
        } catch (ClassNotFoundException e){
            System.out.println("Class not found!");
        }

        String url = "jdbc:postgresql://localhost:5432/dbproduct";
        String username = "postgres";
        String password = "12345678";

        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connected");
            //Start from here
            //1. Display Product List


        } catch (SQLException e){
            System.out.println("Connection Failed");
        }
    }
}