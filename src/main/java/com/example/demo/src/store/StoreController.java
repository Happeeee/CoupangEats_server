package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.*;
import com.example.demo.src.store.model.DTO.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/coupang-eats/stores")
public class StoreController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StoreProvider storeProvider;
    private final StoreService storeService;
    private final StoreDao storeDao;
    private final JwtService jwtService;



    public StoreController(StoreProvider storeProvider, StoreService storeService, StoreDao storeDao, JwtService jwtService) {
        this.storeProvider = storeProvider;
        this.storeService = storeService;
        this.storeDao = storeDao;
        this.jwtService = jwtService;
    }

    /**
     * (홈화면) 골라먹는 맛집 목록 불러오기 API
     * [GET] /stores/choose-stores
     * @return BaseResponse<List<GetStoreRes>>
     * ★ 거리 20KM ★
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/choose-stores")
    public BaseResponse<List<GetStoreRes>> getChooseStores(@RequestParam int addressId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            int userIdByAddressId = storeProvider.getUserIdByAddressId(addressId);

            if(userIdByJwt != userIdByAddressId)
                return new BaseResponse<>(WRONG_ADDRESSID); // 접근한 유저의 주소정보가 아닐때(addressId 틀린경우)


            UserCoordinate userCoordinate = storeProvider.getUserCoordinate(addressId);

            double userLatitude = userCoordinate.getUserLatitude();
            double userLongitude = userCoordinate.getUserLongitude();

            List<GetStoreRes> getStoreRes = storeProvider.getChooseStores(userIdByJwt, userLatitude, userLongitude);
            return new BaseResponse<>(getStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * (홈화면) ※비회원 골라먹는 맛집 목록 불러오기 API
     * [GET] /stores/choose-stores
     * @return BaseResponse<List<GetStoreRes>>
     * ★ 거리 20KM ★
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/choose-stores/no-login")
    public BaseResponse<List<GetStoreNoLoginRes>> getChooseStoresNoLogin(@RequestParam double latitude, @RequestParam double longitude) {
        try {
            if(latitude == 0)
                return new BaseResponse<>(EMPTY_LATITUDE);
            if(longitude == 0)
                return new BaseResponse<>(EMPTY_LONGITUDE);

            List<GetStoreNoLoginRes> getStoreNoLoginRes = storeProvider.getChooseStoresNoLogin(latitude, longitude);
            return new BaseResponse<>(getStoreNoLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * (홈화면) 이츠오리지널 목록 불러오기 API
     * [GET] /stores/eats-original-stores
     * @return BaseResponse<List<GetStoreRes>>
     * ★ 거리 20KM, 이츠오리지널 ★
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/eats-original-stores")
    public BaseResponse<List<GetStoreRes>> getEatsOriginalStores(@RequestParam int addressId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            int userIdByAddressId = storeProvider.getUserIdByAddressId(addressId);

            if(userIdByJwt != userIdByAddressId) {
                return new BaseResponse<>(WRONG_ADDRESSID); // 접근한 유저의 주소정보가 아닐때(addressId 틀린경우)
            }

            UserCoordinate userCoordinate = storeProvider.getUserCoordinate(addressId);

            double userLatitude = userCoordinate.getUserLatitude();
            double userLongitude = userCoordinate.getUserLongitude();

            List<GetStoreRes> getStoreRes = storeProvider.getEatsOriginalStores(userIdByJwt, userLatitude, userLongitude);
            return new BaseResponse<>(getStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * (홈화면) 우리동네 인기맛집 목록 불러오기 API
     * [GET] /stores/town-popular-stores
     * @return BaseResponse<List<GetStoreRes>>
     * ★ 거리 5km, 가게별 주문 수 상위 20개 (최소 3회 이상 -> 인기맛집 이므로)★
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/town-popular-stores")
    public BaseResponse<List<GetStoreRes>> getTownPopularStores(@RequestParam int addressId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            int userIdByAddressId = storeProvider.getUserIdByAddressId(addressId);

            if(userIdByJwt != userIdByAddressId) {
                return new BaseResponse<>(WRONG_ADDRESSID); // 접근한 유저의 주소정보가 아닐때(addressId 틀린경우)
            }

            UserCoordinate userCoordinate = storeProvider.getUserCoordinate(addressId);

            double userLatitude = userCoordinate.getUserLatitude();
            double userLongitude = userCoordinate.getUserLongitude();

            List<GetStoreRes> getStoreRes = storeProvider.getTownPopularStores(userIdByJwt, userLatitude, userLongitude);
            return new BaseResponse<>(getStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * (홈화면) 자주 주문한 맛집 목록 불러오기 API
     * [GET] /stores/frequent-order-stores
     * @return BaseResponse<List<GetStoreRes>>
     * ★ 거리 20km, 유저의 해당 가게 주문 횟수 상위 20개 (최소 1회 이상)★
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/frequent-order-stores")
    public BaseResponse<List<GetStoreRes>> getUserFrequentOrderStores(@RequestParam int addressId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            int userIdByAddressId = storeProvider.getUserIdByAddressId(addressId);

            if(userIdByJwt != userIdByAddressId) {
                return new BaseResponse<>(WRONG_ADDRESSID); // 접근한 유저의 주소정보가 아닐때(addressId 틀린경우)
            }

            UserCoordinate userCoordinate = storeProvider.getUserCoordinate(addressId);

            double userLatitude = userCoordinate.getUserLatitude();
            double userLongitude = userCoordinate.getUserLongitude();

            List<GetStoreRes> getStoreRes = storeProvider.getUserFrequentOrderStores(userIdByJwt, userLatitude, userLongitude);
            return new BaseResponse<>(getStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * (검색창)카테고리 검색 API
     * [GET] /stores/:categoryId
     * @return BaseResponse<List<GetStoreRes>>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/search/{categoryId}")
    public BaseResponse<List<GetStoreRes>> searchStoreByCategory(@PathVariable int categoryId, @RequestParam int addressId) {
        try {

            int userIdByJwt = jwtService.getUserIdx();

            int userIdByAddressId = storeProvider.getUserIdByAddressId(addressId);

            if(userIdByJwt != userIdByAddressId) {
                return new BaseResponse<>(WRONG_ADDRESSID); // 접근한 유저의 주소정보가 아닐때(addressId 틀린경우)
            }

            UserCoordinate userCoordinate = storeProvider.getUserCoordinate(addressId);

            double userLatitude = userCoordinate.getUserLatitude();
            double userLongitude = userCoordinate.getUserLongitude();

            if(categoryId >= 2 && categoryId <= 22) { // 2 ~ 22 한식 치킨 ....
                List<GetStoreRes> getStoreRes = storeProvider.searchStoreByCategoryAboutMenu(userIdByJwt, categoryId, userLatitude, userLongitude);
                return new BaseResponse<>(getStoreRes);
            } else { // 1. 포장, 23. 프랜차이즈, 24. 신규맛집, 25. 1인분
                String conditionLine = "";
                switch (categoryId) {
                    case 1 : conditionLine = "where Stores.takeOut = 1"; break;
                    case 23 : conditionLine = "where Stores.franchise = 1"; break;
                    case 24 : conditionLine = "where Stores.isNew = 1"; break;
                    case 25 : conditionLine = "where Stores.1serving = 1"; break;
                    default: return new BaseResponse<>(NOT_EXIST_CATEGORY);
                }

                List<GetStoreRes> getStoreRes = storeProvider.searchStoreByCategoryAboutProperty(userIdByJwt, conditionLine, userLatitude, userLongitude);
                return new BaseResponse<>(getStoreRes);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * (검색창)※비회원 카테고리 검색 API
     * [GET] /stores/:categoryId
     * @return BaseResponse<List<GetStoreRes>>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/search/no-login/{categoryId}")
    public BaseResponse<List<GetStoreNoLoginRes>> searchStoreByCategory(@PathVariable int categoryId, @RequestParam double latitude, @RequestParam double longitude) {
        try {
            if(categoryId >= 2 && categoryId <= 22) { // 2 ~ 22 한식 치킨 ....
                List<GetStoreNoLoginRes> getStoreNoLoginRes = storeProvider.searchStoreByCategoryAboutMenuNoLogin(categoryId, latitude, longitude);
                return new BaseResponse<>(getStoreNoLoginRes);
            } else { // 1. 포장, 23. 프랜차이즈, 24. 신규맛집, 25. 1인분
                String conditionLine = "";
                switch (categoryId) {
                    case 1 : conditionLine = "where Stores.takeOut = 1"; break;
                    case 23 : conditionLine = "where Stores.franchise = 1"; break;
                    case 24 : conditionLine = "where Stores.isNew = 1"; break;
                    case 25 : conditionLine = "where Stores.1serving = 1"; break;
                    default: return new BaseResponse<>(NOT_EXIST_CATEGORY);
                }

                List<GetStoreNoLoginRes> getStoreNoLoginReqs = storeProvider.searchStoreByCategoryAboutPropertyNoLogin(conditionLine, latitude, longitude);
                return new BaseResponse<>(getStoreNoLoginReqs);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * (홈화면) 가게 카테고리 목록 불러오기 API
     * [GET] /categories
     * @return BaseResponse<List<GetStoreCategoryRes>>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/categories")
    public BaseResponse<List<GetStoreCategoryRes>> getStoreCategories() {
        try {
            List<GetStoreCategoryRes> getStoreCategoryRes = storeProvider.getStoreCategories();
            return new BaseResponse<>(getStoreCategoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * (리뷰상세) 가게별 리뷰 목록 불러오기 API
     * [GET] /:storeId/reviews
     * @return BaseResponse<GetStoreReviewRes>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/{storeId}/reviews")
    public BaseResponse<GetStoreReviewRes> getStoreReviews(@PathVariable("storeId") int storeId) {
        try {
            StoreReviewScore storeReviewScore = storeProvider.getStoreReviewScore(storeId);
            // 가게 평점, 리뷰수

            List<ReviewOnly> reviewOnly = storeProvider.getOnlyReviews(storeId);
            // 리뷰들만 불러오기
            List<ReviewMenus> reviewMenus = new ArrayList<>();
            // [리뷰+메뉴들] List

            for(ReviewOnly r : reviewOnly) {
                ReviewMenus reviews = new ReviewMenus();

                String userName = r.getUserName().substring(0, 1); // 유저의 이름은 성 제외 *로 표시됨
                for(int i = 0; i < r.getUserName().length() - 1; i++)
                    userName += '*';

                reviews.setReviewId(r.getReviewId()); // [리뷰+메뉴들] 1개 생성 과정(1)
                reviews.setUserName(userName);
                reviews.setScore(r.getScore());
                reviews.setReviewContent(r.getReviewContent());
                reviews.setReviewImage(r.getReviewImage());
                reviews.setCreateTime(r.getCreatedAt());

                //----------------------------------------------- 리뷰내용을 객체에 먼저 담고 해당 리뷰의 orderId를 넘겨서
                List<MenuOnly> menuOnly = storeProvider.getOnlyMenus(r.getOrderId()); // 리뷰별 메뉴들을 받아오고
                List<String> menus = new ArrayList<>();

                for(MenuOnly m : menuOnly) {
                    menus.add(m.getMenuName()); // 메뉴들을 String List에 담아서
                }

                reviews.setMenus(menus); // [리뷰+메뉴들] 1개 생성 과정(2) -> 완료
                //-----------------------------------------------
                reviewMenus.add(reviews); // [리뷰+메뉴들] List에 완성된 것 추가
            }

            GetStoreReviewRes getStoreReviewRes = new GetStoreReviewRes( // 가게 정보 + [리뷰+메뉴들] List
                    storeReviewScore.getStoreId(),
                    storeReviewScore.getAvgScore(),
                    storeReviewScore.getReviewCount(),
                    reviewMenus
            );

            return new BaseResponse<>(getStoreReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * (메뉴 화면)메뉴 상세보기
     * [GET] /stores/{storeId}/{menuId}
     * @return BaseResponse<GetMenuRes>
     */

    /** 담당자 : Kiin */
    @GetMapping("{storeId}/{menuId}")
    public BaseResponse<GetMenuRes> getMenuDetail(@PathVariable("storeId") int storeId, @PathVariable("menuId") int menuId) {

        try {
            GetMenuRes getMenuRes = storeProvider.getMenuDetail(storeId,menuId);

            return new BaseResponse<>(getMenuRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 식당 상세보기
     * [GET] /stores/{storeId}
     * @return BaseResponse<GetStoreDetailRes>
     */

    /** 담당자 : Kiin */
    @Transactional(readOnly = true)
    @GetMapping("/{storeId}")
    public BaseResponse<GetStoreDetailRes> getStoreDetail(@PathVariable("storeId") int storeId) {
        try{
            jwtService.getUserIdx();
        }catch (BaseException exception){
            try {
                double latitude = 37.2862000000000000;
                double longitude = 127.17659;
                UserCoordinate userCoordinate = new UserCoordinate(latitude,longitude);
                GetStoreDetailRes getStoreDetailRes = storeProvider.getStoreDetail_noJWT(storeId, userCoordinate);

                return new BaseResponse<>(getStoreDetailRes);
            }
            catch (BaseException exception1){
                return new BaseResponse<>((exception1.getStatus()));
            }
        }
        // jwt토큰이 없을 경우에는 기본 ip를 gps  아이피로 간주하여 검색목록 탐색

        try {

            GetStoreDetailRes getStoreDetailRes = storeProvider.getStoreDetail(storeId, jwtService.getUserIdx());

            return new BaseResponse<>(getStoreDetailRes);
        }
        catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 담당자 : Kiin */
    @PostMapping("/{storeId}/orders")
    public BaseResponse<PostOrderRes> addOrder(@RequestBody PostOrderReq postOrderReq, @PathVariable("storeId") int storeId) {


        if(postOrderReq.getStoreRequest() == null){
            postOrderReq.setRiderRequest("문 앞에 두고 가주세요");
        }


        try {

            PostOrderRes result = storeService.addOrder(postOrderReq,storeId);

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 주문 취소 API
     * [PATCH] /stores/{orderId}/order
     * @return BaseResponse<Integer>
     */

    /** 담당자 : Kiin */
    @Transactional
    @PatchMapping("/{orderId}/orders")
    public BaseResponse<String> cancelOrder(@PathVariable("orderId") int orderId) {

        try {
            storeService.cancelOrder(orderId);
            String result = "주문 취소가 완료되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * (매장/원산지 정보)식당 정보 상세 보기 API
     * [GET] /stores/{storeId}/detail
     * @return BaseResponse<GetStoreInfo>
     */

    /** 담당자 : Kiin */
    @Transactional(readOnly = true)
    @GetMapping("/{storeId}/detail")
    public BaseResponse<GetStoreInfo> getStoreInfo(@PathVariable("storeId") int storeId) {

        try {
            GetStoreInfo getStoreInfo = storeProvider.getStoreInfo(storeId);

            return new BaseResponse<>(getStoreInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * (검색창)키워드 검색 API
     * [GET] /stores/search
     * @return BaseResponse<List<String>>
     */

    /** 담당자 : Kiin */
    @Transactional(readOnly = true)
    @GetMapping("/search")
    public BaseResponse<GetSearchResults> getSearchInfo(@RequestParam("keyword") String keyword) {
        try{
            jwtService.getUserIdx();
        }catch (BaseException exception){
            try {
                double latitude = 37.2862000000000000;
                double longitude = 127.17659;
                List<Integer> getSearchInfo = storeProvider.getSearchInfo(keyword);
                // 키워드 검색으로 식당 목록들을 받아옴
                GetSearchResults getSearchResults = storeProvider.getSearchResults_noJWT(getSearchInfo,latitude,longitude);
                // 검색 된 식당 중에서 20km 이내 식당을 필터링하고 정보 출력
                getSearchResults.setKeyword(keyword);

                return new BaseResponse<>(getSearchResults);
            }
            catch (BaseException exception1){
                return new BaseResponse<>((exception1.getStatus()));
            }
        }



        try {

            UserCoordinate userCoordinate = storeProvider.getUserCoordinate_(jwtService.getUserIdx());

            double userLatitude = userCoordinate.getUserLatitude();
            double userLongitude = userCoordinate.getUserLongitude();

            List<Integer> getSearchInfo = storeProvider.getSearchInfo(keyword);
            // 키워드 검색으로 식당 목록들을 받아옴
            GetSearchResults getSearchResults = storeProvider.getSearchResults(getSearchInfo,userLatitude,userLongitude, jwtService.getUserIdx());
            // 검색 된 식당 중에서 20km 이내 식당을 필터링하고 정보 출력
            getSearchResults.setKeyword(keyword);

            return new BaseResponse<>(getSearchResults);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 별 쿠폰 검색 API
     * [GET] /stores/{storeId}/coupons
     * @return BaseResponse<List<String>>
     */

    /** 담당자 : Kiin */
    @Transactional(readOnly = true)
    @GetMapping("/{storeId}/coupons")
    public BaseResponse<List<GetCouponRes>> getSCouponRes(@PathVariable("storeId") String storeId) {

        try {
            List<GetCouponRes> getCouponResList = storeProvider.getCouponRes(storeId);

            return new BaseResponse<>(getCouponResList);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }






}
