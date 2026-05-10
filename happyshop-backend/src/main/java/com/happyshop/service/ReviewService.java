package com.happyshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happyshop.entity.Review;

public interface ReviewService {

    Page<Review> getProductReviews(Long productId, int current, int size);

    void addReview(Long userId, Review review);
}
