package com.happyshop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.common.PageResult;
import com.happyshop.common.Result;
import com.happyshop.entity.Review;
import com.happyshop.service.ReviewService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/products/{productId}/reviews")
    public Result<PageResult<Review>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "5") int size) {
        Page<Review> page = reviewService.getProductReviews(productId, current, size);
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal(), current, size));
    }

    @PostMapping("/reviews")
    public Result<?> addReview(@RequestBody Review review) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reviewService.addReview(userId, review);
        return Result.success();
    }
}
