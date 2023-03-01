package com.example.demo.src.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPayments {
    private int paymentId;
    private String type;
    private String bankName;
    private String number;
    private String bankLogoImage;
}
