package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMenuCategories {
    private int optId;
    private String categoryName;
    private boolean necessary;
    private boolean multiple;

    private List<GetMenuOpt> optList;


}
