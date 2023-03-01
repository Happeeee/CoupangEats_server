package com.example.demo.src.store.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewOnly {
    private int reviewId;
    private int orderId;
    private String userName;
    private float score;
    private String reviewContent;
    private String reviewImage;
    private String createdAt;
}
