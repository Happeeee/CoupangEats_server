package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreRes {
    private int storeId;
    private String storeName;
    private float avgScore;
    private int reviewCount;
    private int deliveryFee;
    private String deliveryTime;
    private float userDistance;
    private boolean userFavorite;
    private String image1;
    private String image2;
    private String image3;
    private MaxCouponInfos maxCouponInfos = new MaxCouponInfos();
    private BusinessHourInfos businessHourInfos = new BusinessHourInfos();
    private Flags flags = new Flags();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Flags {
        private boolean cheetah;
        private boolean blueRibbon;
        private boolean eatsOriginal;
        private boolean canTakeOut;
        private boolean canDelivery;
        private boolean newStore;
        private boolean sellingDrink;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessHourInfos {
        private String nowOpen;
        private String openTime;
        private String closeTime;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaxCouponInfos {
        private int maxCouponPrice;
        private String maxCouponName;
        private String maxCouponType;
    }


}
