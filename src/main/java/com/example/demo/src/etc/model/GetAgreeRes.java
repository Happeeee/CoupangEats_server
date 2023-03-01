package com.example.demo.src.etc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAgreeRes {
    private int agreeId;
    private String agreeType;
    private String agreeContent;
}
