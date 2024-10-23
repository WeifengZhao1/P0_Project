import java.sql.Connection;


import com.revature.controllers.CartController;
import com.revature.controllers.ProductController;
import com.revature.controllers.UsersController;
import com.revature.utils.ConnectionUtil;
import io.javalin.Javalin;

public class Launcher {
    public static void main(String[] args) {
        try(Connection conn = ConnectionUtil.getConnection()){
            System.out.println("CONNECTION SUCCESSFUL :>");
        } catch (Exception e) {
            e.printStackTrace(); //this is what tells us our error message (the red text)
            System.out.println("CONNECTION FAILED :<");
        }
        Javalin app = Javalin.create().start(7000);

        UsersController usersController = new UsersController();
        ProductController productsController = new ProductController();
        CartController cartController = new CartController();

        //Routes for User
        app.post("/users/register", usersController.registerUserHandler); //register
        app.post("/login", usersController.loginHandler); // login
        app.get("/users/{user_id}", usersController.getUserByIdHandler); // Get user by ID
        app.put("/users/{user_id}", usersController.updateUserNameHandler); // Update username by user_id
        app.delete("/users/{user_id}", usersController.deleteUserHandler); // Delete users by user_id
        app.get("/all-users", usersController.getAllUsersHandler); // Get all users


        // Routes for Product
        app.post("/products", productsController.insertProductHandler); // Insert new product
        app.get("/products/{product_id}", productsController.getProductByIdHandler); // Get product by ID
        app.put("/products/{product_id}", productsController.updateProductQuantityHandler); // Update product quantity
        app.delete("/products/{product_id}", productsController.deleteProductHandler); // Delete product
        app.get("all-products", productsController.getAllProductsHandler); // Get all products


        //Routes for Cart
        app.post("/cart", cartController.addProductToCartHandler); // Add to cart
        app.get("/cart/{user_id}", cartController.getCartItemsHandler); // Get cart items by user ID (No login is needed for now)
        app.put("/cart/{cart_id}", cartController.updateCartQuantityHandler); // Update cart item quantity (No login is needed for now)
        app.delete("/cart/{cart_id}", cartController.deleteCartItemHandler); // Delete cart item

    }
}
