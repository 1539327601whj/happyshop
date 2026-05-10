package com.happyshop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.common.PageResult;
import com.happyshop.common.Result;
import com.happyshop.entity.Order;
import com.happyshop.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Result<Order> create(@RequestBody Map<String, Object> params) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long addressId = Long.valueOf(params.get("addressId").toString());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) params.get("items");
        String remark = (String) params.get("remark");
        return Result.success(orderService.createOrder(userId, addressId, items, remark));
    }

    @GetMapping
    public Result<PageResult<Order>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Order> page = orderService.getOrders(userId, status, current, size);
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal(), current, size));
    }

    @GetMapping("/{id}")
    public Result<Order> detail(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(orderService.getOrderDetail(userId, id));
    }

    @PutMapping("/{id}/pay")
    public Result<?> pay(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.payOrder(userId, id);
        return Result.success();
    }

    @PutMapping("/{id}/cancel")
    public Result<?> cancel(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.cancelOrder(userId, id);
        return Result.success();
    }

    @PutMapping("/{id}/receive")
    public Result<?> receive(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.confirmReceive(userId, id);
        return Result.success();
    }
}
