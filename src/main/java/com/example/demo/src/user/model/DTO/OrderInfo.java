package com.example.demo.src.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    private int orderId;
    private String userName;
    private String storeName;
    private int totalPrice;
    private String address;
    private boolean disposable;
    private String createdAt;
    private String storeRequest;
    private String riderRequest;
    private String payment;
}
