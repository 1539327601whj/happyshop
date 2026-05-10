package com.happyshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.happyshop.common.BusinessException;
import com.happyshop.entity.Merchant;
import com.happyshop.entity.User;
import com.happyshop.mapper.MerchantMapper;
import com.happyshop.mapper.UserMapper;
import com.happyshop.service.MerchantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;
    private final UserMapper userMapper;

    public MerchantServiceImpl(MerchantMapper merchantMapper, UserMapper userMapper) {
        this.merchantMapper = merchantMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public void applyMerchant(Long userId, Merchant merchant) {
        Merchant existing = merchantMapper.selectOne(
                new LambdaQueryWrapper<Merchant>().eq(Merchant::getUserId, userId));
        if (existing != null) {
            throw new BusinessException("已提交过入驻申请");
        }
        merchant.setUserId(userId);
        merchant.setStatus(0); // 待审核
        merchantMapper.insert(merchant);
    }

    @Override
    public Merchant getMerchantByUserId(Long userId) {
        return merchantMapper.selectOne(
                new LambdaQueryWrapper<Merchant>().eq(Merchant::getUserId, userId));
    }

    @Override
    public void updateMerchant(Long userId, Merchant merchant) {
        Merchant existing = getMerchantByUserId(userId);
        if (existing == null) {
            throw new BusinessException("商家信息不存在");
        }
        if (merchant.getShopName() != null) existing.setShopName(merchant.getShopName());
        if (merchant.getShopLogo() != null) existing.setShopLogo(merchant.getShopLogo());
        if (merchant.getShopDesc() != null) existing.setShopDesc(merchant.getShopDesc());
        if (merchant.getContactPhone() != null) existing.setContactPhone(merchant.getContactPhone());
        if (merchant.getContactName() != null) existing.setContactName(merchant.getContactName());
        merchantMapper.updateById(existing);
    }
}
