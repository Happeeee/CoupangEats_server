package com.example.demo.src.user.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserReviewRes {
    private String storeName;
    private float score;
    private String reviewImage;
    private String reviewContent;
    private String createTime;
    private int goodCount;
    private List<String> menus;
}