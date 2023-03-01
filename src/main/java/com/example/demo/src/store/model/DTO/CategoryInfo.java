package com.example.demo.src.store.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInfo {
    private int optId;
    private String categoryName;
    private boolean necessary;
    private boolean multiple;
}
