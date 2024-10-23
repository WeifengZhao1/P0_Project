import com.revature.controllers.CartController;
import io.javalin.Javalin;

public class CartLauncher {
    public static void main(String[] args) {
        CartController cartController = new CartController();

        var app = Javalin.create().start(7000);
        app.get("/", ctx -> ctx.result("Hello Javalin and Postman!"));

        // Add routes for cart
        app.post("/cart", cartController.addProductToCartHandler); // Add to cart
        app.get("/cart/{user_id}", cartController.getCartItemsHandler); // Get cart items by user ID (No login is needed for now)
        app.put("/cart/{cart_id}", cartController.updateCartQuantityHandler); // Update cart item quantity (No login is needed for now)
        app.delete("/cart/{cart_id}", cartController.deleteCartItemHandler); // Delete cart item

       // CartDAO cartDAO = new CartDAO();

        // Create a sample cart item to add
//        Cart cartItem = new Cart();
//        cartItem.setUserId(2); // Use a valid user ID
//        cartItem.setProductId(3); // Use a valid product ID
//        cartItem.setQuantity(5); // Set desired quantity
//
//        // Test adding product to cart
//        try {
//            cartDAO.addProductToCart(cartItem);
//            System.out.println("Product added to cart successfully!");
//        } catch (Exception e) {
//            System.err.println("Failed to add product to cart: " + e.getMessage());
//        }
    }
}
