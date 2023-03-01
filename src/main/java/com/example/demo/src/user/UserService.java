package com.example.demo.src.user;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;
import static com.example.demo.utils.ValidationRegex.isRegexSerialNum;

// Service Create, Update, Delete 의 로직 처리
@Service
@RequiredArgsConstructor
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;
    private final StoreProvider storeProvider;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;



    public String upload(MultipartFile multipartFile) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    public String getThumbnailPath(String path) {
        return amazonS3Client.getUrl(bucket, path).toString();
    }




    //POST
    @Transactional
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){

            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }



        if(userProvider.checkPhoneNumber(postUserReq.getPhoneNumber()) ==1){

            throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
        }

//        String pwd;
//        try{
//            //암호화
//            pwd = new SHA256().encrypt(postUserReq.getPassword());
//            postUserReq.setPassword(pwd);
//
//        } catch (Exception ignored) {
//            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
//        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            logger.error("App - createUser Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {BaseException.class})
    public void addFavorite(int userId, int storeId) throws BaseException {
        if(storeProvider.isExistStore(storeId) == 0)
            throw new BaseException(NOT_EXIST_STORE);

        try {
            int result = userDao.addFavorite(userId, storeId);
            if(result == 0)
                throw new BaseException(DATABASE_ERROR);
        } catch (Exception exception) {
            logger.error("App - addFavorite Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = {BaseException.class})
    public void reOrCancelFavorite(int userId, int storeId) throws BaseException {
        try {
            int result = userDao.reOrCancelFavorite(userId, storeId);
            if(result == 0)
                throw new BaseException(DATABASE_ERROR);
        } catch (Exception exception) {
            logger.error("App - reOrCancelFavorite Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = {BaseException.class})
    public void patchUserFavorites(int userId, PatchFavoriteReq patchFavoriteReq) throws BaseException {

        if(!userProvider.isExistFavorite(userId, patchFavoriteReq))
            throw new BaseException(NOT_USER_FAVORITE_STORE);

        try {
            userDao.patchUserFavorites(userId, patchFavoriteReq);
        } catch (Exception exception) {
            logger.error("App - patchUserFavorites Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = {BaseException.class})
    public PostAddressRes postUserAddress(int userId, PostAddressReq postAddressReq) throws BaseException {
        if(postAddressReq.getName().equals("HOME")) { // 중복체크
            if(userProvider.isHomeExist(userId) == 1)
                throw new BaseException(EXIST_USER_HOME_ADDRESS);
        }

        if(postAddressReq.getName().equals("OFFICE")) { // 중복체크
            if(userProvider.isOfficeExist(userId) == 1)
                throw new BaseException(EXIST_USER_OFFICE_ADDRESS);
        }

        try {
            int addressId = userDao.postUserAddress(userId, postAddressReq);
            return new PostAddressRes(addressId);
        } catch (Exception exception) {
            logger.error("App - postUserAddress Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = {BaseException.class})
    public void patchUserAddress(int userId, PatchAddressReq patchAddressReq) throws BaseException {

        if(userProvider.isExistAddress(patchAddressReq.getAddressId()) == 0)
            throw new BaseException(NOT_EXIST_ADDRESS);

        int userIdByAddressId = userProvider.getUserIdByAddressId(patchAddressReq.getAddressId());

        if(userId != userIdByAddressId)
            throw new BaseException(WRONG_ADDRESSID);

        try {
            userDao.patchUserAddress(userId, patchAddressReq);
        } catch (Exception exception) {
            logger.error("App - patchUserAddress Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = {BaseException.class})
    public PostPaymentRes postPayment(int userId, PostPaymentReq postPaymentReq) throws BaseException {
        if(userProvider.isExistBank(postPaymentReq.getBankId()) == 0)
            throw new BaseException(NOT_EXIST_BANK);

        if(userProvider.isExistPayment(userId, postPaymentReq) == 1)
            throw new BaseException(EXIST_PAYMENT);

        try {
            int paymentId = userDao.postPayment(userId, postPaymentReq);
            return new PostPaymentRes(paymentId);
        } catch(Exception exception) {
            logger.error("App - PostPaymentRes Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetCouponRes addCoupon(String number) throws BaseException{
        if(!userProvider.checkSerial(number)){
            throw new BaseException(POST_NO_EXIST_SERIAL);
        }
        if (!isRegexSerialNum(number)) {
            throw new BaseException(POST_USERS_INVALID_FORMAT_SERIAL);
        }
        if(userDao.checkCoupon(number) == 0){
            throw new BaseException(USER_INVALID_COUPON);
        }

        try{

            return userDao.addCoupon(number,jwtService.getUserIdx());
        }catch (Exception exception){
            logger.error("App - addCoupon Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
