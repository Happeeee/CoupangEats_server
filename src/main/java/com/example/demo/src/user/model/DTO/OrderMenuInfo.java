package com.example.demo.src.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuInfo {
    private String menuName;
    private int menuPrice;
    private int count;
    private String optString;
}
