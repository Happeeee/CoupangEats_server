package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.review.ReviewProvider;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.src.store.model.GetOrderInfo;
import com.example.demo.src.user.model.*;
import com.example.demo.src.user.model.DTO.MyReviewInfo;
import com.example.demo.src.user.model.DTO.UserAddresses;
import com.example.demo.src.user.model.DTO.UserPayments;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;
    private final StoreProvider storeProvider;
    private final ReviewProvider reviewProvider;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService,StoreProvider storeProvider,ReviewProvider reviewProvider) {
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.storeProvider = storeProvider;
        this.reviewProvider = reviewProvider;
    }

    public int getUserIdByAddressId(int addressId) throws BaseException {
        try {
            return userDao.getUserIdByAddressId(addressId);
        } catch (Exception exception) {
            logger.error("App - getUserIdByAddressId Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isHomeExist(int userId) throws BaseException {
        try {
            return userDao.isHomeExist(userId);
        } catch (Exception exception) {
            logger.error("App - isHomeExist Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isOfficeExist(int userId) throws BaseException {
        try {
            return userDao.isOfficeExist(userId);
        } catch (Exception exception) {
            logger.error("App - isOfficeExist Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistAddress(int addressId) throws BaseException {
        try {
            return userDao.isExistAddress(addressId);
        } catch (Exception exception) {
            logger.error("App - isExistAddress Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistPayment(int userId, PostPaymentReq postPaymentReq) throws BaseException {
        try {
            return userDao.isExistPayment(userId, postPaymentReq);
        } catch (Exception exception) {
            logger.error("App - isExistPayment Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<UserPayments> getUserPayments(int userId) throws BaseException {
        if(isExistUser(userId) == 0)
            throw new BaseException(NOT_EXIST_USER);

        try {
            List<UserPayments> userPayments = userDao.getUserPayments(userId);
            return userPayments;
        } catch (Exception exception) {
            logger.error("App - getPaymentManage Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistUser(int userId) throws BaseException {
        try {
            return userDao.isExistUser(userId);
        } catch (Exception exception) {
            logger.error("App - isExistUser Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public int getuserCouPayMoney(int userId) throws BaseException {
        if(isExistUser(userId) == 0)
            throw new BaseException(NOT_EXIST_USER);

        try {
            int userCouPayMoney = userDao.getuserCouPayMoney(userId);
            return userCouPayMoney;
        } catch (Exception exception) {
            logger.error("App - getuserCouPayMoney Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistBank(int bankId) throws BaseException {
        try {
            return userDao.isExistBank(bankId);
        } catch (Exception exception) {
            logger.error("App - isExistBank Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isFirstFavorite(int userId, int storeId) throws BaseException {
        if(storeProvider.isExistStore(storeId) == 0)
            throw new BaseException(NOT_EXIST_STORE);
        try {
            return userDao.isFirstFavorite(userId, storeId);
        } catch (Exception exception) {
            logger.error("App - isFirstFavorite Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<UserAddresses> getUserAddresses(int userId) throws BaseException {

        if(isExistUser(userId) == 0)
            throw new BaseException(NOT_EXIST_USER);

        try {
            List<UserAddresses> userAddresses = userDao.getUserAddresses(userId);
            return userAddresses;
        } catch(Exception exception) {
            logger.error("App - getUserAddresses Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public MyReviewInfo getUserReview(int userId, int orderId) throws BaseException {
        if(userId != reviewProvider.getUserIdByOrderId(orderId)) // 유저의 주문이 아닐때
            throw new BaseException(NOT_USERORDER);

        if(reviewProvider.isExistReview(userId, orderId) == 0) // 유저가 작성한 리뷰가 존재하지 않을때
            throw new BaseException(NOT_EXIST_USER_REVIEW);

        try {
            MyReviewInfo myReviewInfo = userDao.getUserReview(orderId);
            return myReviewInfo;
        } catch (Exception exception) {
            logger.error("App - getUserReviewRes Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getFavoriteStatus(int userId, int storeId) throws BaseException {
        try {
            return userDao.getFavoriteStatus(userId, storeId);
        } catch (Exception exception) {
            logger.error("App - getFavoriteStatus Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getFavoriteCount(int userId) throws BaseException {
        try {
            return userDao.getFavoriteCount(userId);
        } catch (Exception exception) {
            logger.error("App - getFavoriteCount Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isExistFavorite(int userId, PatchFavoriteReq patchFavoriteReq) throws BaseException {
        try {
            boolean isExist = true;
            for(int i = 0; i < patchFavoriteReq.getStores().size(); i++) {
                if(userDao.isExistFavorite(userId, patchFavoriteReq.getStores().get(i)) == 0)
                    isExist = false;
            }
            return isExist;
        } catch (Exception exception) {
            logger.error("App - isExistFavorite Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public GetMyEatsRes getMyEats(int userId) throws BaseException {
        try {
            GetMyEatsRes getMyEatsRes = userDao.getMyEats(userId);
            return getMyEatsRes;
        } catch (Exception exception) {
            logger.error("App - getMyEats Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException {
        try {

            return userDao.checkEmail(email);
        } catch (Exception exception) {
            logger.error("App - checkEmail Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPhoneNumber(String number) throws BaseException {
        try {
            return userDao.checkNumber(number);
        } catch (Exception exception) {
            logger.error("App - checkNumber Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        try {
            int userIdx = userDao.getUserIdx(postLoginReq.getEmail());

            PostLoginReq user = userDao.getPwd(userIdx);


//            String encryptPwd;
//            try {
//                encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
//                System.out.println("-----------2");
//            } catch (Exception exception) {
//                logger.error("App - logIn Provider Encrypt Error", exception);
//                System.out.println("-----------3");
//                throw new BaseException(PASSWORD_DECRYPTION_ERROR);
//
//            }

            if (user.getPassword().equals(postLoginReq.getPassword())) {

                String jwt = jwtService.createJwt(userIdx);

                return new PostLoginRes(userIdx, jwt);
            } else {
                throw new BaseException(FAILED_TO_LOGIN);
            }
        } catch (RuntimeException exception) {
            logger.error("App - logIn Provider Error", exception);
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public GetReceiptInfo getReceiptInfo(int orderId) throws BaseException {
        try {
            return userDao.getReceiptInfo(orderId);
        } catch (Exception exception) {
            logger.error("App - getReceiptInfo Provider Error", exception);
            throw new BaseException(NOT_EXIST_SELECTED_USER);
        }
    }
    @Transactional(readOnly = true)
    public List<GetOrderInfo> getOrdersInfo(int userId) throws BaseException {
        if (!userDao.checkOrder(userId)) {
            throw new BaseException(USER_NO_EXIST_ORDER);
        }
        try {
            return userDao.getOrdersInfo(userId);
        } catch (Exception exception) {
            logger.error("App - getOrdersInfo Provider Error", exception);
            throw new BaseException(NOT_EXIST_SELECTED_USER);

        }
    }



    public List<GetCouponRes> getCoupons(int userId) throws BaseException {
        if (!userDao.existCoupon(userId)) {
            throw new BaseException(USER_NO_EXIST_COUPON);
        }
        System.out.println(userDao.existCoupon(userId));
        try {
            return userDao.getCoupons(userId);
        } catch (Exception exception) {
            logger.error("App - getOrdersInfo Provider Error", exception);

            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean checkSerial(String number) throws BaseException {
        try {
            return userDao.checkSerial(number);
        } catch (Exception exception) {
            logger.error("App - checkSerial Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
