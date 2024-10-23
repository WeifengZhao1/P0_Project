package com.revature.controllers;

import com.revature.DAOs.CartDAO;
import com.revature.DAOs.ProductDAO;
import com.revature.SessionManager;
import com.revature.models.Cart;
import com.revature.utils.ConnectionUtil;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CartController {

    CartDAO cartDAO = new CartDAO();

    // Handler to add a product to the cart
    public Handler addProductToCartHandler = ctx -> {
        try {
            // Extract the session ID from the request header
            String sessionId = ctx.header("Session-ID");

            // Get the user ID from the session using the session manager
            Integer userId = SessionManager.getUserIdFromSession(sessionId);

            // Check if the user ID is valid
            if (userId == null) {
                ctx.status(401); // Unauthorized
                ctx.result("User is not logged in");
                return;
            }

            // Now extract productId and quantity from the request body
            Cart cartItem = ctx.bodyAsClass(Cart.class);

            // Check if productId is valid
            int productId = cartItem.getProductId();

            // Check if the product exists in the product table
            if (!productExists(productId)) {
                ctx.status(404); // Not Found
                ctx.result("Product ID " + productId + " is not found");
                return;
            }

            // Set the userId to the cartItem
            cartItem.setUserId(userId);

            // Check if the item already exists in the cart
            Cart existingCartItem = cartDAO.getCartItemByUserIdAndProductId(userId, productId);

            if (existingCartItem != null) {
                // If it exists, increase the quantity
                existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItem.getQuantity());
                cartDAO.updateCartItem(existingCartItem); // Update the existing cart item
                // Set the product name
                existingCartItem.setProductName(getProductNameById(productId)); // Fetch and set product name

                ctx.status(200); // OK
                ctx.json(existingCartItem); // Return the updated item
            } else {
                // If it does not exist, add it as a new item
                cartDAO.addProductToCart(cartItem);

                // Set the product name
                cartItem.setProductName(getProductNameById(productId)); // Fetch and set product name

                ctx.status(201); // Created
                ctx.json(cartItem); // Return the added item
            }
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            ctx.status(500); // Internal Server Error
            ctx.result("An error occurred: " + e.getMessage());
        }
    };

    public String getProductNameById(int productId) {
        String productName = null;
        String sql = "SELECT name FROM Products WHERE product_id = ?";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                productName = rs.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productName; // Return the product name or null if not found
    }


    // Method to check if a product exists in the product table
    private boolean productExists(int productId) {
        // Assuming you have a ProductDAO to interact with the products table
        ProductDAO productDAO = new ProductDAO();
        return productDAO.getProductById(productId) != null;
    }


    // Handler to get all items in the cart for a user
    public Handler getCartItemsHandler = ctx -> {

        // Extract user ID from the URL
        int userId = Integer.parseInt(ctx.pathParam("user_id"));

        // Retrieve cart items from the DAO
        List<Cart> cartItems = cartDAO.getCartItemsByUserId(userId);

        // Check if the cart items list is empty
        if (cartItems.isEmpty()) {
            ctx.status(200); // Optional: you can return 200 OK even if no items found
            ctx.json(Collections.singletonMap("message", "No items found in the cart for user ID " + userId + "."));
        } else {
            ctx.status(200);
            ctx.json(cartItems);
        }
    };


    // Handler to update the quantity of a product in the cart
    public Handler updateCartQuantityHandler = ctx -> {
        try {
            // Extract the cart_id from the path parameter
            int cartId = Integer.parseInt(ctx.pathParam("cart_id"));

            // Parse the request body to extract the new quantity
            Cart cartItem = ctx.bodyAsClass(Cart.class); // Assuming Cart class has a quantity field

            // Get the new quantity from the Cart object
            int newQuantity = cartItem.getQuantity();

            // Validate that the new quantity is greater than 0
            if (newQuantity <= 0) {
                ctx.status(400).result("Quantity must be greater than 0");
                return;
            }

            // Update the cart item's quantity in the database
            boolean updated = cartDAO.updateCartQuantity(cartId, newQuantity);

            // Check if the update was successful
            if (updated) {
                ctx.status(200).result("Quantity updated successfully");
            } else {
                ctx.status(400).result("Unable to update quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Server error: " + e.getMessage());
        }
    };


    // Handler to delete a product from the cart
    public Handler deleteCartItemHandler = ctx -> {
        int cartId = Integer.parseInt(ctx.pathParam("cart_id"));
        boolean deleted = cartDAO.deleteCartItem(cartId);
        if (deleted) {
            ctx.status(200).result("Product removed from cart");
        } else {
            ctx.status(400).result("Unable to remove product from cart");
        }
    };
}
