package com.happyshop.service;

import com.happyshop.entity.Merchant;

public interface MerchantService {

    void applyMerchant(Long userId, Merchant merchant);

    Merchant getMerchantByUserId(Long userId);

    void updateMerchant(Long userId, Merchant merchant);
}
