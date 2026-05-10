package com.happyshop.service;

import com.happyshop.entity.Cart;
import java.util.List;

public interface CartService {

    List<Cart> getCartList(Long userId);

    void addToCart(Long userId, Long productId, Long skuId, Integer quantity);

    void updateCartItem(Long userId, Long cartId, Integer quantity, Integer checked);

    void removeCartItem(Long userId, Long cartId);

    void clearCart(Long userId);
}
