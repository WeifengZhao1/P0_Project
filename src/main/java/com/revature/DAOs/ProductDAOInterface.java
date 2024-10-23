package com.revature.DAOs;

import com.revature.models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface ProductDAOInterface {

    // Method to insert a new product
    Product insertProduct(Product product);

    // Method to get all products
    ArrayList<Product> getAllProducts();

    // Method to get product by ID
    Product getProductById(int id);

    // Method to update product quantity
    boolean updateProductQuantity(int productId, int newQuantity);

    // Method to update product stock
    //boolean updateProductStock(int productId, int newStock);

    // Method to update product price
   // boolean updateProductPrice(int productId, BigDecimal newPrice);

    boolean deleteProduct(int id);
}


