package com.happyshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.entity.Favorite;

public interface FavoriteService {

    Page<Favorite> getFavorites(Long userId, int current, int size);

    void addFavorite(Long userId, Long productId);

    void removeFavorite(Long userId, Long productId);

    boolean checkFavorite(Long userId, Long productId);
}
