package view;

import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import org.nocrala.tools.texttablefmt.BorderStyle;

import java.sql.*;
import java.util.Scanner;

public class View {

    Scanner scanner = new Scanner(System.in);

    private int currentPage = 1;
    private int rowsPerPage = 10;
    private int totalRecords;
    private int totalPages;

    private Connection connection;

    public View(Connection connection) {
        this.connection = connection;
        showTable();
        showMenu();
        inputOption();

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

            table.addCell("ID", center);
            table.addCell("Name", center);
            table.addCell("Unit Price", center);
            table.addCell("Qty", center);
            table.addCell("Import Date", center);

            while (rs.next()) {

                table.addCell(String.valueOf(rs.getInt("id")));
                table.addCell(rs.getString("product_name"));
                table.addCell(String.valueOf(rs.getDouble("unit_price")));
                table.addCell(String.valueOf(rs.getInt("qty")));
                table.addCell(rs.getDate("import_date").toString());
            }

            System.out.println(table.render());
            System.out.println("Page " + currentPage + " of " + totalPages);

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
                        System.out.println("Already last page");
                    }
                    break;

                case "P":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("Already first page");
                    }
                    break;

                case "F":
                    currentPage = 1;
                    break;

                case "L":
                    currentPage = totalPages;
                    break;

                case "G":
                    System.out.print("Enter page number: ");
                    int page = scanner.nextInt();
                    scanner.nextLine();

                    if (page >= 1 && page <= totalPages) {
                        currentPage = page;
                    } else {
                        System.out.println("Invalid page number");
                    }

                    break;

                case "W":
                    System.out.println("Write");
                    break;

                case "R":
                    System.out.println("Read");
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

                    System.out.print("Enter number of rows per page: ");
                    rowsPerPage = scanner.nextInt();
                    scanner.nextLine();

                    currentPage = 1;

                    break;

                case "SA":
                    System.out.println("Save");
                    break;

                case "UN":
                    System.out.println("Unsaved");
                    break;

                case "BA":
                    System.out.println("Backup");
                    break;

                case "RE":
                    System.out.println("Restore");
                    break;

                case "E":
                    System.out.println("Exiting program..., Goodbye!");
                    break;
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
}