package com.example.demo.src.store;


import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.PostOrderReq;
import com.example.demo.src.store.model.PostOrderRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service

public class StoreService {
    private final StoreDao storeDao;
    private final StoreProvider storeProvider;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    public StoreService(StoreDao storeDao, StoreProvider storeProvider, JwtService jwtService) {
        this.storeDao = storeDao;
        this.storeProvider = storeProvider;
        this.jwtService = jwtService;
    }

    public PostOrderRes addOrder(PostOrderReq postOrderReq, int storeId) throws BaseException{
        int userId = jwtService.getUserIdx();
        if(!(storeDao.checkPasswd(userId).equals(postOrderReq.getPassword()))){

            throw new BaseException(POST_USERS_PASSWD_NOT_CORRECT);
        }
        try{

            PostOrderRes result = storeDao.postOrder(postOrderReq,storeId,userId);
            return result;
        }catch (Exception exception) {
            logger.error("App - getStoreDetail Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void cancelOrder(int orderId) throws BaseException{
        if(!storeDao.checkOrder(orderId)){
            throw new BaseException(PATCH_ORDER_ALREADY_CANCELED);
        }
        try{
            storeDao.cancelOrder(orderId);
        }catch (Exception exception){
            logger.error("App - cancelOrder Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
