package com.example.demo.src.user;


import com.example.demo.src.store.model.GetOrderInfo;
import com.example.demo.src.user.model.DTO.*;
import com.example.demo.src.user.model.GetReceiptRes;

import com.example.demo.src.user.model.*;
import com.example.demo.src.store.model.DTO.MenuOpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public int createUser(PostUserReq postUserReq) {

        String createUserQuery = "insert into Users (name, phoneNumber,password, email,agree,isGoogle,isKakao,payPassword,couPayMoney,createdAt,updatedAt,status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] createUserParams = new Object[]{postUserReq.getUsername(), postUserReq.getPhoneNumber(), postUserReq.getPassword(), postUserReq.getEmail(),postUserReq.isAgree(),0,0,"111111",0,20230131,20230131,1};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public List<UserPayments> getUserPayments(int userId) {
        String getPaymentManageQuery = "select paymentId, type, number, bankName, bankLogoImage from UserPayments\n" +
                "inner join Banks B on UserPayments.bankId = B.bankId\n" +
                "where userId = ?;";
        int getPaymentManageParams = userId;

        return this.jdbcTemplate.query(getPaymentManageQuery,
                (rs,rowNum)-> new UserPayments(
                        rs.getInt("paymentId"),
                        rs.getString("type"),
                        rs.getString("bankName"),
                        rs.getString("number"),
                        rs.getString("bankLogoImage")
                ),
                getPaymentManageParams);
    }

    public int isExistUser(int userId) {
        String isExistUserQuery = "select exists(select * from Users where userId = ?);";
        int isExistParams = userId;

        return this.jdbcTemplate.queryForObject(isExistUserQuery, int.class, isExistParams);
    }

    public int isExistBank(int bankId) {
        String isExistBankQuery = "select exists(select * from Banks where bankId = ?);";
        int isExistBankParams = bankId;

        return this.jdbcTemplate.queryForObject(isExistBankQuery, int.class, isExistBankParams);
    }
    public int getuserCouPayMoney(int userId) {
        String getuserCouPayMoneyQuery = "select couPayMoney from Users where userId = ?;";
        int getuserCouPayMoneyParams = userId;

        return this.jdbcTemplate.queryForObject(getuserCouPayMoneyQuery, int.class, getuserCouPayMoneyParams);
    }

    public int isFirstFavorite(int userId, int storeId) {
        String isFirstFavoriteQuery = "select exists(select favoriteId from Favorites where userId = ? and storeId = ?);";
        Object[] isFirstFavoriteParams = new Object[] {userId, storeId};

        return this.jdbcTemplate.queryForObject(isFirstFavoriteQuery, int.class, isFirstFavoriteParams);
    }
    public int addFavorite(int userId, int storeId) {
        String addFavoriteQuery = "insert into Favorites(userId, storeId) values (?,?);";
        Object[] addFavoritesParams = new Object[] {userId, storeId};

        return this.jdbcTemplate.update(addFavoriteQuery,addFavoritesParams);
    }

    public int getFavoriteCount(int userId) {
        String getFavoriteCountQuery = "select count(favoriteId) from Favorites group by userId having userId = ?;";
        int getFavoriteCountParams = userId;

        return this.jdbcTemplate.queryForObject(getFavoriteCountQuery, int.class, getFavoriteCountParams);
    }

    public int reOrCancelFavorite(int userId, int storeId) {
        String reOrCancelFavorite = "update Favorites set status = case when status = 1 then 0 when status = 0 then 1 end where userId = ? and storeId = ?;";
        Object[] reOrCancelFavoriteParams = new Object[] {userId, storeId};

        return this.jdbcTemplate.update(reOrCancelFavorite, reOrCancelFavoriteParams);
    }

    public List<UserAddresses> getUserAddresses(int userId) {
        String getUserAddressesQuery = "select addressId, name, detail from UserAddresses where userId = ?;";
        int getUserAddressesParams = userId;

        return this.jdbcTemplate.query(getUserAddressesQuery,
                (rs,rowNum)-> new UserAddresses(
                        rs.getInt("addressId"),
                        rs.getString("name"),
                        rs.getString("detail")
                ),
                getUserAddressesParams);
    }

    public MyReviewInfo getUserReview(int orderId) {
        String getUserReviewResQuery = "select S.name as storeName, Reviews.score, reviewImage, reviewContent, Reviews.createdAt, goodCount\n" +
                "from Reviews\n" +
                "    inner join Orders O on Reviews.orderId = O.orderId\n" +
                "    inner join Stores S on O.storeId = S.storeId\n" +
                "    left join (select reviewId, count(userId) as goodCount from ReviewResponses where responseType = 'GOOD' group by reviewId) RGC on Reviews.reviewId = RGC.reviewId\n" +
                "where Reviews.orderId = ?;";
        int getUserReviewResParams = orderId;

        return this.jdbcTemplate.queryForObject(getUserReviewResQuery,
                (rs,rowNum)-> new MyReviewInfo(
                        rs.getString("storeName"),
                        rs.getFloat("score"),
                        rs.getString("reviewImage"),
                        rs.getString("reviewContent"),
                        rs.getString("createdAt"),
                        rs.getInt("goodCount")
                ),
                getUserReviewResParams);
    }

    public GetMyEatsRes getMyEats(int userId) {
        String getMyEatsQuery = "select Users.name, insert(Users.phoneNumber, 4, 4, '-****-') as phoneNumber, reviewCount, helpCount, favoriteCount\n" +
                "from Users\n" +
                "    left join (select userId, count(reviewId) as reviewCount from Reviews group by userId) MR on Users.userId = MR.userId\n" +
                "    left join (select R.userId, count(responseId) as helpCount\n" +
                "                from ReviewResponses\n" +
                "                    inner join Reviews R on ReviewResponses.reviewId = R.reviewId\n" +
                "                where responseType = 'GOOD'\n" +
                "                group by R.userId) MH on Users.userId = MH.userId\n" +
                "    left join (select userId, count(favoriteId) as favoriteCount from Favorites group by userId) MF on Users.userId = MF.userId\n" +
                "where Users.userId = ?;";
        int getMyEatsParams = userId;

        return this.jdbcTemplate.queryForObject(getMyEatsQuery,
                (rs,rowNum)-> new GetMyEatsRes(
                        rs.getString("name"),
                        rs.getString("phoneNumber"),
                        rs.getInt("reviewCount"),
                        rs.getInt("helpCount"),
                        rs.getInt("favoriteCount")
                ),
                getMyEatsParams);
    }

    public int isExistAddress(int addressId) {
        String isExistAddressQuery = "select exists(select * from UserAddresses where addressId = ? and status = 1);";
        int isExistAddressParams = addressId;

        return this.jdbcTemplate.queryForObject(isExistAddressQuery, int.class, isExistAddressParams);
    }

    public int getUserIdByAddressId(int addressId) {
        String getUserIdByAddressIdQuery = "select userId from UserAddresses where addressId = ?;";
        int getUserIdByAddressIdParams = addressId;

        return this.jdbcTemplate.queryForObject(getUserIdByAddressIdQuery, int.class, getUserIdByAddressIdParams);
    }

    public int isHomeExist(int userId) {
        String isHomeExistQuery = "select exists(select * from UserAddresses where userId = ? and name = 'HOME' and status = 1)";
        int isHomeExistParams = userId;

        return this.jdbcTemplate.queryForObject(isHomeExistQuery, int.class, isHomeExistParams);
    }

    public int isOfficeExist(int userId) {
        String isOfficeExistQuery = "select exists(select * from UserAddresses where userId = ? and name = 'OFFICE' and status = 1)";
        int isOfficeExistParams = userId;

        return this.jdbcTemplate.queryForObject(isOfficeExistQuery, int.class, isOfficeExistParams);
    }

    public void patchUserFavorites(int userId, PatchFavoriteReq patchFavoriteReq) {
        String patchUserFavoritesQuery = "update Favorites set status = 0 where userId = ? and storeId = ?;";

        for(int i = 0; i < patchFavoriteReq.getStores().size(); i++) {
            Object[] patchUserFavoritesParams = new Object[] {userId, patchFavoriteReq.getStores().get(i)};
            this.jdbcTemplate.update(patchUserFavoritesQuery, patchUserFavoritesParams);
        }
    }

    public void patchUserAddress(int userId, PatchAddressReq patchAddressReq) {
        String patchUserAddressQuery = "update UserAddresses set status = 0 where userId = ? and addressId = ?;";
        Object[] patchUserAddressParams = new Object[] {userId, patchAddressReq.getAddressId()};

        this.jdbcTemplate.update(patchUserAddressQuery, patchUserAddressParams);
    }

    public int postPayment(int userId, PostPaymentReq postPaymentReq) {
        String postPaymentQuery = "insert into UserPayments (userId, bankId, type, number) values (?,?,?,?);";
        Object[] postPaymentParams = new Object[] {userId, postPaymentReq.getBankId(), postPaymentReq.getType(), postPaymentReq.getNumber()};

        this.jdbcTemplate.update(postPaymentQuery, postPaymentParams);

        String lastInsertIdQuery = "select last_insert_id()"; // update 작업이 끝나고 해당 유저의 pk 를 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int postUserAddress(int userId, PostAddressReq postAddressReq) {
        String postUserAddressQuery = "insert into UserAddresses (userId, name, detail, latitude, longitude) values (?,?,?,?,?);";
        Object[] postUserAddressParams = new Object[] {userId, postAddressReq.getName(), postAddressReq.getDetail(), postAddressReq.getLatitude(), postAddressReq.getLongitude()};

        this.jdbcTemplate.update(postUserAddressQuery, postUserAddressParams);

        String lastInsertIdQuery = "select last_insert_id()"; // update 작업이 끝나고 해당 유저의 pk 를 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int isExistFavorite(int userId, int storeId) {
        String isExistFavoriteQuery = "select exists(select * from Favorites where userId = ? and storeId = ? and status = 1);";
        Object[] isExistFavoriteParams = new Object[] {userId, storeId};

        return this.jdbcTemplate.queryForObject(isExistFavoriteQuery, int.class, isExistFavoriteParams);
    }

    public int isExistPayment(int userId, PostPaymentReq postPaymentReq) {
        String isExistPaymentQuery = "select exists(select * from UserPayments where userId = ? and bankId = ? and type = ? and number = ?)";
        Object[] isExistPaymentParams = new Object[] {userId, postPaymentReq.getBankId(), postPaymentReq.getType(), postPaymentReq.getNumber()};

        return this.jdbcTemplate.queryForObject(isExistPaymentQuery, int.class, isExistPaymentParams);
    }

    public int getFavoriteStatus(int userId, int storeId) {
        String getFavoriteStatusQuery = "select status from Favorites where userId = ? and storeId = ?;";
        Object[] getFavoriteStatusParams = new Object[] {userId, storeId};

        return this.jdbcTemplate.queryForObject(getFavoriteStatusQuery, int.class, getFavoriteStatusParams);
    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from Users where email = ?)";
        String checkEmailParams = email;

        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int checkNumber(String number) {
        String checkEmailQuery = "select exists(select phoneNumber from Users where phoneNumber = ?)";
        String checkEmailParams = number;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }


    public PostLoginReq getPwd(int userIdx) {

        String getPwdQuery = "select userId ,password ,email from Users where userId = ?";
        int getPwdParams = userIdx;

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new PostLoginReq(
                        rs.getString("email"),
                        rs.getString("password")

                ),getPwdParams
        );

    }

    public int getUserIdx(String email){
        String getUserIdxQuery = "select userId from Users where email= ?";
        String  getUserIdxParams = email;

        return this.jdbcTemplate.queryForObject(getUserIdxQuery,int.class, getUserIdxParams);

    }


    public GetReceiptInfo getReceiptInfo(int orderId) {

        GetReceiptInfo getReceiptInfo = new GetReceiptInfo();

        String getOrderInfoQuery = "select S.name,\n" +
                "       orderId,\n" +
                "       Orders.createdAt AS createdAt,\n" +
                "       S.deliveryFee AS fee,\n" +
                "       totalPrice,\n" +
                "       bankName,\n" +
                "       bankLogoImage,\n" +
                "       number,\n" +
                "       detail\n" +
                "from Orders\n" +
                "         inner join (select storeId, deliveryFee, name from Stores) S on Orders.storeId = S.storeId\n" +
                "         inner join UserPayments UP on Orders.paymentId = UP.paymentId\n" +
                "         inner join Banks B on UP.bankId = B.bankId\n" +
                "         inner join UserAddresses UA on Orders.addressId = UA.addressId\n" +
                "where orderId = ?";

        int getOrderInfoByOrderIdParam = orderId;
        GetReceiptRes receiptInfo = this.jdbcTemplate.queryForObject(getOrderInfoQuery, (rs, rowNum) -> new GetReceiptRes(
                rs.getString("name"),
                rs.getInt("orderId"),
                rs.getString("createdAt"),
                rs.getInt("totalPrice"),
                rs.getInt("fee"),
                rs.getString("detail"),
                rs.getString("bankName"),
                rs.getString("number"),
                rs.getString("bankLogoImage")
        ),getOrderInfoByOrderIdParam);

        getReceiptInfo.setGetReceiptRes(receiptInfo);



        String getMenuNameQuery = "select menuName,price,optionList,count\n" +
                "from OrderLists\n" +
                "         inner join Menus M on OrderLists.menuId = M.menuId\n" +
                "where OrderLists.orderId = ?;";



        List<OrderMenuInfo> orderMenuInfoList = this.jdbcTemplate.query(getMenuNameQuery, (rs, rowNum) -> new OrderMenuInfo(
                rs.getString("menuName"),
                rs.getInt("price"),
                rs.getInt("count"),
                rs.getString("optionList")
        ),orderId);

        int cnt = orderMenuInfoList.size();

        List<ViewOrder> viewOrderList = new ArrayList<>();

        for(int i =0;i<cnt;i++){
            ViewOrder viewOrder = new ViewOrder();
            OrderMenuInfo mNo = orderMenuInfoList.get(i);

            viewOrder.setMenuName(mNo.getMenuName());
            viewOrder.setMenuPrice(mNo.getMenuPrice());
            viewOrder.setCount(mNo.getCount());


            String opt = mNo.getOptString();
            System.out.println(opt);
            String[] opt_list = opt.split(",");
            int opt_cnt = opt_list.length;
            List<OptPrice> optList = new ArrayList<>();
            OptPrice optValue = new OptPrice();

            for(int j=0;j<opt_cnt;j++) {
                System.out.println(opt_list[j]);
                optValue = this.jdbcTemplate.queryForObject("select optionName,optionPrice from MenuOptions where menuOptionId = ?", (rs, rowNum) -> new OptPrice(
                        rs.getString("optionName"),
                        rs.getInt("optionPrice")
                ),opt_list[j]);
                optList.add(optValue);
            }
            viewOrder.setOptList(optList);
            viewOrderList.add(viewOrder);

        }

        getReceiptInfo.setGetReceiptRes(receiptInfo);
        getReceiptInfo.setViewOrderList(viewOrderList);


        return getReceiptInfo;


    }

    public List<GetOrderInfo> getOrdersInfo(int userId) {
        List<GetOrderInfo> getOrdersInfo = new ArrayList<>();


        String getOrdersIdQuery = "select orderId from Orders O where O.userId = ? order by createdAt desc limit 0,10";
        int getOrdersIdByUserIdParam = userId;

        List<Integer> ordersId = this.jdbcTemplate.queryForList(getOrdersIdQuery,int.class,getOrdersIdByUserIdParam);
//        System.out.println(ordersId.get(0));
//        System.out.println(ordersId.get(1));
        for (Integer orderIdx : ordersId) {
            GetOrderInfo getOrderInfo = new GetOrderInfo();
            System.out.println(orderIdx);
            String getOrderInfoQuery = "select orderId,\n" +
                    "       U.name           AS userName,\n" +
                    "       S.name           AS storeName,\n" +
                    "       totalPrice,\n" +
                    "       detail,\n" +
                    "       disposable,\n" +
                    "       Orders.createdAt AS createdAt,\n" +
                    "       storeRequest,\n" +
                    "       riderReqeust     AS riderRequest,\n" +
                    "       payments\n" +
                    "from Orders\n" +
                    "         inner join Users U on Orders.userId = U.userId\n" +
                    "         inner join Stores S on Orders.storeId = S.storeId\n" +
                    "         left join (select userId,min(addressId),detail from UserAddresses group by userId) UA on U.userId = UA.userId\n" +
                    "where Orders.orderId = ?";

            int getOrderInfoByOrderIdParam = orderIdx;
            OrderInfo orderInfo = this.jdbcTemplate.queryForObject(getOrderInfoQuery, (rs, rowNum) -> new OrderInfo(
                    rs.getInt("orderId"),
                    rs.getString("userName"),
                    rs.getString("storeName"),
                    rs.getInt("totalPrice"),
                    rs.getString("detail"),
                    rs.getBoolean("disposable"),
                    rs.getString("createdAt"),
                    rs.getString("storeRequest"),
                    rs.getString("riderRequest"),
                    rs.getString("payments")

            ), getOrderInfoByOrderIdParam);

            getOrderInfo.setOrderInfo(orderInfo);


            String getMenuNameQuery = "select menuName,optionList,count\n" +
                    "from OrderLists\n" +
                    "         inner join Menus M on OrderLists.menuId = M.menuId\n" +
                    "where OrderLists.orderId = ?;";

            int orderId = orderInfo.getOrderId();

            List<MenuNameNOpt> menuNameNOpts = this.jdbcTemplate.query(getMenuNameQuery, (rs, rowNum) -> new MenuNameNOpt(
                    rs.getString("menuName"),
                    rs.getString("optionList"),
                    rs.getInt("count")
            ), orderIdx);

            int cnt = menuNameNOpts.size();
            List<MenuOpt> mnolist = new ArrayList<>();
            String getOptNameQuery = "";
            for (int i = 0; i < cnt; i++) {
                MenuNameNOpt mNo = menuNameNOpts.get(i);
                String opt = mNo.getOptList();
                System.out.println("____");
                System.out.println(opt);
                System.out.println("____");
                String[] opt_list = opt.split(",");
                String opt_name = "";
                int opt_cnt = opt_list.length;
                for (int j = 0; j < opt_cnt; j++) {
                    String optList = this.jdbcTemplate.queryForObject("select optionName from MenuOptions where menuOptionId = ?;", String.class, Integer.parseInt(opt_list[j]));
                    opt_name += optList;
                    opt_name += ",";
                }
                mnolist.add(new MenuOpt(mNo.getMenuName(), opt_name,mNo.getCount()));
            }
            getOrderInfo.setMnO(mnolist);

            getOrdersInfo.add(getOrderInfo);
        }

        return getOrdersInfo;

    }



    public List<GetCouponRes> getCoupons(int userId) {
        String getCouponsQuery = "select name,\n" +
                "       couponName,\n" +
                "       couponType,\n" +
                "       date_format(endDate, '%m/%d') AS endDate,\n" +
                "       C.minimumOrder AS mini,\n" +
                "       discountPrice,\n" +
                "       (case when now() <= endDate then '사용가능' else concat(date_format(endDate, '%m/%d'),'일 까지입니다.') end) AS status\n" +
                "from UserCoupons\n" +
                "         inner join Coupons C on UserCoupons.couponId = C.couponId\n" +
                "         inner join Stores S on C.storeId = S.storeId\n" +
                "where userId = ?\n";

        return this.jdbcTemplate.query(getCouponsQuery, (rs, rowNum) -> new GetCouponRes(
                rs.getString("name"),
                rs.getString("couponName"),
                rs.getString("couponType"),
                rs.getString("endDate"),
                rs.getInt("mini"),
                rs.getInt("discountPrice"),
                rs.getString("status")
        ), userId);
    }

    public GetCouponRes addCoupon(String number, int userId) {
        String couponId = this.jdbcTemplate.queryForObject("select couponId from CouponsNumber where serialNumber = ?",String.class,number);

        String addCouponQuery = "insert into UserCoupons(userId, couponId) values (?,?)";
        String getCouponRes = "select name,\n" +
                "       couponName,\n" +
                "       couponType,\n" +
                "       endDate,\n" +
                "       C.minimumOrder                                                                                      AS mini,\n" +
                "       discountPrice,\n" +
                "       (case when now() <= endDate then '사용가능' else concat(date_format(endDate, '%m/%d'), '일 까지입니다.') end) AS status\n" +
                "from CouponsNumber\n" +
                "         inner join Coupons C on CouponsNumber.couponId = C.couponId\n" +
                "         inner join Stores S\n" +
                "                    on C.storeId = S.storeId\n" +
                "where CouponsNumber.serialNumber = ?";

        this.jdbcTemplate.update(addCouponQuery,userId,couponId);
        this.jdbcTemplate.update("update CouponsNumber set status = false where serialNumber = ?",number);
        return this.jdbcTemplate.queryForObject(getCouponRes, (rs, rowNum) -> new GetCouponRes(
                rs.getString("name"),
                rs.getString("couponName"),
                rs.getString("couponType"),
                rs.getString("endDate"),
                rs.getInt("mini"),
                rs.getInt("discountPrice"),
                rs.getString("status")
        ), number);

    }

    public int checkCoupon(String number) {
        String checkCouponQuery = "select count(C.couponId) from CouponsNumber inner join Coupons C on CouponsNumber.couponId = C.couponId where serialNumber = ? and now() < endDate and status = true";
        return this.jdbcTemplate.queryForObject(checkCouponQuery,int.class,number);



    }

    public boolean existCoupon(int userId) {
        String checkExistCouponQuery = "select exists(select * from UserCoupons where userId = ?)";

        return this.jdbcTemplate.queryForObject(checkExistCouponQuery,boolean.class,userId);
    }

    public boolean checkOrder(int userId) {
        String checkExistOrderQuery = "select exists(select * from Orders where userId = ?)";

        return this.jdbcTemplate.queryForObject(checkExistOrderQuery,boolean.class,userId);

    }

    public boolean checkSerial(String number) {
        String checkSerialQuery = "select exists(select * from CouponsNumber where serialNumber = ?);";

        return this.jdbcTemplate.queryForObject(checkSerialQuery,boolean.class,number);
    }


}
