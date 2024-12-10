package com.example.test_end_01.entity.vo;

import com.example.test_end_01.entity.pojo.Review;
import lombok.Data;

@Data
public class ReviewVo {
    private Review reviews;
    private boolean isReview;
    public ReviewVo(Review review, boolean isReview) {
        this.reviews = review;
        this.isReview = isReview;
    }
}
