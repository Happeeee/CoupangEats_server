package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetMyEatsRes {
    private String userName;
    private String phoneNumber;
    private int userReviewCount;
    private int userHelpCount;
    private int userFavoriteCount;
}