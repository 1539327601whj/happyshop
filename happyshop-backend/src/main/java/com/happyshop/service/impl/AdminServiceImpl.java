package com.happyshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.common.BusinessException;
import com.happyshop.entity.Merchant;
import com.happyshop.entity.Product;
import com.happyshop.entity.User;
import com.happyshop.mapper.MerchantMapper;
import com.happyshop.mapper.ProductMapper;
import com.happyshop.mapper.UserMapper;
import com.happyshop.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;
    private final MerchantMapper merchantMapper;
    private final ProductMapper productMapper;

    public AdminServiceImpl(UserMapper userMapper, MerchantMapper merchantMapper, ProductMapper productMapper) {
        this.userMapper = userMapper;
        this.merchantMapper = merchantMapper;
        this.productMapper = productMapper;
    }

    @Override
    public Page<User> getUsers(int current, int size) {
        return userMapper.selectPage(new Page<>(current, size),
                new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime));
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        user.setStatus(status);
        userMapper.updateById(user);
    }

    @Override
    public Page<Merchant> getMerchants(int current, int size, Integer status) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        if (status != null) wrapper.eq(Merchant::getStatus, status);
        wrapper.orderByDesc(Merchant::getCreateTime);
        return merchantMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    @Transactional
    public void auditMerchant(Long merchantId, Integer status) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) throw new BusinessException("商家不存在");
        merchant.setStatus(status);
        merchantMapper.updateById(merchant);

        // 审核通过时更新用户角色为 MERCHANT
        if (status == 1) {
            User user = userMapper.selectById(merchant.getUserId());
            if (user != null) {
                user.setRole("MERCHANT");
                userMapper.updateById(user);
            }
        }
    }

    @Override
    public Page<Product> getProductsForAudit(int current, int size, Integer status) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (status != null) wrapper.eq(Product::getStatus, status);
        wrapper.orderByDesc(Product::getCreateTime);
        return productMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    public void auditProduct(Long productId, Integer status) {
        Product product = productMapper.selectById(productId);
        if (product == null) throw new BusinessException("商品不存在");
        product.setStatus(status);
        productMapper.updateById(product);
    }
}
