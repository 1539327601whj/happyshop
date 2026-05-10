package com.happyshop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.common.PageResult;
import com.happyshop.common.Result;
import com.happyshop.entity.Merchant;
import com.happyshop.entity.Product;
import com.happyshop.entity.User;
import com.happyshop.service.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public Result<PageResult<User>> getUsers(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "20") int size) {
        Page<User> page = adminService.getUsers(current, size);
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal(), current, size));
    }

    @PutMapping("/users/{id}/status")
    public Result<?> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        adminService.updateUserStatus(id, status);
        return Result.success();
    }

    @GetMapping("/merchants")
    public Result<PageResult<Merchant>> getMerchants(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "20") int size) {
        Page<Merchant> page = adminService.getMerchants(current, size, status);
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal(), current, size));
    }

    @PutMapping("/merchants/{id}/audit")
    public Result<?> auditMerchant(@PathVariable Long id, @RequestParam Integer status) {
        adminService.auditMerchant(id, status);
        return Result.success();
    }

    @GetMapping("/products")
    public Result<PageResult<Product>> getProducts(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "20") int size) {
        Page<Product> page = adminService.getProductsForAudit(current, size, status);
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal(), current, size));
    }

    @PutMapping("/products/{id}/audit")
    public Result<?> auditProduct(@PathVariable Long id, @RequestParam Integer status) {
        adminService.auditProduct(id, status);
        return Result.success();
    }
}
