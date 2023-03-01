package com.example.demo.src.user.model;

import com.example.demo.src.user.model.DTO.UserPayments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserPaymentRes {
    private int userCouPayMoney;
    private List<UserPayments> cardList;
    private List<UserPayments> accountList;
}
