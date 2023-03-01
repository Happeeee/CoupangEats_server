package com.example.demo.src.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewOrder {
    private String menuName;

    private int menuPrice;
    private int count;
    private List<OptPrice> optList;


}
