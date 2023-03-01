package com.example.demo.src.store.model;

import com.example.demo.src.store.model.GetStoreRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreNoLoginRes {
    private int storeId;
    private String storeName;
    private float avgScore;
    private int reviewCount;
    private int deliveryFee;
    private String deliveryTime;
    private float userDistance;
    //private boolean userFavorite;
    private String image1;
    private String image2;
    private String image3;
    private GetStoreRes.MaxCouponInfos maxCouponInfos = new GetStoreRes.MaxCouponInfos();
    private GetStoreRes.BusinessHourInfos businessHourInfos = new GetStoreRes.BusinessHourInfos();
    private GetStoreRes.Flags flags = new GetStoreRes.Flags();

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