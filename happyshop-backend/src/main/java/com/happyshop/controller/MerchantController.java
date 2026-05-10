package com.happyshop.controller;

import com.happyshop.common.Result;
import com.happyshop.entity.Merchant;
import com.happyshop.service.MerchantService;
import com.happyshop.service.OrderService;
import com.happyshop.entity.Order;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.common.PageResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    private final MerchantService merchantService;
    private final OrderService orderService;

    public MerchantController(MerchantService merchantService, OrderService orderService) {
        this.merchantService = merchantService;
        this.orderService = orderService;
    }

    @PostMapping("/apply")
    public Result<?> apply(@RequestBody Merchant merchant) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        merchantService.applyMerchant(userId, merchant);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<Merchant> info() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(merchantService.getMerchantByUserId(userId));
    }

    @PutMapping("/info")
    public Result<?> updateInfo(@RequestBody Merchant merchant) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        merchantService.updateMerchant(userId, merchant);
        return Result.success();
    }

    @PutMapping("/orders/{id}/ship")
    public Result<?> shipOrder(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Merchant merchant = merchantService.getMerchantByUserId(userId);
        if (merchant == null) return Result.error("商家信息不存在");
        orderService.shipOrder(merchant.getId(), id);
        return Result.success();
    }
}
