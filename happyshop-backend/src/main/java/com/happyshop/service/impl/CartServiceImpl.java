package com.happyshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.happyshop.common.BusinessException;
import com.happyshop.entity.Cart;
import com.happyshop.entity.Product;
import com.happyshop.mapper.CartMapper;
import com.happyshop.mapper.ProductMapper;
import com.happyshop.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    public CartServiceImpl(CartMapper cartMapper, ProductMapper productMapper) {
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
    }

    @Override
    public List<Cart> getCartList(Long userId) {
        return cartMapper.selectList(
                new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId).orderByDesc(Cart::getCreateTime));
    }

    @Override
    public void addToCart(Long userId, Long productId, Long skuId, Integer quantity) {
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new BusinessException("商品不存在或已下架");
        }

        Cart existing = cartMapper.selectOne(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, productId)
                .eq(skuId != null, Cart::getSkuId, skuId));

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartMapper.updateById(existing);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setSkuId(skuId);
            cart.setQuantity(quantity);
            cart.setChecked(1);
            cartMapper.insert(cart);
        }
    }

    @Override
    public void updateCartItem(Long userId, Long cartId, Integer quantity, Integer checked) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        if (quantity != null) cart.setQuantity(quantity);
        if (checked != null) cart.setChecked(checked);
        cartMapper.updateById(cart);
    }

    @Override
    public void removeCartItem(Long userId, Long cartId) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        cartMapper.deleteById(cartId);
    }

    @Override
    public void clearCart(Long userId) {
        cartMapper.delete(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
    }
}
