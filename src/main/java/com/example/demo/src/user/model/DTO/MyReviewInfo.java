package com.example.demo.src.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyReviewInfo {
    private String storeName;
    private float score;
    private String reviewImage;
    private String reviewContent;
    private String createTime;
    private int goodCount;
}