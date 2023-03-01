package com.example.demo.src.user;

import com.example.demo.src.review.ReviewProvider;
import com.example.demo.src.review.model.ReviewMenu;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.src.store.model.GetOrderInfo;
import com.example.demo.src.store.model.DTO.UserCoordinate;
import com.example.demo.src.store.model.DTO.UserFavorites;
import com.example.demo.src.user.model.DTO.AddCoupon;
import com.example.demo.src.user.model.DTO.MyReviewInfo;
import com.example.demo.src.user.model.DTO.UserAddresses;
import com.example.demo.src.user.model.DTO.UserPayments;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/coupang-eats/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StoreProvider storeProvider;
    private final UserProvider userProvider;

    private final UserService userService;

    private final JwtService jwtService;
    private final ReviewProvider reviewProvider;

    /**
     * (즐겨찾기 하트버튼) 즐겨찾기 버튼 누르기 (추가/해제) API
     * [POST] /users/favorites/:storeId
     * @return BaseResponse<String>
     */

    /** 담당자 : Happy */

    @ResponseBody
    @PostMapping("/favorites/{storeId}")
    public BaseResponse<String> clickFavoriteButton(@PathVariable int storeId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            boolean isFirst = (userProvider.isFirstFavorite(userIdByJwt, storeId) == 0) ? true : false;
            // 유저가 해당 가게를 즐겨찼기 했던 기록이 존재하면 1 아니면 0

            if(isFirst) { // 처음이라면 추가
                userService.addFavorite(userIdByJwt, storeId);
                return new BaseResponse<>("즐겨찾기 (최초)추가 성공");
            } else { // 처음이 아닐경우 1. 즐겨찾기에 추가 되어있는 경우 : 해제 / 2. 해제 되어있는경우 : 다시 추가
                String result = "";

                if(userProvider.getFavoriteStatus(userIdByJwt, storeId) == 0)  result = "즐겨찾기 재 추가 성공";
                else result = "즐겨찾기 해제 성공";

                userService.reOrCancelFavorite(userIdByJwt, storeId);
                return new BaseResponse<>(result);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * (즐겨찾기) 내 즐겨찾기 목록
     * [GET] /users/favorites?addressId=1
     * @return BaseResponse<List<GetUserFavoriteRes>>
     * ★ 거리 제한x, 주문횟수 순 정렬, 주문횟수 명시★
     */

    /** 담당자 : Happy */

    @ResponseBody
    @GetMapping("/favorites")
    public BaseResponse<GetUserFavoriteRes> getUserFavorites(@RequestParam int addressId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            int userIdByAddressId = storeProvider.getUserIdByAddressId(addressId);

            if(userIdByJwt != userIdByAddressId) {
                return new BaseResponse<>(WRONG_ADDRESSID); // 접근한 유저의 주소정보가 아닐때(addressId 틀린경우)
            }

            UserCoordinate userCoordinate = storeProvider.getUserCoordinate(addressId);

            double userLatitude = userCoordinate.getUserLatitude();
            double userLongitude = userCoordinate.getUserLongitude();

            int userFavoriteCount = userProvider.getFavoriteCount(userIdByJwt);
            List<UserFavorites> userFavoriteRes = storeProvider.getUserFavorites(userIdByJwt, userLatitude, userLongitude);

            GetUserFavoriteRes getUserFavoriteRes = new GetUserFavoriteRes(userFavoriteCount, userFavoriteRes);
            return new BaseResponse<>(getUserFavoriteRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * (즐겨찾기) 즐겨찾기 해제(수정)
     * [PATCH] /users/favorites
     * @return BaseResponse<String>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @PatchMapping("/favorites")
    public BaseResponse<String> patchUserFavorites(@RequestBody PatchFavoriteReq patchFavoriteReq) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            userService.patchUserFavorites(userIdByJwt, patchFavoriteReq);
            return new BaseResponse<>("즐겨찾기 해제 성공");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * (My eats) 내 정보 불러오기
     * [POST] /users/my-eats
     * @return BaseResponse<List<GetMyEatsRes>>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/my-eats")
    public BaseResponse<GetMyEatsRes> getMyEats() {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            GetMyEatsRes getMyEatsRes = userProvider.getMyEats(userIdByJwt);
            return new BaseResponse<>(getMyEatsRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * (유저 주소) 유저 주소 추가 API
     * [POST] /addresses
     * @return BaseResponse<PostAddressRes>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @PostMapping("/addresses")
    public BaseResponse<PostAddressRes> postUserAddress(@RequestBody PostAddressReq postAddressReq) {
        try {
            if(postAddressReq.getName() == null)
                return new BaseResponse<>(POST_ADDRESS_EMPTY_NAME);

            if(postAddressReq.getDetail() == null)
                return new BaseResponse<>(POST_ADDRESS_EMPTY_DETAIL);

            if(postAddressReq.getLatitude() == 0)
                return new BaseResponse<>(EMPTY_LATITUDE);

            if(postAddressReq.getLongitude() == 0)
                return new BaseResponse<>(EMPTY_LONGITUDE);

            int userIdByJwt = jwtService.getUserIdx();

            PostAddressRes postAddressRes = userService.postUserAddress(userIdByJwt, postAddressReq);
            return new BaseResponse<>(postAddressRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * (유저 주소) 유저 주소 삭제 API
     * [PATCH] /addresses
     * @return BaseResponse<String>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @PatchMapping("/addresses")
    public BaseResponse<String> patchUserAddress(@RequestBody PatchAddressReq patchAddressReq) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            userService.patchUserAddress(userIdByJwt, patchAddressReq);
            return new BaseResponse<>("주소 삭제 성공");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * (유저 주소) 유저의 주소정보 불러오기 API
     * [GET] /addresses
     * @return BaseResponse<GetUserAddressRes>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/addresses")
    public BaseResponse<List<GetUserAddressRes>> getUserAddresses() {
        try {
            int userIdByJwt = jwtService.getUserIdx();


            List<UserAddresses> userAddresses = userProvider.getUserAddresses(userIdByJwt); // 일단 다 가져와서
            List<GetUserAddressRes> getUserAddressResList = new ArrayList<>();


            for(UserAddresses u : userAddresses) {
                GetUserAddressRes getUserAddressRes = new GetUserAddressRes();
                if(u.getName().equals("HOME"))  {
                    getUserAddressRes.setAddressType("HOME");
                } else if(u.getName().equals("OFFICE")) {
                    getUserAddressRes.setAddressType("OFFICE");
                } else {
                    getUserAddressRes.setAddressType("ETC");
                }

                getUserAddressRes.setAddressId(u.getAddressId());
                getUserAddressRes.setName(u.getName());
                getUserAddressRes.setDetail(u.getDetail());

                getUserAddressResList.add(getUserAddressRes);
            }

            return new BaseResponse<>(getUserAddressResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * (주문, 리뷰) 유저가 작성한 리뷰 보기
     * [GET] /my-review
     * @return BaseResponse<GetUserReviewRes>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/my-review")
    public BaseResponse<GetUserReviewRes> getUserReview(@RequestParam int orderId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            List<ReviewMenu> reviewMenus = reviewProvider.getReviewMenus(orderId);
            List<String> menus = new ArrayList<>();

            for(ReviewMenu r : reviewMenus)
                menus.add(r.getMenuName());

            MyReviewInfo myReviewInfo  = userProvider.getUserReview(userIdByJwt, orderId);

            GetUserReviewRes getUserReviewRes = new GetUserReviewRes(
                    myReviewInfo.getStoreName(),
                    myReviewInfo.getScore(),
                    myReviewInfo.getReviewImage(),
                    myReviewInfo.getReviewContent(),
                    myReviewInfo.getCreateTime(),
                    myReviewInfo.getGoodCount(),
                    menus
            );

            return new BaseResponse<>(getUserReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * (결제관리) 결제수단 추가
     * [POST] /payments
     * @return BaseResponse<PostPaymentRes>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @PostMapping("/payments")
    public BaseResponse<PostPaymentRes> postPayment(@RequestBody PostPaymentReq postPaymentReq) {
        try {
            if(postPaymentReq.getBankId() == null)
                return new BaseResponse<>(POST_PAYMENT_EMPTY_BANK);

            if(postPaymentReq.getType() == null)
                return new BaseResponse<>(POST_PAYMENT_EMPTY_TYPE);

            if(!postPaymentReq.getType().equals("CREDIT") & !postPaymentReq.getType().equals("ACCOUNT"))
                return new BaseResponse<>(POST_PAYMENT_WRONG_TYPE);

            if(postPaymentReq.getNumber() == null)
                return new BaseResponse<>(POST_PAYMENT_EMPTY_NUMBER);

            if(postPaymentReq.getType().equals("CREDIT")) { // 카드는 숫자 16자리
                if(!isRegexCredit(postPaymentReq.getNumber()))
                    return new BaseResponse<>(POST_PAYMENT_WRONG_CREDIT);

                char[] creditArray = postPaymentReq.getNumber().substring(8 ,16).toCharArray();
                creditArray[0] = '*'; creditArray[1] = '*'; creditArray[2] = '*'; creditArray[3] = '*'; creditArray[7] = '*'; // 암호화 간단 구현
                postPaymentReq.setNumber(String.valueOf(creditArray));
            }

            if(postPaymentReq.getType().equals("ACCOUNT")) { // 계좌는 숫자 12자리
                if(!isRegexAccount(postPaymentReq.getNumber()))
                    return new BaseResponse<>(POST_PAYMENT_WRONG_ACCOUNT);

                char[] accountArray = postPaymentReq.getNumber().substring(4 ,12).toCharArray();
                accountArray[0] = '*'; accountArray[1] = '*'; accountArray[2] = '*'; accountArray[3] = '*';
                postPaymentReq.setNumber(String.valueOf(accountArray));
            }

            int userIdByJwt = jwtService.getUserIdx();

            PostPaymentRes postPaymentRes = userService.postPayment(userIdByJwt, postPaymentReq);
            return new BaseResponse<>(postPaymentRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * (My이츠)결제관리 페이지 불러오기 API
     * [GET] /users/payments
     * @return BaseResponse<List<GetPaymentManageRes>>
     */

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/payments")
    public BaseResponse<GetUserPaymentRes> getPaymentManage(){
        try {
            int userIdByJwt = jwtService.getUserIdx();

            // couPayMoney는 user 테이블에 있으니 따로 가져오고
            int userCouPayMoney = userProvider.getuserCouPayMoney(userIdByJwt);
            // 유저의 결제 정보 받아와서
            List<UserPayments> userPayments = userProvider.getUserPayments(userIdByJwt);

            List<UserPayments> cardList = new ArrayList<>();
            List<UserPayments> accountList = new ArrayList<>();

            for(UserPayments obj : userPayments) { // 카드라면 cardList에 계좌라면 accountList에 담기
                if(obj.getType().equals("CREDIT")) {
                    cardList.add(obj);
                } else {
                    accountList.add(obj);
                }
            }

            GetUserPaymentRes GetUserPaymentRes = new GetUserPaymentRes();
            GetUserPaymentRes.setUserCouPayMoney(userCouPayMoney); // 필요한 정보들 반환용 Res 객체에 담아서 반환
            GetUserPaymentRes.setCardList(cardList);
            GetUserPaymentRes.setAccountList(accountList);

            return new BaseResponse<>(GetUserPaymentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @GetMapping("/view-upload")
    public String findImg() {
        String imgPath = userService.getThumbnailPath("3b96d468-0427-4b9b-b0d5-19c4352a705f-moon.jpeg");
        return imgPath;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        userService.upload(multipartFile);
        return "success";
    }
    @Transactional(readOnly = true)
    @GetMapping("/orders/{orderId}")
    public BaseResponse<GetReceiptInfo> getReceiptInfo(@PathVariable("orderId") int orderId) {

        try {
            GetReceiptInfo getReceiptInfo = userProvider.   getReceiptInfo(orderId);
            return new BaseResponse<>(getReceiptInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    @GetMapping("/{userId}/orders")
    public BaseResponse<List<GetOrderInfo>> getOrdersInfo(@PathVariable("userId") int userId) {

        try {
            List<GetOrderInfo> getOrderInfo = userProvider.getOrdersInfo(userId);
            return new BaseResponse<>(getOrderInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /users
     *
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        if (postUserReq.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if (!isRegexEmail(postUserReq.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        if (!isRegexPassword(postUserReq.getPassword())) {
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        if (!isRegexPhoneNumber(postUserReq.getPhoneNumber())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }
        if (!postUserReq.isAgree()) {
            return new BaseResponse<>(POST_USERS_AGREE);
        }
        try {

            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 담당자 : Kiin */
    @Transactional(readOnly = true)
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);

            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {

            return new BaseResponse<>(exception.getStatus());
        }
    }

    /** 담당자 : Kiin */
    @GetMapping("/checkEmail")
    public BaseResponse<String> checkEmail(@RequestBody PostLoginReq postLoginReq) {
        try {
            if(userProvider.checkEmail(postLoginReq.getEmail()) ==1){
                throw new BaseException(POST_USERS_EXISTS_EMAIL);
            }
            String result = "사용 가능한 이메일입니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 담당자 : Kiin */
    @GetMapping("/checkNumber")
    public BaseResponse<String> checkNumber(@RequestBody PostUserReq postUserReq) {

        try {
            if(userProvider.checkPhoneNumber(postUserReq.getPhoneNumber()) ==1){
                throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
            }
            String result = "사용 가능한 번호입니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 담당자 : Kiin */
    @GetMapping("/{userId}/coupons")
    public BaseResponse<List<GetCouponRes>> checkCouponList(@PathVariable("userId") int userId) {

        try {
            List<GetCouponRes> getCouponResList = userProvider.getCoupons(userId);
            return new BaseResponse<>(getCouponResList);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 담당자 : Kiin */
    @PostMapping("/coupons")
    public BaseResponse<GetCouponRes> addCoupon(@RequestBody AddCoupon addCoupon) {

        try {
            GetCouponRes getCouponResList = userService.addCoupon(addCoupon.getSerialNumber());
            return new BaseResponse<>(getCouponResList);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
