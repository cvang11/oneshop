import java.sql.*;

public class Sale {
    private int customerId;
    private int productId;
    private int quantity;
    private double totalAmount;

    public Sale(int customerId, int productId, int quantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public boolean processSale() {
        Product product = Product.getProductById(this.productId);
        if (product != null && product.quantity >= this.quantity) {
            this.totalAmount = product.price * this.quantity;
            
            String insertSaleSql = "INSERT INTO Sales (customer_id, product_id, quantity, total_amount) VALUES (?, ?, ?, ?)";
            String updateProductSql = "UPDATE Products SET quantity = quantity - ? WHERE product_id = ?";

            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement insertSaleStmt = conn.prepareStatement(insertSaleSql);
                 PreparedStatement updateProductStmt = conn.prepareStatement(updateProductSql)) {
                
                conn.setAutoCommit(false);
                
                insertSaleStmt.setInt(1, this.customerId);
                insertSaleStmt.setInt(2, this.productId);
                insertSaleStmt.setInt(3, this.quantity);
                insertSaleStmt.setDouble(4, this.totalAmount);
                insertSaleStmt.executeUpdate();
                
                updateProductStmt.setInt(1, this.quantity);
                updateProductStmt.setInt(2, this.productId);
                updateProductStmt.executeUpdate();
                
                conn.commit();
                
                System.out.println("Sale processed successfully!");
                return true;
                
            } catch (SQLException e) {
                try {
                    Connection conn = DatabaseUtil.getConnection();
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
                System.out.println("Sale processing failed. Transaction rolled back.");
                return false;
            }
        } else {
            System.out.println("Insufficient stock available for the sale.");
            return false;
        }
    }

	public String getCustomerId() {
		return null;
	}

	public String getProductId() {
		return null;
	}

	public String getQuantity() {
		return null;
	}

	public String getTotalAmount() {
		return null;
	}

}
