package com.example.demo.src.store.model;

import com.example.demo.src.store.model.DTO.MenuCategory;
import com.example.demo.src.store.model.DTO.MenuList;
import com.example.demo.src.store.model.DTO.StoreInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreDetailRes {
    private StoreInfo storeInfo;
    private List<MenuCategory> categoryList;
    private List<List<MenuList>> menuList;

}
