package com.example.demo.src.store;

import com.example.demo.src.store.model.*;
import com.example.demo.src.store.model.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetStoreNoLoginRes> searchStoreByCategoryAboutPropertyNoLogin(String conditionLine, double userLatitude, double userLongitude) {
        String searchStoreByCategoryAboutPropertyNoLoginQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude)\n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId\n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select storeId, openTime, closeTime,\n" +
                "                      case when closeTime <= openTime then\n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) as SOP on Stores.storeId = SOP.storeId\n" +
                conditionLine + "\n" +
                "having userDistance <= (20);";
        Object[] searchStoreByCategoryAboutPropertyNoLoginParams = new Object[] {userLatitude, userLongitude, userLatitude};

        return this.jdbcTemplate.query(searchStoreByCategoryAboutPropertyNoLoginQuery,
                (rs,rowNum) -> new GetStoreNoLoginRes(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getString("image1"),
                        rs.getString("image2"),
                        rs.getString("image3"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                searchStoreByCategoryAboutPropertyNoLoginParams);
    }

    public List<GetStoreNoLoginRes> searchStoreByCategoryAboutMenuNoLogin(int categoryId, double userLatitude, double userLongitude) {
        String searchStoreByCategoryAboutMenuNoLoginQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude)\n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId\n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select storeId, openTime, closeTime,\n" +
                "                      case when closeTime <= openTime then\n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) as SOP on Stores.storeId = SOP.storeId\n" +
                "where Stores.categoryId = ?\n" +
                "having userDistance <= (20);";
        Object[] searchStoreByCategoryAboutMenuNoLoginParams = new Object[] {userLatitude, userLongitude, userLatitude, categoryId};

        return this.jdbcTemplate.query(searchStoreByCategoryAboutMenuNoLoginQuery,
                (rs,rowNum) -> new GetStoreNoLoginRes(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getString("image1"),
                        rs.getString("image2"),
                        rs.getString("image3"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                searchStoreByCategoryAboutMenuNoLoginParams);
    }

    public List<GetStoreRes> searchStoreByCategoryAboutProperty(int userId, String conditionLine, double userLatitude, double userLongitude) {
        String searchStoreByCategoryAboutPropertyQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3, F.status as isUserFavorite,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude)\n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select FavoriteId, userId, storeId, status from Favorites where userId = ?) F on Stores.storeId = F.storeId\n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId\n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select storeId, openTime, closeTime,\n" +
                "                      case when closeTime <= openTime then\n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) as SOP on Stores.storeId = SOP.storeId\n" +
                conditionLine + "\n" +
                "having userDistance <= (20);";
        Object[] searchStoreByCategoryAboutPropertyParams = new Object[] {userLatitude, userLongitude, userLatitude, userId};

        return this.jdbcTemplate.query(searchStoreByCategoryAboutPropertyQuery,
                (rs,rowNum) -> new GetStoreRes(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getBoolean("isUserFavorite"),
                        rs.getString("image1"),
                        rs.getString("image2"),
                        rs.getString("image3"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                searchStoreByCategoryAboutPropertyParams);
    }

    public List<GetStoreRes> searchStoreByCategoryAboutMenu(int userId, int categoryId, double userLatitude, double userLongitude) {
        String searchStoreByCategoryAboutMenuQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3, F.status as isUserFavorite,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude)\n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select FavoriteId, userId, storeId, status from Favorites where userId = ?) F on Stores.storeId = F.storeId\n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId\n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select storeId, openTime, closeTime,\n" +
                "                      case when closeTime <= openTime then\n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) as SOP on Stores.storeId = SOP.storeId\n" +
                "where Stores.categoryId = ?\n" +
                "having userDistance <= (20);";
        Object[] searchStoreByCategoryAboutMenuParams = new Object[] {userLatitude, userLongitude, userLatitude, userId, categoryId};

        return this.jdbcTemplate.query(searchStoreByCategoryAboutMenuQuery,
                (rs,rowNum) -> new GetStoreRes(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getBoolean("isUserFavorite"),
                        rs.getString("image1"),
                        rs.getString("image2"),
                        rs.getString("image3"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                searchStoreByCategoryAboutMenuParams);
    }

    public List<GetStoreRes> getChooseStores(int userId, double userLatitude, double userLongitude) {
        String getChooseStoresQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3, F.status as isUserFavorite,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude) \n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select FavoriteId, userId, storeId, status from Favorites where userId = ?) F on Stores.storeId = F.storeId \n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId \n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select storeId, openTime, closeTime,\n" +
                "                      case when closeTime <= openTime then \n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) as SOP on Stores.storeId = SOP.storeId\n" +
                "having userDistance <= (20);";
        Object[] getChooseStoresParams = new Object[] {userLatitude, userLongitude, userLatitude, userId};

        return this.jdbcTemplate.query(getChooseStoresQuery,
                (rs,rowNum) -> new GetStoreRes(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getBoolean("isUserFavorite"),
                        rs.getString("image1"),
                        rs.getString("image2"),
                        rs.getString("image3"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                getChooseStoresParams);
    }

    public List<GetStoreNoLoginRes> getChooseStoresNoLogin(double userLatitude, double userLongitude) {
        String getChooseStoresQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude)\n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId\n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select storeId, openTime, closeTime,\n" +
                "                      case when closeTime <= openTime then\n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) as SOP on Stores.storeId = SOP.storeId\n" +
                "having userDistance <= (20);";
        Object[] getChooseStoresParams = new Object[] {userLatitude, userLongitude, userLatitude};

        return this.jdbcTemplate.query(getChooseStoresQuery,
                (rs,rowNum) -> new GetStoreNoLoginRes(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getString("image1"),
                        rs.getString("image2"),
                        rs.getString("image3"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                getChooseStoresParams);
    }

    public List<GetStoreRes> getEatsOriginalStores(int userId, double userLatitude, double userLongitude) {
        String getEatsOriginalStoresQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3, F.status as isUserFavorite,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude) \n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select FavoriteId, userId, storeId, status from Favorites where userId = ?) F on Stores.storeId = F.storeId \n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId \n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select storeId, openTime, closeTime,\n" +
                "                      case when closeTime <= openTime then \n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) as SOP on Stores.storeId = SOP.storeId\n" +
                "having userDistance <= (20) and original = 1;";
        Object[] getEatsOriginalStoresParams = new Object[] {userLatitude, userLongitude, userLatitude, userId};

        return this.jdbcTemplate.query(getEatsOriginalStoresQuery,
                (rs,rowNum) -> new GetStoreRes(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getBoolean("isUserFavorite"),
                        rs.getString("image1"),
                        rs.getString("image2"),
                        rs.getString("image3"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                getEatsOriginalStoresParams);
    }

    public List<GetStoreRes> getTownPopularStores(int userId, double userLatitude, double userLongitude) {
        String getTownPopularStoresQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3, F.status as isUserFavorite,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude)\n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, orderCount, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select FavoriteId, userId, storeId, status from Favorites where userId = ?) F on Stores.storeId = F.storeId \n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId\n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore \n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    left join (select storeId, count(orderId) as orderCount from Orders group by (storeId)) OC on Stores.storeId = OC.storeId\n" +
                "inner join (select storeId, openTime, closeTime,\n" +
                "                   case when closeTime <= openTime then\n" +
                "                        case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                            when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                    else\n" +
                "                        case when now() between openTime and closeTime then 'Open'\n" +
                "                        else 'Close' end\n" +
                "                    end as isOpen\n" +
                "            from StoreInfos) SOP on Stores.storeId = SOP.storeId\n" +
                "having userDistance <= (5) and orderCount >= 3\n" +
                "order by orderCount DESC\n" +
                "limit 20;";

        Object[] getTownPopularStoresParams = new Object[] {userLatitude, userLongitude, userLatitude, userId};

        return this.jdbcTemplate.query(getTownPopularStoresQuery,
                (rs,rowNum) -> new GetStoreRes(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getBoolean("isUserFavorite"),
                        rs.getString("image1"),
                        rs.getString("image2"),
                        rs.getString("image3"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                getTownPopularStoresParams);
    }

    public List<GetStoreRes> getUserFrequentOrderStores(int userId, double userLatitude, double userLongitude) {
        String getUserFrequentOrderStoresQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3, F.status as isUserFavorite,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude)\n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, userOrderCount, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId\n" +
                "    left join (select FavoriteId, userId, storeId, status from Favorites where userId = ?) F on Stores.storeId = F.storeId\n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId\n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select userId, storeId, count(storeId) as userOrderCount from Orders\n" +
                "                group by userId, storeId\n" +
                "                having userId = ?) USC on Stores.storeId = USC.storeId\n" +
                "    inner join (select storeId, openTime, closeTime,\n" +
                "                       case when closeTime <= openTime then\n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) SOP on Stores.storeId = SOP.storeId\n" +
                "having userDistance <= (20) and userOrderCount >= 1\n" +
                "order by userOrderCount DESC\n" +
                "limit 20;";

        Object[] getUserFrequentOrderStoresParams = new Object[] {userLatitude, userLongitude, userLatitude, userId, userId};

        return this.jdbcTemplate.query(getUserFrequentOrderStoresQuery,
                (rs,rowNum) -> new GetStoreRes(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getBoolean("isUserFavorite"),
                        rs.getString("image1"),
                        rs.getString("image2"),
                        rs.getString("image3"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                getUserFrequentOrderStoresParams);
    }

    public List<UserFavorites> getUserFavorites(int userId, double userLatitude, double userLongitude) {
        String getUserFavoritesQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, F.status as isUserFavorite,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude)\n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, userOrderCount, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId\n" +
                "    left join (select FavoriteId, userId, storeId, status from Favorites where userId = ?) F on Stores.storeId = F.storeId\n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId\n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    left join(select userId, storeId, count(storeId) as userOrderCount from Orders\n" +
                "                group by userId, storeId\n" +
                "                having userId = 1) USC on Stores.storeId = USC.storeId\n" +
                "    inner join (select storeId, openTime, closeTime,\n" +
                "                       case when closeTime <= openTime then\n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) SOP on Stores.storeId = SOP.storeId\n" +
                "having isUserFavorite = 1\n" +
                "order by userOrderCount DESC";
        Object[] getUserFavoritesParams = new Object[] {userLatitude, userLongitude, userLatitude, userId};

        return this.jdbcTemplate.query(getUserFavoritesQuery,
                (rs,rowNum) -> new UserFavorites(
                        rs.getInt("storeId"),
                        rs.getString("name"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryTime"),
                        rs.getFloat("userDistance"),
                        rs.getInt("userOrderCount"),
                        rs.getBoolean("isUserFavorite"),
                        rs.getString("image1"),
                        new GetStoreRes.MaxCouponInfos(
                                rs.getInt("maxCouponPrice"),
                                rs.getString("maxCouponName"),
                                rs.getString("maxCouponType")
                        ),
                        new GetStoreRes.BusinessHourInfos(
                                rs.getString("isOpen"),
                                rs.getString("openTime"),
                                rs.getString("closeTime")
                        ),
                        new GetStoreRes.Flags(
                                rs.getBoolean("cheetah"),
                                rs.getBoolean("blue"),
                                rs.getBoolean("original"),
                                rs.getBoolean("takeOut"),
                                rs.getBoolean("delivery"),
                                rs.getBoolean("isNew"),
                                rs.getBoolean("drink")
                        )
                ),
                getUserFavoritesParams);
    }





    public UserCoordinate getUserCoordinate(int userAddressId) {
        String getUserCoordinateQuery = "select latitude, longitude from UserAddresses where addressId = ?;";
        int getUserCoordinateParams = userAddressId;
        return this.jdbcTemplate.queryForObject(getUserCoordinateQuery,
                (rs,rowNum) -> new UserCoordinate(
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                ), getUserCoordinateParams
        );
    }

    public List<GetStoreCategoryRes> getStoreCategories() {
        String getStoreCategoriesQuery = "select categoryName, categoryImg from StoreCategories;";

        return this.jdbcTemplate.query(getStoreCategoriesQuery,
                (rs,rowNum) -> new GetStoreCategoryRes(
                        rs.getString("categoryName"),
                        rs.getString("categoryImg")
                )
        );
    }

    public int isExistStore(int storeId) {
        String isExistStoreQuery = "select exists(select * from Stores where storeId = ?);";
        int isExistStoreParams = storeId;

        return this.jdbcTemplate.queryForObject(isExistStoreQuery, int.class, isExistStoreParams);
    }

    public StoreReviewScore getStoreReviewScore(int storeId) {
        String getStoreReviewScoreQuery = "select Stores.storeId, RC.reviewCount, SAVG.avgScore\n" +
                "from Stores\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) RC on Stores.storeId = RC.storeId\n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "where Stores.StoreId = ?\n" +
                ";";
        int getStoreReviewScoreParams = storeId;

        return this.jdbcTemplate.queryForObject(getStoreReviewScoreQuery,
                (rs,rowNum) -> new StoreReviewScore(
                        rs.getInt("storeId"),
                        rs.getFloat("avgScore"),
                        rs.getInt("reviewCount")
                ) ,getStoreReviewScoreParams
        );
    }

    public List<ReviewOnly> getOnlyReviews(int storeId) {
        String getOnlyReviewsQuery = "select reviewId, Reviews.orderId, name as userName, score, reviewImage, reviewContent, Reviews.createdAt\n" +
                "from Reviews\n" +
                "    inner join Users U on Reviews.userId = U.userId\n" +
                "    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "where O.storeId = ?;";
        int getOnlyReviewsParams = storeId;

        return this.jdbcTemplate.query(getOnlyReviewsQuery,
                (rs,rowNum) -> new ReviewOnly(
                        rs.getInt("reviewId"),
                        rs.getInt("orderId"),
                        rs.getString("userName"),
                        rs.getFloat("score"),
                        rs.getString("reviewContent"),
                        rs.getString("reviewImage"),
                        rs.getString("createdAt")
                ) ,getOnlyReviewsParams
        );
    }

    public List<MenuOnly> getOnlyMenus(int orderId) {
        String getOnlyMenusQuery = "select Orders.orderId, menuName\n" +
                "from Orders\n" +
                "    inner join OrderLists OL on Orders.orderId = OL.orderId\n" +
                "    inner join Menus M on OL.menuId = M.menuId\n" +
                "where OL.orderId = ?;";
        int getOnlyMenusParams = orderId;

        return this.jdbcTemplate.query(getOnlyMenusQuery,
                (rs,rowNum) -> new MenuOnly(
                        rs.getString("menuName")
                ) ,getOnlyMenusParams
        );
    }

    public UserCoordinate getUserCoordinate_(int userId) {
        String getUserCoordinateQuery = "select min(addressId),latitude,longitude from UserAddresses where userId = ?";
        int getUserCoordinateParams = userId;

        return this.jdbcTemplate.queryForObject(getUserCoordinateQuery,
                (rs, rowNum) -> new UserCoordinate(
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                ), getUserCoordinateParams
        );
    }




    public GetStoreDetailRes getStoreDetail(int storeId, int userId) {
        String getStoreDetailQuery = "select name,\n" +
                "       Stores.storeId,\n" +
                "       image1,\n" +
                "       image2,\n" +
                "       image3,\n" +
                "       deliveryFee,\n" +
                "       RC.reviewCount AS reviewCount,\n" +
                "       SAVG.avgScore AS score,\n" +
                "       Stores.minimumOrder,\n" +
                "       deliveryTime,\n" +
                "       cheetah,\n" +
                "       original,\n" +
                "       blue,\n" +
                "       takeOut,\n" +
                "       delivery,\n" +
                "       isNew,\n" +
                "       drink,\n" +
                "       latitude,\n" +
                "       longitude,\n" +
                "\n" +
                "       round((6371 * acos(cos(radians(?)) * cos(radians(SI.latitude)) * cos(radians(SI.longitude)\n" +
                "           - radians(?)) + sin(radians(?)) * sin(radians(SI.latitude)))), 1)\n" +
                "           as userDistance\n" +
                "from Stores\n" +
                "         left join (select latitude, longitude, storeId from StoreInfos) SI on SI.storeId = Stores.storeId\n" +
                "         left join (select O.storeId, count(reviewId) as reviewCount\n" +
                "                    from Reviews\n" +
                "                             inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    group by O.storeId) RC on Stores.storeId = RC.storeId\n" +
                "         left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                    from Reviews\n" +
                "                             inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                             inner join Stores S on O.storeId = S.storeId\n" +
                "                    group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "where Stores.storeId = ?;";

        UserCoordinate userCoordinate = getUserCoordinate_(userId);


        int getStoreDetailById = storeId;
        GetStoreDetailRes getStoreDetailRes = new GetStoreDetailRes();
        StoreInfo storeInfo = this.jdbcTemplate.queryForObject(getStoreDetailQuery, (rs, rowNum) -> new StoreInfo(
                rs.getInt("storeId"),
                rs.getString("name"),
                rs.getString("image1"),
                rs.getString("image2"),
                rs.getString("image3"),
                rs.getFloat("score"),
                rs.getInt("reviewCount"),
                rs.getInt("deliveryFee"),
                rs.getInt("minimumOrder"),
                rs.getString("deliveryTime"),
                rs.getBoolean("cheetah"),
                rs.getBoolean("original"),
                rs.getBoolean("blue"),
                rs.getBoolean("takeOut"),
                rs.getBoolean("delivery"),
                rs.getBoolean("isNew"),
                rs.getBoolean("drink"),
                rs.getFloat("latitude"),
                rs.getFloat("longitude"),
                rs.getFloat("userDistance")
        ), userCoordinate.getUserLatitude(), userCoordinate.getUserLongitude(), userCoordinate.getUserLatitude(), getStoreDetailById);

        getStoreDetailRes.setStoreInfo(storeInfo);
        String getStoreCategory = "select categoryId,categoryName from MenuCategories where MenuCategories.storeId = ?";

        List<MenuCategory> categoryLists = this.jdbcTemplate.query(getStoreCategory, (rs, rowNum) -> new MenuCategory(
                rs.getInt("categoryId"),
                rs.getString("categoryName")
        ), getStoreDetailById);

        int size = categoryLists.size();

        getStoreDetailRes.setCategoryList(categoryLists);
        MenuCategory menuCategory = new MenuCategory();
        List<List<MenuList>> menuLists = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            menuCategory = categoryLists.get(i);
            int param = menuCategory.getCategoryId();

            String getMenuQuery = "select menuId,categoryId,menuName,menuPrice,description,image,soldOut,goodCount,orderMost,reviewMost from Menus where Menus.categoryId =?";
            List<MenuList> menuList = this.jdbcTemplate.query(getMenuQuery, (rs, rowNum) -> new MenuList(
                    rs.getInt("menuId"),
                    rs.getInt("categoryId"),
                    rs.getString("menuName"),
                    rs.getInt("menuPrice"),
                    rs.getString("description"),
                    rs.getString("image"),
                    rs.getBoolean("soldOut"),
                    rs.getInt("goodCount"),
                    rs.getBoolean("orderMost"),
                    rs.getBoolean("reviewMost")
            ), param);
            menuLists.add(menuList);
        }
        getStoreDetailRes.setMenuList(menuLists);
        return getStoreDetailRes;
    }

    public GetStoreDetailRes getStoreDetail(int storeId, UserCoordinate userCoordinate) {
        String getStoreDetailQuery = "select name,\n" +
                "       Stores.storeId,\n" +
                "       image1,\n" +
                "       image2,\n" +
                "       image3,\n" +
                "       deliveryFee,\n" +
                "       RC.reviewCount AS reviewCount,\n" +
                "       SAVG.avgScore AS score,\n" +
                "       Stores.minimumOrder,\n" +
                "       deliveryTime,\n" +
                "       cheetah,\n" +
                "       original,\n" +
                "       blue,\n" +
                "       takeOut,\n" +
                "       delivery,\n" +
                "       isNew,\n" +
                "       drink,\n" +
                "       latitude,\n" +
                "       longitude,\n" +
                "\n" +
                "       round((6371 * acos(cos(radians(?)) * cos(radians(SI.latitude)) * cos(radians(SI.longitude)\n" +
                "           - radians(?)) + sin(radians(?)) * sin(radians(SI.latitude)))), 1)\n" +
                "           as userDistance\n" +
                "from Stores\n" +
                "         left join (select latitude, longitude, storeId from StoreInfos) SI on SI.storeId = Stores.storeId\n" +
                "         left join (select O.storeId, count(reviewId) as reviewCount\n" +
                "                    from Reviews\n" +
                "                             inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    group by O.storeId) RC on Stores.storeId = RC.storeId\n" +
                "         left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                    from Reviews\n" +
                "                             inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                             inner join Stores S on O.storeId = S.storeId\n" +
                "                    group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "where Stores.storeId = ?;";


        int getStoreDetailById = storeId;
        GetStoreDetailRes getStoreDetailRes = new GetStoreDetailRes();
        StoreInfo storeInfo = this.jdbcTemplate.queryForObject(getStoreDetailQuery, (rs, rowNum) -> new StoreInfo(
                rs.getInt("storeId"),
                rs.getString("name"),
                rs.getString("image1"),
                rs.getString("image2"),
                rs.getString("image3"),
                rs.getFloat("score"),
                rs.getInt("reviewCount"),
                rs.getInt("deliveryFee"),
                rs.getInt("minimumOrder"),
                rs.getString("deliveryTime"),
                rs.getBoolean("cheetah"),
                rs.getBoolean("original"),
                rs.getBoolean("blue"),
                rs.getBoolean("takeOut"),
                rs.getBoolean("delivery"),
                rs.getBoolean("isNew"),
                rs.getBoolean("drink"),
                rs.getFloat("latitude"),
                rs.getFloat("longitude"),
                rs.getFloat("userDistance")
        ), userCoordinate.getUserLatitude(), userCoordinate.getUserLongitude(), userCoordinate.getUserLatitude(), getStoreDetailById);

        getStoreDetailRes.setStoreInfo(storeInfo);
        String getStoreCategory = "select categoryId,categoryName from MenuCategories where MenuCategories.storeId = ?";

        List<MenuCategory> categoryLists = this.jdbcTemplate.query(getStoreCategory, (rs, rowNum) -> new MenuCategory(
                rs.getInt("categoryId"),
                rs.getString("categoryName")
        ), getStoreDetailById);

        int size = categoryLists.size();

        getStoreDetailRes.setCategoryList(categoryLists);
        MenuCategory menuCategory = new MenuCategory();
        List<List<MenuList>> menuLists = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            menuCategory = categoryLists.get(i);
            int param = menuCategory.getCategoryId();

            String getMenuQuery = "select menuId,categoryId,menuName,menuPrice,description,image,soldOut,goodCount,orderMost,reviewMost from Menus where Menus.categoryId =?";
            List<MenuList> menuList = this.jdbcTemplate.query(getMenuQuery, (rs, rowNum) -> new MenuList(
                    rs.getInt("menuId"),
                    rs.getInt("categoryId"),
                    rs.getString("menuName"),
                    rs.getInt("menuPrice"),
                    rs.getString("description"),
                    rs.getString("image"),
                    rs.getBoolean("soldOut"),
                    rs.getInt("goodCount"),
                    rs.getBoolean("orderMost"),
                    rs.getBoolean("reviewMost")
            ), param);
            menuLists.add(menuList);
        }
        getStoreDetailRes.setMenuList(menuLists);
        return getStoreDetailRes;
    }


    public GetStoreInfo getStoreInfo(int storeId) {
        String getStoreInfoQuery = "select number,ceo,businessNumber,businessName,openTime,closeTime,introduction,originInfo,latitude,longitude from StoreInfos where StoreInfos.storeId = ?";
        int getStoreInfoByIdParam = storeId;

        GetStoreInfo getStoreInfo = this.jdbcTemplate.queryForObject(getStoreInfoQuery, (rs, rowNum) -> new GetStoreInfo(
                rs.getString("number"),
                rs.getString("ceo"),
                rs.getString("businessNumber"),
                rs.getString("businessName"),
                rs.getString("openTime"),
                rs.getString("closeTime"),
                rs.getString("introduction"),
                rs.getString("originInfo"),
                rs.getFloat("latitude"),
                rs.getFloat("longitude")
        ), getStoreInfoByIdParam);

        return getStoreInfo;

    }

    public GetMenuRes getMenuDetail(int storeId, int menuId) {
        GetMenuRes getMenuRes = new GetMenuRes();

        String getQueryMenuDetail = "select menuId,image,menuName,menuPrice,description from Menus where Menus.storeId=? and Menus.menuId=? ";
        int getMenuDetailByStoreId = storeId;
        int getMenuDetailByMenuId = menuId;

        GetDetailRes getDetailRes = this.jdbcTemplate.queryForObject(getQueryMenuDetail, (rs, rowNum) -> new GetDetailRes(
                rs.getInt("menuId"),
                rs.getString("image"),
                rs.getString("menuName"),
                rs.getString("description"),
                rs.getInt("menuPrice")

        ), storeId, menuId);

        getMenuRes.setDetail(getDetailRes);

        String getQueryMenuOptCate = "select menuOptionId,optionName,necessary,multiple from MenuOptionsCategories where MenuOptionsCategories.menuId=? ";

        List<CategoryInfo> categoryInfoList = this.jdbcTemplate.query(getQueryMenuOptCate, (rs, rowNum) -> new CategoryInfo(
                rs.getInt("menuOptionId"),
                rs.getString("optionName"),
                rs.getBoolean("necessary"),
                rs.getBoolean("multiple")
        ), menuId);


        int cnt = categoryInfoList.size();
        List<GetMenuCategories> getMenuCategoriesList = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            CategoryInfo opt = categoryInfoList.get(i);
            int optId = opt.getOptId();
            GetMenuCategories getMenuCategories = new GetMenuCategories();
            getMenuCategories.setOptId(opt.getOptId());
            getMenuCategories.setCategoryName(opt.getCategoryName());
            getMenuCategories.setNecessary(opt.isNecessary());
            getMenuCategories.setMultiple(opt.isMultiple());
            String getQueryMenuOpt = "select OptionCategoryId,optionName,optionPrice,menuOptionId from MenuOptions where MenuOptions.OptionCategoryId = ?";
            List<GetMenuOpt> getMenuOpts = this.jdbcTemplate.query(getQueryMenuOpt, (rs, rowNum) -> new GetMenuOpt(
                    rs.getInt("OptionCategoryId"),
                    rs.getString("optionName"),
                    rs.getInt("menuOptionId"),
                    rs.getInt("optionPrice")
            ), optId);
            getMenuCategories.setOptList(getMenuOpts);
            getMenuCategoriesList.add(getMenuCategories);
        }

        getMenuRes.setMenulist(getMenuCategoriesList);

        return getMenuRes;

    }

    //insert into Orders(userId,storeId, disposable,storeRequest,riderReqeust,payments) values (1,2,0,'맛있게 해주세요','빨리 와주세요','CREDIT')
// insert into OrderLists(orderId,menuId,count) values (1,3,3)
// insert into OptionLists(menuid, optionid) values (3,3)
    public PostOrderRes postOrder(PostOrderReq postOrderReq, int storeId, int userId) {

        String postOrderQuery = "insert into Orders(userId,storeId, disposable,storeRequest,riderReqeust,payments,paymentId,addressId,totalPrice) values (?,?,?,?,?,?,?,?,?)";
        String postOrderOptCateQuery = "insert into OrderLists(orderId,menuId,optionList,count,price) values (?,?,?,?,?)";

        int menuPrice = 0;
        int totalPrice = 0;
        int cnt = postOrderReq.getMenuList().size();
        int[] price = new int[cnt];
        for (int i = 0; i < cnt; i++) {
            menuPrice = 0;
            List<menuNOption> mNo = postOrderReq.getMenuList();
            menuNOption mNo_ = mNo.get(i);

            String optStr = mNo_.getOptList();
            String[] optList = optStr.split(",");
            menuPrice += this.jdbcTemplate.queryForObject("select menuPrice from Menus where menuId=?", int.class, mNo_.getMenuId());
            for (String opt : optList) {
                menuPrice += this.jdbcTemplate.queryForObject("select optionPrice from MenuOptions where MenuOptions.menuOptionId = ?", int.class, opt);
            }
            menuPrice *= mNo_.getCount();
            price[i] = menuPrice;
            totalPrice += menuPrice;
        }

        int fee = this.jdbcTemplate.queryForObject("select deliveryFee from Stores where storeId =?;", int.class, storeId);


        this.jdbcTemplate.update(postOrderQuery, userId, storeId, postOrderReq.isDisposable(), postOrderReq.getStoreRequest(), postOrderReq.getRiderRequest(), postOrderReq.getPayments(), postOrderReq.getPaymentId(), postOrderReq.getAddressId(), totalPrice + fee);

        String lastInsertIdQuery = "select last_insert_id()";
        int id = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);

        String getOrderInfo = "select orderId,totalPrice from Orders where orderId=?";

        PostOrderRes postOrderRes = this.jdbcTemplate.queryForObject(getOrderInfo, (rs, rowNum) -> new PostOrderRes(
                rs.getInt("orderId")

        ), id);


        for (int i = 0; i < cnt; i++) {
            List<menuNOption> mNo = postOrderReq.getMenuList();
            menuNOption mNo_ = mNo.get(i);

            this.jdbcTemplate.update(postOrderOptCateQuery, id, mNo_.getMenuId(), mNo_.getOptList(), mNo_.getCount(), price[i]);

        }
        return postOrderRes;
    }

    public String checkPasswd(int userId) {

        String getPassword = "select payPassword from Users where userId = ?";
        int getPasswordParam = userId;

        return this.jdbcTemplate.queryForObject(getPassword, String.class, getPasswordParam);


    }
