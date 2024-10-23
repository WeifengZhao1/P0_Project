package com.revature.models;

public class Cart {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;

    private String productName; // New field for product name



    // Constructor
    public Cart(int cartId, int userId, int productId, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Default constructor
    public Cart() {}

    public Cart(int cartId, int userId, int productId, int quantity, String productName) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName; // Set the product name
    }

    // Getters and Setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
