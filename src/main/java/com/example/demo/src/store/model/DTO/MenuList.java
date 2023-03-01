package com.example.demo.src.store.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuList {
    private int menuId;
    private int categoryId;
    private String menuName;
    private int price;
    private String description;
    private String img;
    private boolean soldOut;
    private int goodCount;
    private boolean orderMost;
    private boolean reviewMost;

}
