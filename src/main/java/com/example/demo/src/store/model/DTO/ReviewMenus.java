package com.example.demo.src.store.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMenus {
    private int reviewId;
    private String userName;
    private float score;
    private String reviewContent;
    private String reviewImage;
    private String createTime;
    private List<String> menus;
}
