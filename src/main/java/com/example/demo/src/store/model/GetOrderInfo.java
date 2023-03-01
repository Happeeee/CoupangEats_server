package com.example.demo.src.store.model;

import com.example.demo.src.store.model.DTO.MenuOpt;
import com.example.demo.src.user.model.DTO.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderInfo {
    private OrderInfo orderInfo;
    private List<MenuOpt> MnO;

}
