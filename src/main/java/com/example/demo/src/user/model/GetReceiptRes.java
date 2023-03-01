package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetReceiptRes {
    private String storeName;
    private int orderId;
    private String createdAt;
    private int totalPrice;
    private int deliveryFee;
    private String address;
    private String bankName;
    private String payNumber;
    private String bankImgUrl;




}
