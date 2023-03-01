package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.*;
import com.example.demo.src.store.model.DTO.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class StoreProvider {

    private final StoreDao storeDao;
    private final JwtService jwtService;

    @Autowired
    public StoreProvider(StoreDao storeDao, JwtService jwtService) {
        this.storeDao = storeDao;
        this.jwtService = jwtService;
    }
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public int getUserIdByAddressId(int addressId) throws BaseException {
        if(storeDao.isExistAddress(addressId) == 0)
            throw new BaseException(NOT_EXIST_ADDRESS);

        try {
            int userIdByAddressId = storeDao.getUserIdByAddressId(addressId);
            return userIdByAddressId;
        } catch (Exception exception) {
            logger.error("App - getUserIdByAddressId Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<UserFavorites> getUserFavorites(int userId, double userLatitude, double userLongitude) throws BaseException {
        try {
            List<UserFavorites> userFavoriteRes = storeDao.getUserFavorites(userId, userLatitude, userLongitude);
            return userFavoriteRes;
        } catch (Exception exception) {
            logger.error("App - GetUserFavorites Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreRes> searchStoreByCategoryAboutMenu(int userId, int categoryId, double userLatitude, double userLongitude) throws BaseException {
        try {
            List<GetStoreRes> getStoreRes = storeDao.searchStoreByCategoryAboutMenu(userId, categoryId, userLatitude, userLongitude);
            return getStoreRes;
        } catch (Exception exception) {
            logger.error("App - searchStoreByCategoryAboutMenu Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreRes> searchStoreByCategoryAboutProperty(int userId, String conditionLine, double userLatitude, double userLongitude) throws BaseException {
        try {
            List<GetStoreRes> getStoreRes = storeDao.searchStoreByCategoryAboutProperty(userId, conditionLine, userLatitude, userLongitude);
            return getStoreRes;
        } catch (Exception exception) {
            logger.error("App - searchStoreByCategoryAboutProperty Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreNoLoginRes> searchStoreByCategoryAboutMenuNoLogin(int categoryId, double userLatitude, double userLongitude) throws BaseException {
        try {
            List<GetStoreNoLoginRes> getStoreNoLoginRes = storeDao.searchStoreByCategoryAboutMenuNoLogin(categoryId, userLatitude, userLongitude);
            return getStoreNoLoginRes;
        } catch (Exception exception) {
            logger.error("App - searchStoreByCategoryAboutMenuNoLogin Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreNoLoginRes> searchStoreByCategoryAboutPropertyNoLogin(String conditionLine, double userLatitude, double userLongitude) throws BaseException {
        try {
            List<GetStoreNoLoginRes> getStoreNoLoginReqs = storeDao.searchStoreByCategoryAboutPropertyNoLogin(conditionLine, userLatitude, userLongitude);
            return getStoreNoLoginReqs;
        } catch (Exception exception) {
            logger.error("App - searchStoreByCategoryAboutPropertyNoLogin Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreRes> getChooseStores(int userId, double userLatitude, double userLongitude) throws BaseException {
        try {
            List<GetStoreRes> getStoreRes = storeDao.getChooseStores(userId, userLatitude, userLongitude);
            return getStoreRes;
        } catch (Exception exception) {
            logger.error("App - getChooseStores Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreNoLoginRes> getChooseStoresNoLogin(double userLatitude, double userLongitude) throws BaseException {
        try {
            List<GetStoreNoLoginRes> getStoreNoLoginRes = storeDao.getChooseStoresNoLogin(userLatitude, userLongitude);
            return getStoreNoLoginRes;
        } catch (Exception exception) {
            logger.error("App - getChooseStoresNoLogin Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreRes> getEatsOriginalStores(int userId, double userLatitude, double userLongitude) throws BaseException {
        try {
            List<GetStoreRes> getStoreRes = storeDao.getEatsOriginalStores(userId, userLatitude, userLongitude);
            return getStoreRes;
        } catch(Exception exception) {
            logger.error("App - getEatsOriginalStores Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreRes> getTownPopularStores(int userId, double userLatitude, double userLongitude) throws BaseException {
        try {
            List<GetStoreRes> getStoreRes = storeDao.getTownPopularStores(userId, userLatitude, userLongitude);
            return getStoreRes;
        } catch(Exception exception) {
            logger.error("App - getTownPopularStores Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreRes> getUserFrequentOrderStores(int userId, double userLatitude, double userLongitude) throws BaseException {
        try {
            List<GetStoreRes> getStoreRes = storeDao.getUserFrequentOrderStores(userId, userLatitude, userLongitude);
            return getStoreRes;
        } catch(Exception exception) {
            logger.error("App - getUserFrequentOrderStores Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public UserCoordinate getUserCoordinate(int userAddressId) throws BaseException {
        try {
            UserCoordinate userCoordinate = storeDao.getUserCoordinate(userAddressId);
            return userCoordinate;
        } catch (Exception exception) {
            logger.error("App - getUserCoordinate Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetStoreCategoryRes> getStoreCategories() throws BaseException {
        try {
            List<GetStoreCategoryRes> getStoreCategoryRes = storeDao.getStoreCategories();
            return getStoreCategoryRes;
        } catch (Exception exception) {
            logger.error("App - getStoreCategories Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistStore(int storeId) throws BaseException {
        try {
            return storeDao.isExistStore(storeId);
        } catch (Exception exception) {
            logger.error("App - isExistStore Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public StoreReviewScore getStoreReviewScore(int storeId) throws BaseException {
        if(isExistStore(storeId) == 0)
            throw new BaseException(NOT_EXIST_STORE);

        try {
            StoreReviewScore storeReviewScore = storeDao.getStoreReviewScore(storeId);
            return storeReviewScore;
        } catch (Exception exception) {
            logger.error("App - getStoreReviewScore Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<ReviewOnly> getOnlyReviews(int storeId) throws BaseException {
        try {
            List<ReviewOnly> reviewOnly = storeDao.getOnlyReviews(storeId);
            return reviewOnly;
        } catch (Exception exception) {
            logger.error("App - getOnlyReviews Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<MenuOnly> getOnlyMenus(int orderId) throws BaseException {
        try {
            List<MenuOnly> menuOnly = storeDao.getOnlyMenus(orderId);
            return menuOnly;
        } catch (Exception exception) {
            logger.error("App - getOnlymenus Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetSearchResults getSearchResults(List<Integer> storeList,double userLatitude,double userLongitude,int userId) throws BaseException {
        try {
            GetSearchResults getSearchResults = storeDao.getSearchResults(storeList,userId,userLatitude,userLongitude);
            return getSearchResults;
        } catch (Exception exception) {
            logger.error("App - getChooseStores Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public GetSearchResults getSearchResults_noJWT(List<Integer> storeList,double userLatitude,double userLongitude) throws BaseException {
        try {
            GetSearchResults getSearchResults = storeDao.getSearchResults_noJWT(storeList,userLatitude,userLongitude);
            return getSearchResults;
        } catch (Exception exception) {
            logger.error("App - getChooseStores Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public UserCoordinate getUserCoordinate_(int userId) throws BaseException {
        try {
            UserCoordinate userCoordinate = storeDao.getUserCoordinate_(userId);

            return userCoordinate;
        } catch (Exception exception) {
            logger.error("App - getUserCoordinate Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetStoreDetailRes getStoreDetail(int storeId,int userId) throws BaseException {
        if(!storeDao.checkStoreStatus(storeId)){
            throw new BaseException(POST_STORE_INVALID_STATUS);
        }


        try{
            return storeDao.getStoreDetail(storeId,userId);
        }
        catch (EmptyResultDataAccessException empty){
            logger.error("App - getStoreDetail Service Error", empty);
            throw new BaseException(POST_STORE_INVALID_INFO);
        }
        catch (RuntimeException exception){
            logger.error("App - getStoreDetail Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public GetStoreDetailRes getStoreDetail_noJWT(int storeId,UserCoordinate userCoordinate) throws BaseException {
        if(!storeDao.checkStoreStatus(storeId)){
            throw new BaseException(POST_STORE_INVALID_STATUS);
        }

        try{
            return storeDao.getStoreDetail(storeId,userCoordinate);
        }
        catch (EmptyResultDataAccessException empty){
            logger.error("App - getStoreDetail Service Error", empty);
            throw new BaseException(POST_STORE_INVALID_INFO);
        }
        catch (RuntimeException exception){
            logger.error("App - getStoreDetail Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetStoreInfo getStoreInfo(int storeId) throws BaseException {
        if(!storeDao.checkStoreStatus(storeId)){
            throw new BaseException(POST_STORE_INVALID_STATUS);
        }


        try{
            return storeDao.getStoreInfo(storeId);

        }
        catch (EmptyResultDataAccessException empty){
            logger.error("App - getStoreInfo Service Error", empty);
            throw new BaseException(POST_STORE_INVALID_INFO);
        }
        catch (RuntimeException exception){
            logger.error("App - getStoreInfo Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetMenuRes getMenuDetail(int storeId, int menuId) throws BaseException{
        if(!storeDao.getMenuStatus(menuId)){
            throw new BaseException(POST_MENU_INVALID_STATUS);
        }
        if(storeDao.checkMenuId(menuId) != 1){
            throw new BaseException(POST_MENU_INVALID_ID);
        }

        try{
            return storeDao.getMenuDetail(storeId,menuId);
        }catch (RuntimeException exception){
            logger.error("App - getMenuDetail Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }
//
//    public GetOrderInfo getOrderInfo(int userId) throws BaseException{
//        try{
//            return storeDao.getOrderInfo(userId);
//        }catch(Exception exception){
//            logger.error("App - getOrderInfo Service Error", exception);
//            throw new BaseException(NOT_EXIST_SELECTED_USER);
//        }
//    }
//
//
//
//    public List<GetOrderInfo> getOrdersInfo(int userId) throws BaseException{
//        try{
//            return storeDao.getOrdersInfo(userId);
//        } catch(Exception exception){
//            logger.error("App - getOrdersInfo Service Error", exception);
//            throw new BaseException(NOT_EXIST_SELECTED_USER);
//
//        }
//    }

    public List<Integer> getSearchInfo(String keyword) throws BaseException{
        try{
            return storeDao.getSearchInfo(keyword);
        } catch(Exception exception){
            logger.error("App - getSearchInfo Service Error", exception);
            throw new BaseException(GET_SEARCH_ERROR);

        }
    }

    public List<GetCouponRes> getCouponRes(String storeId) throws BaseException{
        if(storeDao.checkStoreCoupon(storeId) == 0){

            throw new BaseException(POST_STORE_NO_COUPON);
        }

        try{
            return storeDao.getCouponRes(storeId);
        }catch(Exception exception){
            logger.error("App - getCouponRes Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
