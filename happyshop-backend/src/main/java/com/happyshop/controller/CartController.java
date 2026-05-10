package com.happyshop.controller;

import com.happyshop.common.Result;
import com.happyshop.entity.Cart;
import com.happyshop.service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Result<List<Cart>> list() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(cartService.getCartList(userId));
    }

    @PostMapping
    public Result<?> add(@RequestBody Map<String, Object> params) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long productId = Long.valueOf(params.get("productId").toString());
        Long skuId = params.get("skuId") != null ? Long.valueOf(params.get("skuId").toString()) : null;
        Integer quantity = params.get("quantity") != null ? Integer.valueOf(params.get("quantity").toString()) : 1;
        cartService.addToCart(userId, productId, skuId, quantity);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer quantity = params.get("quantity") != null ? Integer.valueOf(params.get("quantity").toString()) : null;
        Integer checked = params.get("checked") != null ? Integer.valueOf(params.get("checked").toString()) : null;
        cartService.updateCartItem(userId, id, quantity, checked);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> remove(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cartService.removeCartItem(userId, id);
        return Result.success();
    }

    @DeleteMapping("/clear")
    public Result<?> clear() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cartService.clearCart(userId);
        return Result.success();
    }
}
