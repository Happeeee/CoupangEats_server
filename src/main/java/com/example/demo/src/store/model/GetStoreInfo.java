package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreInfo {
    private String number;
    private String ceo;
    private String businessNumber;
    private String businessName;
    private String openTime;
    private String closeTime;
    private String introduction;
    private String originInfo;
    private float latitude;
    private float longitude;

}
