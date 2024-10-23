package com.revature.DAOs;

import com.revature.models.Cart;

import java.util.ArrayList;

public interface CartDAOInterface {

    Cart addProductToCart(Cart cart);

    boolean updateCartQuantity(int cartId, int newQuantity);

    ArrayList<Cart> getCartItemsByUserId(int userId);

    boolean deleteCartItem(int cartId);

    Cart getCartItemByUserIdAndProductId(int userId, int productId);

    void updateCartItem(Cart cartItem);
}