//
//    public GetOrderInfo getOrderInfo(int userId) {
//        StringBuilder sb = new StringBuilder();
//        GetOrderInfo getOrderInfo = new GetOrderInfo();
//
//        String getOrderInfoQuery = "select orderId,\n" +
//                "       name,\n" +
//                "       storeId,\n" +
//                "       disposable,\n" +
//                "       Orders.createdAt AS createdAt,\n" +
//                "       storeRequest,\n" +
//                "       riderReqeust AS riderRequest,\n" +
//                "       payments\n" +
//                "from Orders\n" +
//                "         inner join Users U on Orders.userId = U.userId\n" +
//                "where U.userId  = ?;";
//
//        int getOrderInfoByUserIdParam = userId;
//        OrderInfo orderInfo = this.jdbcTemplate.queryForObject(getOrderInfoQuery, (rs, rowNum) -> new OrderInfo(
//                rs.getInt("orderId"),
//                rs.getString("name"),
//                rs.getInt("storeId"),
//                rs.getBoolean("disposable"),
//                rs.getString("createdAt"),
//                rs.getString("storeRequest"),
//                rs.getString("riderRequest"),
//                rs.getString("payments")
//
//        ),getOrderInfoByUserIdParam);
//
//        getOrderInfo.setOrderInfo(orderInfo);
//
//
//
//        String getMenuNameQuery = "select menuName,optionList,count\n" +
//                "from OrderLists\n" +
//                "         inner join Menus M on OrderLists.menuId = M.menuId\n" +
//                "where OrderLists.orderId = ?;";
//
//        int orderId = orderInfo.getOrderId();
//
//        List<MenuNameNOpt> menuNameNOpts = this.jdbcTemplate.query(getMenuNameQuery, (rs, rowNum) -> new MenuNameNOpt(
//                rs.getString("menuName"),
//                rs.getString("optionList"),
//                rs.getInt("count")
//        ),orderId);
//
//        int cnt = menuNameNOpts.size();
//        List<MenuOpt> mnolist = new ArrayList<>();
//        String getOptNameQuery = "";
//        for(int i =0;i<cnt;i++){
//            MenuNameNOpt mNo = menuNameNOpts.get(i);
//            String opt = mNo.getOptList();
//            String[] opt_list = opt.split(",");
//            String opt_name= "";
//            int opt_cnt = opt_list.length;
//            for(int j=0;j<opt_cnt;j++) {
//                String optList = this.jdbcTemplate.queryForObject("select optionName from MenuOptions where menuOptionId = ?;", String.class,Integer.parseInt(opt_list[j]));
//                opt_name +=optList;
//                opt_name += ",";
//            }
//            mnolist.add(new MenuOpt(mNo.getMenuName(),opt_name));
//        }
//        getOrderInfo.setMnO(mnolist);
//
//        return getOrderInfo;
//
//
//    }
//
//    public List<GetOrderInfo> getOrdersInfo(int userId) {
//        List<GetOrderInfo> getOrdersInfo = new ArrayList<>();
//        GetOrderInfo getOrderInfo = new GetOrderInfo();
//
//        String getOrdersIdQuery = "select orderId from Orders O where O.userId = ?";
//        int getOrdersIdByUserIdParam = userId;
//
//        List<Integer> ordersId = this.jdbcTemplate.queryForList(getOrdersIdQuery,int.class,getOrdersIdByUserIdParam);
//
//        for (Integer orderIdx : ordersId) {
//
//
//            String getOrderInfoQuery = "select orderId,\n" +
//                    "       name,\n" +
//                    "       storeId,\n" +
//                    "       disposable,\n" +
//                    "       Orders.createdAt AS createdAt,\n" +
//                    "       storeRequest,\n" +
//                    "       riderReqeust AS riderRequest,\n" +
//                    "       payments\n" +
//                    "from Orders\n" +
//                    "         inner join Users U on Orders.userId = U.userId\n" +
//                    "where Orders.userId  = ?;";
//
//            int getOrderInfoByOrderIdParam = orderIdx;
//            OrderInfo orderInfo = this.jdbcTemplate.queryForObject(getOrderInfoQuery, (rs, rowNum) -> new OrderInfo(
//                    rs.getInt("orderId"),
//                    rs.getString("name"),
//                    rs.getInt("storeId"),
//                    rs.getBoolean("disposable"),
//                    rs.getString("createdAt"),
//                    rs.getString("storeRequest"),
//                    rs.getString("riderRequest"),
//                    rs.getString("payments")
//
//            ), getOrderInfoByOrderIdParam);
//
//            getOrderInfo.setOrderInfo(orderInfo);
//
//
//            String getMenuNameQuery = "select menuName,optionList,count\n" +
//                    "from OrderLists\n" +
//                    "         inner join Menus M on OrderLists.menuId = M.menuId\n" +
//                    "where OrderLists.orderId = ?;";
//
//            int orderId = orderInfo.getOrderId();
//
//            List<MenuNameNOpt> menuNameNOpts = this.jdbcTemplate.query(getMenuNameQuery, (rs, rowNum) -> new MenuNameNOpt(
//                    rs.getString("menuName"),
//                    rs.getString("optionList"),
//                    rs.getInt("count")
//            ), orderIdx);
//
//            int cnt = menuNameNOpts.size();
//            List<MenuOpt> mnolist = new ArrayList<>();
//            String getOptNameQuery = "";
//            for (int i = 0; i < cnt; i++) {
//                MenuNameNOpt mNo = menuNameNOpts.get(i);
//                String opt = mNo.getOptList();
//                String[] opt_list = opt.split(",");
//                String opt_name = "";
//                int opt_cnt = opt_list.length;
//                for (int j = 0; j < opt_cnt; j++) {
//                    String optList = this.jdbcTemplate.queryForObject("select optionName from MenuOptions where menuOptionId = ?;", String.class, Integer.parseInt(opt_list[j]));
//                    opt_name += optList;
//                    opt_name += ",";
//                }
//                mnolist.add(new MenuOpt(mNo.getMenuName(), opt_name));
//            }
//            getOrderInfo.setMnO(mnolist);
//
//        getOrdersInfo.add(getOrderInfo);
//        }
//
//        return getOrdersInfo;
//
//    }

    public List<Integer> getSearchInfo(String keyword) {
        String getSearchQuery = "select s.storeId\n" +
                "from Stores s\n" +
                "         inner join (select storeId, group_concat(menuName separator ',') AS menus from Menus group by storeId) m on m.storeId = s.storeId\n" +
                " where (s.name like '%" + keyword + "%' OR m.menus like '%" + keyword + "%')";

        List<Integer> storeNameList = this.jdbcTemplate.queryForList(getSearchQuery, Integer.class);

        return storeNameList;
    }


    public GetSearchResults getSearchResults(List<Integer> storeList, int userId, double userLatitude, double userLongitude) {
        int cnt = storeList.size();

        GetSearchResults getSearchResults = new GetSearchResults();

        String getChooseStoresQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3, F.status as isUserFavorite,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude) \n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select FavoriteId, userId, storeId, status from Favorites where userId = ?) F on Stores.storeId = F.storeId \n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId \n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select storeId, openTime, closeTime,\n" +
                "                      case when closeTime <= openTime then \n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) as SOP on Stores.storeId = SOP.storeId\n" +
                "where Stores.storeId = ?\n" +
                "having userDistance <= (20)";


        List<List<GetStoreRes>> getStoreList = new ArrayList<>();
        for (Integer storeId : storeList) {
            Object[] getChooseStoresParams = new Object[]{userLatitude, userLongitude, userLatitude, userId, storeId};

            getStoreList.add(this.jdbcTemplate.query(getChooseStoresQuery,
                    (rs, rowNum) -> new GetStoreRes(
                            rs.getInt("storeId"),
                            rs.getString("name"),
                            rs.getFloat("avgScore"),
                            rs.getInt("reviewCount"),
                            rs.getInt("deliveryFee"),
                            rs.getString("deliveryTime"),
                            rs.getFloat("userDistance"),
                            rs.getBoolean("isUserFavorite"),
                            rs.getString("image1"),
                            rs.getString("image2"),
                            rs.getString("image3"),
                            new GetStoreRes.MaxCouponInfos(
                                    rs.getInt("maxCouponPrice"),
                                    rs.getString("maxCouponName"),
                                    rs.getString("maxCouponType")
                            ),
                            new GetStoreRes.BusinessHourInfos(
                                    rs.getString("isOpen"),
                                    rs.getString("openTime"),
                                    rs.getString("closeTime")
                            ),
                            new GetStoreRes.Flags(
                                    rs.getBoolean("cheetah"),
                                    rs.getBoolean("blue"),
                                    rs.getBoolean("original"),
                                    rs.getBoolean("takeOut"),
                                    rs.getBoolean("delivery"),
                                    rs.getBoolean("isNew"),
                                    rs.getBoolean("drink")
                            )
                    ),
                    getChooseStoresParams));
        }

        getSearchResults.setStoreList(getStoreList);

        return getSearchResults;


    }

    public GetSearchResults getSearchResults_noJWT(List<Integer> storeList, double userLatitude, double userLongitude) {
        int cnt = storeList.size();
        int userId = 1;
        GetSearchResults getSearchResults = new GetSearchResults();

        String getChooseStoresQuery = "SELECT Stores.storeId, name, avgScore, R.reviewCount, deliveryFee, deliveryTime, image1, image2, image3, F.status as isUserFavorite,\n" +
                "\n" +
                "    round((6371*acos(cos(radians(?))*cos(radians(SI.latitude))*cos(radians(SI.longitude) \n" +
                "    -radians(?))+sin(radians(?))*sin(radians(SI.latitude)))), 1)\n" +
                "    as userDistance,\n" +
                "    isOpen, SOP.openTime, SOP.closeTime, SMC.maxCouponPrice, couponName as maxCouponName, couponType as maxCouponType, cheetah, blue, original, takeOut, delivery, isNew, drink\n" +
                "\n" +
                "from Stores\n" +
                "    inner join StoreInfos SI on Stores.storeId = SI.storeId\n" +
                "    left join (select O.storeId, count(reviewId) as reviewCount from Reviews inner join Orders O on Reviews.orderId = O.orderId group by O.storeId) R on Stores.storeId = R.storeId # 리뷰수\n" +
                "    left join (select FavoriteId, userId, storeId, status from Favorites where userId = ?) F on Stores.storeId = F.storeId \n" +
                "    left join (select Stores.storeId, CP.maxCouponPrice, C.couponName, C.couponType\n" +
                "                from Stores\n" +
                "                    inner join (select storeId, max(discountPrice) as maxCouponPrice\n" +
                "                        from Coupons\n" +
                "                        where endDate >= (select NOW())\n" +
                "                        group by storeId) CP on Stores.storeId = CP.storeId\n" +
                "                    inner join Coupons C on maxCouponPrice = C.discountPrice\n" +
                "                group by storeId) SMC on Stores.storeId = SMC.storeId \n" +
                "    left join (select O.storeId, round(avg(Reviews.score), 1) as avgScore\n" +
                "                from Reviews\n" +
                "                    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "                    inner join Stores S on O.storeId = S.storeId\n" +
                "                group by O.storeId) SAVG on Stores.storeId = SAVG.storeId\n" +
                "    inner join(select storeId, openTime, closeTime,\n" +
                "                      case when closeTime <= openTime then \n" +
                "                            case when now() between time('00:00:01') and closeTime then 'Open'\n" +
                "                                when now() between openTime and time('23:59:59') then 'Open'\n" +
                "                                else 'Close' end\n" +
                "                        else\n" +
                "                            case when now() between openTime and closeTime then 'Open'\n" +
                "                            else 'Close' end\n" +
                "                        end as isOpen\n" +
                "                from StoreInfos) as SOP on Stores.storeId = SOP.storeId\n" +
                "where Stores.storeId = ?\n" +
                "having userDistance <= (20)";


        List<List<GetStoreRes>> getStoreList = new ArrayList<>();
        for (Integer storeId : storeList) {
            Object[] getChooseStoresParams = new Object[]{userLatitude, userLongitude, userLatitude, storeId, userId};

            getStoreList.add(this.jdbcTemplate.query(getChooseStoresQuery,
                    (rs, rowNum) -> new GetStoreRes(
                            rs.getInt("storeId"),
                            rs.getString("name"),
                            rs.getFloat("avgScore"),
                            rs.getInt("reviewCount"),
                            rs.getInt("deliveryFee"),
                            rs.getString("deliveryTime"),
                            rs.getFloat("userDistance"),
                            rs.getBoolean("isUserFavorite"),
                            rs.getString("image1"),
                            rs.getString("image2"),
                            rs.getString("image3"),
                            new GetStoreRes.MaxCouponInfos(
                                    rs.getInt("maxCouponPrice"),
                                    rs.getString("maxCouponName"),
                                    rs.getString("maxCouponType")
                            ),
                            new GetStoreRes.BusinessHourInfos(
                                    rs.getString("isOpen"),
                                    rs.getString("openTime"),
                                    rs.getString("closeTime")
                            ),
                            new GetStoreRes.Flags(
                                    rs.getBoolean("cheetah"),
                                    rs.getBoolean("blue"),
                                    rs.getBoolean("original"),
                                    rs.getBoolean("takeOut"),
                                    rs.getBoolean("delivery"),
                                    rs.getBoolean("isNew"),
                                    rs.getBoolean("drink")
                            )
                    ),
                    getChooseStoresParams));
        }

        getSearchResults.setStoreList(getStoreList);

        return getSearchResults;


    }


    public List<GetCouponRes> getCouponRes(String storeId) {
        String getCouponQuery = "select couponId,\n" +
                "       storeId,\n" +
                "       couponName,\n" +
                "       couponCode,\n" +
                "       couponType,\n" +
                "       Coupons.minimumOrder,\n" +
                "       endDate,\n" +
                "       discountPrice\n" +
                "from Coupons\n" +
                "where Coupons.storeId = ? AND Coupons.endDate > now()";

        List<GetCouponRes> getCouponResList = this.jdbcTemplate.query(getCouponQuery, (rs, rowNum) -> new GetCouponRes(
                rs.getInt("couponId"),
                rs.getInt("storeId"),
                rs.getString("couponName"),
                rs.getString("couponCode"),
                rs.getString("couponType"),
                rs.getInt("minimumOrder"),
                rs.getString("endDate"),
                rs.getInt("discountPrice")
        ), storeId);

        return getCouponResList;

    }

    public boolean checkStoreStatus(int storeId) {
        String checkStatusQuery = "select status from Stores where storeId =?";
        return this.jdbcTemplate.queryForObject(checkStatusQuery, boolean.class, storeId);
    }

    public boolean getMenuStatus(int menuId) {
        String checkStatusQuery = "select status from Menus where menuId =?";
        return this.jdbcTemplate.queryForObject(checkStatusQuery, boolean.class, menuId);
    }

    public int checkMenuId(int menuId) {
        String checkEmailQuery = "select exists(select * from Menus where menuId = ?)";
        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, menuId);
    }

    public int checkStoreCoupon(String storeId) {
        String checkCoupon = "select exists(select * from Coupons where storeId = ? OR (storeId = ? AND Coupons.endDate > now()))";
        return this.jdbcTemplate.queryForObject(checkCoupon, int.class, storeId, storeId);
    }

    public void cancelOrder(int orderId) {
        String cancelOrder = "update Orders set status = false where orderId = ?";

        this.jdbcTemplate.update(cancelOrder, orderId);
    }

    public boolean checkOrder(int orderId) {
        String checkOrder = "select status from Orders where orderId = ?";

        return this.jdbcTemplate.queryForObject(checkOrder, boolean.class, orderId);

    }
    public int getUserIdByAddressId(int addressId) {
        String getUserIdByAddressIdQuery = "select userId from UserAddresses where addressId = ?;";
        int getUserIdByAddressIdParams = addressId;

        return this.jdbcTemplate.queryForObject(getUserIdByAddressIdQuery, int.class, getUserIdByAddressIdParams);
    }

    public int isExistAddress(int addressId) {
        String isExistAddressQuery = "select exists(select * from UserAddresses where addressId = ?);";
        int isExistAddressParams = addressId;

        return this.jdbcTemplate.queryForObject(isExistAddressQuery, int.class, isExistAddressParams);
    }
}
