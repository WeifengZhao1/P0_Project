package com.revature.DAOs;

import com.revature.models.Product;
import com.revature.utils.ConnectionUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class ProductDAO implements ProductDAOInterface { // Assuming there's a ProductDAOInterface to implement

    @Override
    public Product insertProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock, category) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Prepare the statement to return generated keys
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getCategory());

            // Execute the insert statement
            ps.executeUpdate();

            // Retrieve the generated product_id
            ResultSet rs = ps.getGeneratedKeys(); // Use getGeneratedKeys() to retrieve generated keys
            if (rs.next()) {
                // Get the generated product_id and set it in the product object
                product.setProduct_id(rs.getInt(1)); // Set the generated ID
            }
            return product; // Return the product object with the generated ID
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock"),
                        rs.getString("category")
                );
                product.setProduct_id(rs.getInt("product_id")); // Set the ID to the product object
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean updateProductQuantity(int productId,int newQuantity) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "UPDATE products SET stock = ? WHERE product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, newQuantity);
            ps.setInt(2, productId);

            // Execute the update
            ps.executeUpdate();

            return true; // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't update the Username");
        }
        return false;
    }

    @Override
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return true; // Returns true if the product was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't delete the product from database");
        }
        return false;
    }

    @Override
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products order by product_id";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock"),
                        rs.getString("category")
                );
                product.setProduct_id(rs.getInt("product_id")); // Set the ID to the product object
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
}
