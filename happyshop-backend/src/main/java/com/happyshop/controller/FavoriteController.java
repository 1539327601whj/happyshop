package com.happyshop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.common.PageResult;
import com.happyshop.common.Result;
import com.happyshop.entity.Favorite;
import com.happyshop.service.FavoriteService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public Result<PageResult<Favorite>> list(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Favorite> page = favoriteService.getFavorites(userId, current, size);
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal(), current, size));
    }

    @PostMapping("/{productId}")
    public Result<?> add(@PathVariable Long productId) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        favoriteService.addFavorite(userId, productId);
        return Result.success();
    }

    @DeleteMapping("/{productId}")
    public Result<?> remove(@PathVariable Long productId) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        favoriteService.removeFavorite(userId, productId);
        return Result.success();
    }

    @GetMapping("/check/{productId}")
    public Result<Boolean> check(@PathVariable Long productId) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(favoriteService.checkFavorite(userId, productId));
    }
}
