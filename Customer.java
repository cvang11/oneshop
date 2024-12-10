import java.sql.*;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Customer(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public static void addCustomer(Customer customer) {
        String sql = "INSERT INTO Customers (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.firstName);
            stmt.setString(2, customer.lastName);
            stmt.setString(3, customer.email);
            stmt.setString(4, customer.phone);
            stmt.executeUpdate();
            System.out.println("Customer added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
