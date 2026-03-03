package view;
import org.nocrala.tools.texttablefmt.Table;
import org.nocrala.tools.texttablefmt.BorderStyle;
import java.sql.*;
import java.util.Scanner;

public class View {

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

            System.out.println("\nConnected Successfully\n");
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

}