import view.Products;
import view.View;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static view.Products.insertProduct;


public class Main {
    public static void main(String[] args){

        try (Connection connection = View.getConnection()) {
            View view = new View(connection);
            view.start();
        }catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
    }
}