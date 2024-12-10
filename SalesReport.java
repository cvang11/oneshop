
import java.sql.*;

public class SalesReport {
    public static void generateSalesReport() {
        String sql = "SELECT c.first_name, c.last_name, p.product_name, s.quantity, s.total_amount, s.sale_date " +
                     "FROM Sales s " +
                     "JOIN Customers c ON s.customer_id = c.customer_id " +
                     "JOIN Products p ON s.product_id = p.product_id";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Customer: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Product: " + rs.getString("product_name"));
                System.out.println("Quantity: " + rs.getInt("quantity"));
                System.out.println("Total Amount: $" + rs.getDouble("total_amount"));
                System.out.println("Sale Date: " + rs.getTimestamp("sale_date"));
                System.out.println("-------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
