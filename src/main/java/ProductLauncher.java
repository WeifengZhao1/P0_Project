import com.revature.DAOs.ProductDAO;
import com.revature.models.Product;
import com.revature.models.Users;
import com.revature.utils.ConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

public class ProductLauncher {
    public static void main(String[] args) {
        try(Connection conn = ConnectionUtil.getConnection()){
            System.out.println("CONNECTION SUCCESSFUL :>");
        } catch (Exception e) {
            e.printStackTrace(); //this is what tells us our error message (the red text)
            System.out.println("CONNECTION FAILED :<");
        }

        ProductDAO productDAO = new ProductDAO();
        Product newProduct1 = new Product( "Iphone 16 Pro Max", "Hello, Apple Intelligence. A18 Pro chip. Camera Control. Buy now!", BigDecimal.valueOf(1199.99), 50, "Electronics");
        Product newProduct2 = new Product("Notebook", "A spiral-bound notebook for writing notes", new BigDecimal("2.49"), 100, "Education");
        Product newProduct3 = new Product("WALL-E", "WALL-E, short for Waste Allocation Load Lifter Earth-class, is the last robot left on Earth.", new BigDecimal("15.99"), 30, "Toy");

//        System.out.println("Inserted Products:");
//        productDAO.insertProduct(newProduct1);
//        productDAO.insertProduct(newProduct2);
//        productDAO.insertProduct(newProduct3);
//        System.out.println(newProduct1);
//        System.out.println(newProduct2);
//        System.out.println(newProduct3);


        // Retrieve and display all products
        ArrayList<Product> products = productDAO.getAllProducts();
        System.out.println("\nAll Products:");
        for (Product product : products) {
            System.out.println(product);
        }

        //get product by id
        Product pd = productDAO.getProductById(1);
        System.out.println("\nGet Product By ID");
        System.out.println(pd);

        //Update the product quantity by ID
        boolean isUpdated = productDAO.updateProductQuantity(1, 15); // Update stock of product ID 1
        System.out.println("Product updated: " + isUpdated);

        products = productDAO.getAllProducts();
        System.out.println("\nAll Products:");
        for (Product product : products) {
            System.out.println(product);
        }

        //Delete the product by ID
        productDAO.deleteProduct(2);
        System.out.println("After delete");
        products = productDAO.getAllProducts();
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
