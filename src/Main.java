import view.View;

import java.sql.*;


public class Main {
    public static void main(String[] args){
        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("File found!");
        } catch (ClassNotFoundException e){
            System.out.println("Class not found!");
        }

        try (Connection connection = DriverManager.getConnection(url,username,password)) {

            System.out.println("Connected");
            View view = new View(connection);
            view.start();


        }catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
    }
}