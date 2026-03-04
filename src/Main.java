import view.View;

import java.sql.*;


public class Main {
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";
    public static void main(String[] args){

        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("Class not found!");
        }

        try (Connection connection = DriverManager.getConnection(url,username,password)) {

            System.out.println(GREEN+"=".repeat(200)+ RESET);
            System.out.println("""
                                                                                          
                                                                                           Stock Management System
                    """);
            System.out.println(GREEN+"=".repeat(200)+ RESET);
            View view = new View(connection);
            view.start();


        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}