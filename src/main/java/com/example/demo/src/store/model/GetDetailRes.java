package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDetailRes {
    private int menuId;
    private String menuImg;
    private String menuName;
    private String menuDescription;
    private int price;
}
