package com.example.demo.src.store.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreInfo {
    private int storeId;
    private String storeName;
    private String storeImg1;
    private String storeImg2;
    private String storeImg3;
    private float score;
    private int scoreCnt;
    private int fee;
    private int minimum;
    private String businessHour;
    private boolean cheetah;
    private boolean original;
    private boolean blue;
    private boolean takeOut;
    private boolean delivery;
    private boolean isNew;
    private boolean drink;
    private float latitude;
    private float longitude;
    private float distance;


}