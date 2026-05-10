package com.happyshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.entity.Product;

public interface ProductService {

    Page<Product> getProducts(int current, int size, Long categoryId, String keyword, String sort);

    Product getProductDetail(Long id);

    void addProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(Long id);

    void updateStatus(Long id, Integer status);
}
