package com.example.demo.src.store.model;

import com.example.demo.src.store.model.DTO.menuNOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderReq {
    private String password;
    private boolean disposable;
    private String storeRequest;
    private String riderRequest;
    private String payments;
    private int paymentId;
    private int addressId;
    private int totalPrice;


    private List<menuNOption> menuList;


}
