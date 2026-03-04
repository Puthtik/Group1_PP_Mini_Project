package view;

import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import org.nocrala.tools.texttablefmt.BorderStyle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.awt.Color.GREEN;


public class View {

    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    List<Products> products = new ArrayList<>();
    List<Products> Updateproducts = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    private int currentPage = 1;
    private int rowsPerPage = 10;
    private int totalRecords;
    private int totalPages;

    private Connection connection;

    public View(Connection connection) {
        this.connection = connection;

    }

    private void showTable() {
        calculateTotalPages();

        int offset = (currentPage - 1) * rowsPerPage;

        String sql = """
                SELECT * FROM tbproducts
                ORDER BY id
                LIMIT ? OFFSET ?
                """;

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rowsPerPage);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();

            Table table = new Table(5, BorderStyle.UNICODE_BOX);
            CellStyle center = new CellStyle(CellStyle.HorizontalAlign.center);
            CellStyle left = new CellStyle(CellStyle.HorizontalAlign.left);

            table.setColumnWidth(0, 12, 18);
            table.setColumnWidth(1, 35, 45);
            table.setColumnWidth(2, 20, 25);
            table.setColumnWidth(3, 12, 15);
            table.setColumnWidth(4, 20, 25);

            table.addCell(  View.ANSI_BLUE+ "ID" + View.ANSI_RESET, center);
            table.addCell(  View.ANSI_BLUE+ "Name" + View.ANSI_RESET, center);
            table.addCell(  View.ANSI_BLUE+ "Unit Price" + View.ANSI_RESET, center);
            table.addCell(View.ANSI_BLUE+ "Quantity" + View.ANSI_RESET, center);
            table.addCell(  View.ANSI_BLUE+ "Import Date" + View.ANSI_RESET, center);

            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("id")), center);
                table.addCell(rs.getString("product_name"), left);
                table.addCell(String.format("%.2f", rs.getDouble("unit_price")), center);
                table.addCell(String.valueOf(rs.getInt("qty")), center);
                table.addCell(rs.getDate("import_date").toString(), center);
            }

            System.out.println(table.render());
            System.out.println("Page " + currentPage + " of " + totalPages);
            System.out.println("Total Records: " + totalRecords);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showMenu() {
        System.out.println("\t".repeat(8) + "--------------- Menu ---------------");
        System.out.println("\t N  - Next Page \t P  - Previous Page \t F  - First Page \t L  - Last Page \t G  - Go To Page \n");

        System.out.println(" W  - Write \t R  - Read (id) \t U  - Update \t D  - Delete \t S  - Search (name) \t SR - Set Rows");

        System.out.println(" SA - Save \t\t UN - Unsaved \t\t BA - Backup \t RE - Restore \t E  - Exit ");

        System.out.println("\t".repeat(8) + "------------------------------------");

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

        table.addCell(" Product Name ", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(" Unit Price ($) ", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(" Qty ", new CellStyle(CellStyle.HorizontalAlign.center));

        for (Products p : products) {
            table.addCell(
                    p.getProduct_name(),
                    new CellStyle(CellStyle.HorizontalAlign.center)
            );
            table.addCell(
                    String.format("$%.2f", p.getUnit_price()),
                    new CellStyle(CellStyle.HorizontalAlign.center)
            );
            table.addCell(
                    String.valueOf(p.getQty()),
                    new CellStyle(CellStyle.HorizontalAlign.center)
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

        table.addCell(" Product Name ", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(" Unit Price ($) ", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(" Qty ", new CellStyle(CellStyle.HorizontalAlign.center));

        for (Products p : Updateproducts) {
            table.addCell(
                    p.getProduct_name(),
                    new CellStyle(CellStyle.HorizontalAlign.center)
            );
            table.addCell(
                    String.format("$%.2f", p.getUnit_price()),
                    new CellStyle(CellStyle.HorizontalAlign.center)
            );
            table.addCell(
                    String.valueOf(p.getQty()),
                    new CellStyle(CellStyle.HorizontalAlign.center)
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



    public String inputOption() {

        String input;

        do {
            System.out.print("\n -> Choose option: ");
            input = scanner.nextLine().trim().toUpperCase();

            if (!Validate.isValidMenu(input)) {
                System.out.println("Invalid option! Please try again");
            }

        } while (!Validate.isValidMenu(input));

        return input;
    }



    public void start() {
        String command;
        do {
            showTable();
            showMenu();
            command = inputOption();

            switch (command) {

                case "N":
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println(RED + "Already last page." + RESET);
                    }
                    break;
                case "P":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println(RED + "Already first page." + RESET);
                    }
                    break;
                case "F":
                    currentPage = 1;
                    break;
                case "L":
                    currentPage = totalPages;
                    break;
                case "G":
                    try {
                        System.out.print("Enter page number: ");
                        int page = Integer.parseInt(scanner.nextLine());

                        if (page >= 1 && page <= totalPages) {
                            currentPage = page;
                        } else {
                            System.out.println(RED + "Invalid page number." + RESET);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(RED + "Page must be a number." + RESET);
                    }
                    break;
                case "W":
                    insertProduct(products, scanner);
                    break;
                case "R":
                    readById();
                    break;
                case "U":
                    updateMenu();
                    break;
                case "D":
                    System.out.println(YELLOW + "Delete feature coming soon." + RESET);
                    break;
                case "S":
                    SearchName search = new SearchName(connection);
                    search.searchName();
                    break;
                case "SR":
                    try {
                        System.out.print("Enter number of rows per page: ");
                        rowsPerPage = Integer.parseInt(scanner.nextLine());
                        currentPage = 1;
                    } catch (NumberFormatException e) {
                        System.out.println(RED + "Rows must be a number." + RESET);
                    }
                    break;
                case "SA":
                    System.out.println(YELLOW + "Save feature coming soon." + RESET);
                    break;
                case "UN":
                    unSaveCase();
                    break;
                case "BA":
                    System.out.println(YELLOW + "Backup feature coming soon." + RESET);
                    break;
                case "RE":
                    System.out.println(YELLOW + "Restore feature coming soon." + RESET);
                    break;
                case "E":
                    System.out.println("Exiting program... Goodbye!" + RESET);
                    break;

                default:
                    System.out.println(RED + "Invalid option." + RESET);
            }

        } while (!command.equals("E"));
    }

    // Pagination method
    private void calculateTotalPages() {
        String sql = "SELECT COUNT(*) FROM tbproducts";
        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                totalRecords = rs.getInt(1);
                totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMenu() {

        try {
            System.out.print("Input ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());

            String sql = "SELECT * FROM tbproducts WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("ID not found!");
                return;
            }

            String name = rs.getString("product_name");
            double price = rs.getDouble("unit_price");
            int qty = rs.getInt("qty");
            Date importDate = rs.getDate("import_date");

            String choice;

            do {

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

                table.addCell(String.valueOf(id));
                table.addCell(name);
                table.addCell(String.valueOf(price));
                table.addCell(String.valueOf(qty));
                table.addCell(importDate.toString());

                System.out.println(table.render());

                System.out.println("1. Name  2. Unit Price  3. Qty  4. All Field  5. Exit");
                System.out.print("Choose option to update: ");
                choice = scanner.nextLine();

                switch (choice) {

                    case "1":
                        System.out.print("Enter new name: ");
                        name = scanner.nextLine();
                        break;

                    case "2":
                        System.out.print("Enter new price: ");
                        price = Double.parseDouble(scanner.nextLine());
                        break;

                    case "3":
                        System.out.print("Enter new qty: ");
                        qty = Integer.parseInt(scanner.nextLine());
                        break;

                    case "4":
                        System.out.print("Enter new name: ");
                        name = scanner.nextLine();
                        System.out.print("Enter new price: ");
                        price = Double.parseDouble(scanner.nextLine());
                        System.out.print("Enter new qty: ");
                        qty = Integer.parseInt(scanner.nextLine());
                        break;

                    case "5":
                        System.out.println("Exit updating...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } while (!"5".equals(choice));

        } catch (Exception e) {
            System.out.println("Invalid Input!");
        }
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

    public void readById(){

        System.out.print("Please Input id to get product : ");
        String input = scanner.nextLine();
        int productId = 0;
        try{
            productId = Integer.parseInt(input);
        }catch (NumberFormatException e){
            System.out.println( RED + "ID must be number !" + RESET);
            return;
        }
        String sql = "SELECT * FROM tbproducts WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, productId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                // Create a table for a single record
                Table table = new Table(5, BorderStyle.UNICODE_BOX);
                CellStyle center = new CellStyle(CellStyle.HorizontalAlign.center);

                table.addCell(BLUE + "ID" + RESET, center);
                table.addCell(BLUE +"Name" + RESET, center);
                table.addCell(BLUE +"Unit Price" + RESET, center);
                table.addCell(BLUE +"Qty" + RESET, center);
                table.addCell(BLUE +"Import Date" + RESET, center);

                table.addCell(String.valueOf(rs.getInt("id")));
                table.addCell(rs.getString("product_name"));
                table.addCell(String.valueOf(rs.getDouble("unit_price")));
                table.addCell(String.valueOf(rs.getInt("qty")));
                table.addCell(rs.getDate("import_date").toString());

                System.out.println(table.render());

                // Pause so the user can actually see the record before the menu refreshes
                System.out.print(YELLOW + "Press Enter to continue..." + RESET);
                scanner.nextLine();

            } else {
                System.out.println(RED+"No record was found with id : "+ RESET + productId);
                System.out.print(YELLOW + "Press enter to continue..." + RESET);
                scanner.nextLine();
            }

        } catch (SQLException e) {
            System.out.println("Error reading product: " + e.getMessage());
        }
    }

}
