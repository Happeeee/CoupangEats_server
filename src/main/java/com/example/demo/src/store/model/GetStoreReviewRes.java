package com.example.demo.src.store.model;

import com.example.demo.src.store.model.DTO.ReviewMenus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreReviewRes {
    private int storeId;
    private float avgScore;
    private int reviewCount;
    private List<ReviewMenus> storeReviews;
}
