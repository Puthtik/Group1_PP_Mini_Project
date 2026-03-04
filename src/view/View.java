package view;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import org.nocrala.tools.texttablefmt.BorderStyle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static view.Products.*;

public class View {
    List<Products> products = new ArrayList<>();
    List<Products> Updateproducts = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        return DriverManager.getConnection(url,username,password);
    }

    private Connection connection;

    public View(Connection connection) {
        this.connection = connection;
        showTable();
        showMenu();
        inputOption();
    }

    public void showTable() {

        String sql = "SELECT * FROM tbproducts ORDER BY id LIMIT 10";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            Table table = new Table(5, BorderStyle.UNICODE_BOX);

            table.setColumnWidth(0, 10, 15);
            table.setColumnWidth(1, 30, 40);
            table.setColumnWidth(2, 20, 25);
            table.setColumnWidth(3, 10, 15);
            table.setColumnWidth(4, 15, 20);

            table.addCell("ID");
            table.addCell("Name");
            table.addCell("Unit Price");
            table.addCell("Qty");
            table.addCell("Import Date");

            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("id")));
                table.addCell(rs.getString("product_name"));
                table.addCell(String.format("%.2f", rs.getDouble("unit_price")));
                table.addCell(String.valueOf(rs.getInt("qty")));
                table.addCell(rs.getDate("import_date").toString());
            }

            System.out.println("\nConnected Successfully\n");
            System.out.println(table.render());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Unsave Case
    public void showUnsaveItem() {

        if (products.isEmpty()) {
            System.out.println("\nNo unsaved products found.\n");
            return;
        }

        Table table = new Table(3, BorderStyle.UNICODE_BOX_WIDE);

        table.setColumnWidth(0, 25, 35);
        table.setColumnWidth(1, 15, 20);
        table.setColumnWidth(2, 10, 15);

        table.addCell(" Product Name ", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Unit Price ($) ", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Qty ", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        for (Products p : products) {
            table.addCell(
                    p.getProduct_name(),
                    new CellStyle(CellStyle.HorizontalAlign.CENTER)
            );
            table.addCell(
                    String.format("$%.2f", p.getUnit_price()),
                    new CellStyle(CellStyle.HorizontalAlign.CENTER)
            );
            table.addCell(
                    String.valueOf(p.getQty()),
                    new CellStyle(CellStyle.HorizontalAlign.CENTER)
            );
        }

        System.out.println(table.render());
    }
    public void showUnsaveUpdate(){
        if (Updateproducts.isEmpty()) {
            System.out.println("\nNo unsaved products found.\n");
            return;
        }

        Table table = new Table(3, BorderStyle.UNICODE_BOX_WIDE);

        table.setColumnWidth(0, 25, 35);
        table.setColumnWidth(1, 15, 20);
        table.setColumnWidth(2, 10, 15);

        table.addCell(" Product Name ", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Unit Price ($) ", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(" Qty ", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        for (Products p : Updateproducts) {
            table.addCell(
                    p.getProduct_name(),
                    new CellStyle(CellStyle.HorizontalAlign.CENTER)
            );
            table.addCell(
                    String.format("$%.2f", p.getUnit_price()),
                    new CellStyle(CellStyle.HorizontalAlign.CENTER)
            );
            table.addCell(
                    String.valueOf(p.getQty()),
                    new CellStyle(CellStyle.HorizontalAlign.CENTER)
            );
        }

        System.out.println(table.render());
    }
    public void unSaveCase(){
        System.out.println("UI. Iserted Items \t UU. Updated Product \t B. Back");
        String command;

        do {
            command = inputOption();

            switch (command) {

                case "UI":
                    showUnsaveItem();
                    break;

                case "UU":
                    System.out.println("Previous Page");
                    break;

                case "B":
                    System.out.println("make it back the the main menu");
                    break;
            }

        } while (!command.equals("B"));

    }

    public void showMenu(){
        System.out.println("\t".repeat(8)+"--------------- Menu ---------------");
        System.out.println("\t N  - Next Page \t P  - Previous Page \t F  - First Page \t L  - Last Page \t G  - Go To Page \n");

        System.out.println(" W  - Write \t R  - Read (id) \t U  - Update \t D  - Delete \t S  - Search (name) \t SR - Set Rows");

        System.out.println(" SA - Save \t\t UN - Unsaved \t\t BA - Backup \t RE - Restore \t E  - Exit ");

        System.out.println("\t".repeat(8)+"------------------------------------");

    }
    public String inputOption() {
        String input;
        do {
            System.out.print("\n -> Choose option: ");
            input = scanner.nextLine().trim();

            if (!Validate.isValidMenu(input)) {
                System.out.println("Invalid option! Please try again");
            }

        } while (!Validate.isValidMenu(input));

        return input.toUpperCase();
    }


    public void start() throws SQLException {

        String command;

        do {
            showTable();
            showMenu();
            command = inputOption();

            switch (command) {

                case "N":
                    System.out.println("Next Page");
                    break;

                case "P":
                    System.out.println("Previous Page");
                    break;

                case "F":
                    System.out.println("First Page");
                    break;

                case "L":
                    System.out.println("Last Page");
                    break;

                case "G":
                    System.out.println("Go To Page");
                    break;

                case "W":
                    insertProduct(products, scanner);
                    break;

                case "R":
                    break;

                case "U":
                    System.out.println("Update");
                    break;

                case "D":
                    System.out.println("Delete");
                    break;

                case "S":
                    System.out.println("Search");
                    break;

                case "SR":
                    System.out.println("Set Rows");
                    break;

                case "SA":
                    System.out.println("Save");
                    break;

                case "UN":
                    unSaveCase();
                    break;

                case "BA":
                    System.out.println("Backup");
                    break;

                case "RE":
                    System.out.println("Restore");
                    break;

                case "E":
                    System.out.println("Exit program");
                    System.exit(0);
                    break;
            }

        } while (!command.equals("E"));
    }
}