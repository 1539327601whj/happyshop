package com.happyshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.entity.Order;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Order createOrder(Long userId, Long addressId, List<Map<String, Object>> items, String remark);

    Page<Order> getOrders(Long userId, Integer status, int current, int size);

    Order getOrderDetail(Long userId, Long orderId);

    void payOrder(Long userId, Long orderId);

    void cancelOrder(Long userId, Long orderId);

    void confirmReceive(Long userId, Long orderId);

    void shipOrder(Long merchantId, Long orderId);
}
