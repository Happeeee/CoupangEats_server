package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCouponRes {
    private String name;
    private String couponName;
    private String couponType;
    private String endDate;
    private int minimumOrder;
    private int discountPrice;
    private String status;

}