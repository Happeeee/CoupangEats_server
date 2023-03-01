package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCouponRes {
    private int couponId;
    private int storeId;
    private String couponName;
    private String couponCode;
    private String couponType;
    private int minimumOrder;
    private String endDate;
    private int discount;

}
