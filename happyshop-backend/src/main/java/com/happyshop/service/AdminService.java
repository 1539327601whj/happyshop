package com.happyshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.entity.Merchant;
import com.happyshop.entity.Product;
import com.happyshop.entity.User;

public interface AdminService {

    Page<User> getUsers(int current, int size);

    void updateUserStatus(Long userId, Integer status);

    Page<Merchant> getMerchants(int current, int size, Integer status);

    void auditMerchant(Long merchantId, Integer status);

    Page<Product> getProductsForAudit(int current, int size, Integer status);

    void auditProduct(Long productId, Integer status);
}
