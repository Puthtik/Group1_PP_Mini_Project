package view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Products {
    private int id;
    private String product_name;
    private double unit_price;
    private int qty;
    private Date date;

    public Products(String product_name, double unit_price, int qty) {
//        this.id = id;
        this.product_name = product_name;
        this.unit_price = unit_price;
        this.qty = qty;
    }

//    public int getId() { return id; }
    public String getProduct_name() { return product_name; }
    public double getUnit_price() { return unit_price; }
    public int getQty() { return qty; }
    private void setImportDate(Date importDate) {
    }
    private void setId(int productId) {
    }

    //insert product into List
    public static void insertProduct(List<Products> products, Scanner sc) {
//        System.out.print("Enter Product ID: ");
//        int id = sc.nextInt();
//        sc.nextLine();

        System.out.print("Enter Product Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Unit Price: ");
        double price = sc.nextDouble();

        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();

        Products product = new Products(name, price, qty);
        products.add(product);

        System.out.println("Product inserted successfully");
    }


}
