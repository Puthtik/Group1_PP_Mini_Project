package view;

import java.sql.*;
import java.util.Scanner;
import org.nocrala.tools.texttablefmt.*;

public class SearchName {

    private Connection connection;
    private Scanner scanner = new Scanner(System.in);

    public SearchName(Connection connection) {
        this.connection = connection;
    }

    public void searchName() {

        System.out.print("Enter product name: ");
        String searchName = scanner.nextLine();

        String sql = "SELECT * FROM tbproducts WHERE product_name ILIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchName + "%");

            ResultSet rs = pstmt.executeQuery();

            Table table = new Table(5, BorderStyle.UNICODE_BOX,
                    ShownBorders.ALL);

            table.setColumnWidth(0, 10, 15);
            table.setColumnWidth(1, 30, 40);
            table.setColumnWidth(2, 20, 25);
            table.setColumnWidth(3, 10, 15);
            table.setColumnWidth(4, 15, 20);

            table.addCell("ID");
            table.addCell("NAME");
            table.addCell("PRICE");
            table.addCell("QTY");
            table.addCell("IMPORT DATE");

            boolean found = false;

            while (rs.next()) {
                found = true;

                table.addCell(String.valueOf(rs.getInt("id")));
                table.addCell(rs.getString("product_name"));
                table.addCell(rs.getBigDecimal("unit_price").toString());
                table.addCell(String.valueOf(rs.getInt("qty")));
                table.addCell(rs.getDate("import_date").toString());
            }

            if (!found) {
                table.addCell("NO PRODUCT FOUND",
                        new CellStyle(CellStyle.HorizontalAlign.center), 5);
            }

            System.out.println(table.render());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}