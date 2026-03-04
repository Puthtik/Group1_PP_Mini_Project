package view;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import org.nocrala.tools.texttablefmt.BorderStyle;
import java.sql.*;
import java.util.Scanner;

public class View {
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";

    Scanner scanner = new Scanner(System.in);

    private Connection connection;

    public View(Connection connection) {
        this.connection = connection;
        showTable();
        showMenu();
        inputOption();

    }

    private void showTable() {

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

            System.out.println(table.render());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showMenu(){
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
    public void readById(){

        System.out.print("Please Input id to get record : ");
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


    public void start() {

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
                    System.out.println("Write");
                    break;

                case "R":
                    readById();
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
                    System.out.println("Unsaved");
                    break;

                case "BA":
                    System.out.println("Backup");
                    break;

                case "RE":
                    System.out.println("Restore");
                    break;

                case "E":
                    System.out.println("Exit program");
                    break;
            }

        } while (!command.equals("E"));
    }
}