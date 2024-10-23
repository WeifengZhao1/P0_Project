package com.revature.DAOs;

import com.revature.models.Cart;
import com.revature.utils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;

public class CartDAO {

    // Method to add a product to the cart
    public Cart addProductToCart(Cart cart) {
        String sql = "INSERT INTO Cart (user_id, product_id, quantity) VALUES (?, ?, ?) RETURNING cart_id";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cart.getUserId());
            ps.setInt(2, cart.getProductId());
            ps.setInt(3, cart.getQuantity());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cart.setCartId(rs.getInt("cart_id"));
                return cart; // Return the cart object with the generated cart_id
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    // Method to update the quantity of a product in the cart
    public boolean updateCartQuantity(int cartId, int newQuantity) {
        String sql = "UPDATE Cart SET quantity = ? WHERE cart_id = ?";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Method to get all items in the cart for a user
    public ArrayList<Cart> getCartItemsByUserId(int userId) {
        ArrayList<Cart> cartItems = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.user_id, c.product_id, c.quantity, p.name AS product_name " +
                "FROM Cart c " +
                "JOIN Products p ON c.product_id = p.product_id " +
                "WHERE c.user_id = ?";

        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cart cartItem = new Cart(
                        rs.getInt("cart_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getString("product_name") // Assuming you add this field in Cart class
                );
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cartItems;
    }

    // Method to delete a product from the cart
    public boolean deleteCartItem(int cartId) {
        String sql = "DELETE FROM Cart WHERE cart_id = ?";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Return true if delete was successful
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // In CartDAO.java
    public Cart getCartItemByUserIdAndProductId(int userId, int productId) {
        Cart cartItem = null;
        String sql = "SELECT * FROM Cart WHERE user_id = ? AND product_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cartItem = new Cart();
                cartItem.setCartId(rs.getInt("cart_id"));
                cartItem.setUserId(rs.getInt("user_id"));
                cartItem.setProductId(rs.getInt("product_id"));
                cartItem.setQuantity(rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItem;
    }

    // In CartDAO.java
    public void updateCartItem(Cart cartItem) {
        String sql = "UPDATE Cart SET quantity = ? WHERE cart_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getQuantity());
            stmt.setInt(2, cartItem.getCartId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}