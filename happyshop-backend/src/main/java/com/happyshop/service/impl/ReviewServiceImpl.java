package com.happyshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.entity.Review;
import com.happyshop.mapper.ReviewMapper;
import com.happyshop.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Page<Review> getProductReviews(Long productId, int current, int size) {
        Page<Review> page = new Page<>(current, size);
        return reviewMapper.selectPage(page, new LambdaQueryWrapper<Review>()
                .eq(Review::getProductId, productId)
                .eq(Review::getStatus, 1)
                .orderByDesc(Review::getCreateTime));
    }

    @Override
    public void addReview(Long userId, Review review) {
        review.setUserId(userId);
        review.setStatus(1);
        reviewMapper.insert(review);
    }
}
