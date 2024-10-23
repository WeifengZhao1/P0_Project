package com.revature.controllers;

import com.revature.DAOs.ProductDAO;
import com.revature.models.Product;
import io.javalin.http.Handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {

    private final ProductDAO productDAO = new ProductDAO();

    // Handler for inserting a new product
    // Handler for product insertion
    public Handler insertProductHandler = ctx -> {
        try {
            // Extract product details from the request body
            Product newProduct = ctx.bodyAsClass(Product.class);

            // Optional: You can add checks here for existing products based on name or category
            // Example: if (productDAO.isProductNameTaken(newProduct.getName())) { ... }

            // Insert the new product into the database
            Product insertedProduct = productDAO.insertProduct(newProduct);

            // Check if the product was successfully inserted
            if (insertedProduct != null) {
                ctx.status(201).json(insertedProduct); // Return the created product with ID
            } else {
                ctx.status(500).result("Product insertion failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("An error occurred while inserting the product: " + e.getMessage());
        }
    };








    // Handler for getting a product by product_id
    public Handler getProductByIdHandler = ctx -> {
        try {
            int productId = Integer.parseInt(ctx.pathParam("product_id")); // Get product_id from the path
            Product product = productDAO.getProductById(productId);

            if (product != null) {
                ctx.status(200).json(product); // Return the found product
            } else {
                ctx.status(404).result("Product with ID " + productId + " not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid product ID format");
        } catch (Exception e) {
            ctx.status(500).result("An error occurred: " + e.getMessage());
        }
    };

    // Handler for updating a product
    public Handler updateProductQuantityHandler = ctx -> {
        try {
            int productId = Integer.parseInt(ctx.pathParam("product_id")); // Get product_id from the path
            Product updatedProduct = ctx.bodyAsClass(Product.class); // Deserialize the request body

            // Check if product exists
            if (productDAO.getProductById(productId) == null) {
                ctx.status(404).result("Product with ID " + productId + " not found");
                return;
            }

            // Update the product in the database
            boolean success = productDAO.updateProductQuantity(productId, updatedProduct.getStock());

            if (success) {
                ctx.status(200).result("Product updated successfully");
            } else {
                ctx.status(500).result("Failed to update product");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid product ID format");
        } catch (Exception e) {
            ctx.status(500).result("An error occurred: " + e.getMessage());
        }
    };

    // Handler for deleting a product
    public Handler deleteProductHandler = ctx -> {
        try {
            int productId = Integer.parseInt(ctx.pathParam("product_id")); // Get product_id from the path

            // Check if product exists
            if (productDAO.getProductById(productId) == null) {
                ctx.status(404).result("Product with ID " + productId + " not found");
                return;
            }

            boolean success = productDAO.deleteProduct(productId);
            if (success) {
                ctx.status(200).result("Product deleted successfully");
            } else {
                ctx.status(500).result("Failed to delete product");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid product ID format");
        } catch (Exception e) {
            ctx.status(500).result("An error occurred: " + e.getMessage());
        }
    };

    // Handler for getting all products
    public Handler getAllProductsHandler = ctx -> {
        try {
            List<Product> products = productDAO.getAllProducts();
            if (products.isEmpty()) {
                ctx.status(404).result("No products found");
            } else {
                ctx.status(200).json(products); // Return the list of products
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("An error occurred while retrieving products: " + e.getMessage());
        }
    };



}
