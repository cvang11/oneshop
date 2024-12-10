import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("OneShop System Menu:");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Product");
            System.out.println("3. Process Sale");
            System.out.println("4. Generate Sales Report");
            System.out.println("5. Generate Inventory Report");
            System.out.println("6. Exit");
            System.out.print("Choose an option (1-6): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    addCustomer(scanner);
                    break;
                case 2:
                    addProduct(scanner);
                    break;
                case 3:
                    processSale(scanner);
                    break;
                case 4:
                    generateSalesReport();
                    break;
                case 5:
                    generateInventoryReport();
                    break;
                case 6:
                    System.out.println("Exiting program.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void addCustomer(Scanner scanner) {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();

        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO customers (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, email);
                stmt.setString(4, phone);
                stmt.executeUpdate();
            }
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Product Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Product Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); 

        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setInt(3, quantity);
                stmt.executeUpdate();
            }
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void processSale(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        System.out.print("Enter Product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();  

        try (Connection conn = DatabaseUtil.getConnection()) {
            String productQuery = "SELECT price, quantity FROM products WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(productQuery)) {
                stmt.setInt(1, productId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("quantity");

                    if (quantity <= stock) {
                        double totalAmount = price * quantity;

                        String updateProduct = "UPDATE products SET quantity = quantity - ? WHERE id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateProduct)) {
                            updateStmt.setInt(1, quantity);
                            updateStmt.setInt(2, productId);
                            updateStmt.executeUpdate();
                        }

                        String insertSale = "INSERT INTO sales (customer_id, product_id, quantity, total_amount) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSale)) {
                            insertStmt.setInt(1, customerId);
                            insertStmt.setInt(2, productId);
                            insertStmt.setInt(3, quantity);
                            insertStmt.setDouble(4, totalAmount);
                            insertStmt.executeUpdate();
                        }

                        System.out.println("Sale processed successfully.");
                    } else {
                        System.out.println("Insufficient stock.");
                    }
                } else {
                    System.out.println("Product not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateSalesReport() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT s.id, c.first_name, c.last_name, p.name, s.quantity, s.total_amount FROM sales s " +
                         "JOIN customers c ON s.customer_id = c.id " +
                         "JOIN products p ON s.product_id = p.id";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                System.out.println("Sales Report:");
                while (rs.next()) {
                    System.out.println("Sale ID: " + rs.getInt("id") +
                                       ", Customer: " + rs.getString("first_name") + " " + rs.getString("last_name") +
                                       ", Product: " + rs.getString("name") +
                                       ", Quantity: " + rs.getInt("quantity") +
                                       ", Total Amount: " + rs.getDouble("total_amount"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateInventoryReport() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT id, name, price, quantity FROM products";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                System.out.println("Inventory Report:");
                while (rs.next()) {
                    System.out.println("Product ID: " + rs.getInt("id") +
                                       ", Name: " + rs.getString("name") +
                                       ", Price: " + rs.getDouble("price") +
                                       ", Quantity: " + rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

