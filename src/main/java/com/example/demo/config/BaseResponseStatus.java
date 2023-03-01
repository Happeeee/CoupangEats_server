package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : 형식적 validation
     */

    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    POST_USERS_AGREE(false, 2002, "약관에 동의해주세요"),
    GET_SEARCH_ERROR(false, 2003, "검색 결과가 없습니다."),

    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_INVALID_NAME(false, 2018, "이름에는 숫자,영어,한글이 혼용될 수 없습니다."),
    POST_USERS_INVALID_PHONE_NUMBER(false, 2019,    "번호만 입력해주세요"),
    POST_USERS_INVALID_PASSWORD(false, 2020, "비밀번호 형식을 확인해주세요"),
    POST_USERS_EMPTY_TEXT(false, 2024, "이메일을 입력해주세요."),
    POST_USERS_EMPTY_PROD_NAME(false, 2025, "상품 이름을 입력해주세요."),
    POST_REVIEWS_EMPTY_TEXT(false, 2026, "리뷰 내용을 입력해주세요."),
    POST_USERS_NOT_CORRESPOND_PASSWD(false, 2027, "새 비밀번호가 일치하지 않습니다."),

    POST_USERS_INVALID_FORMAT_SERIAL(false, 2050, "8자리에서 16자리를 입력해주세요."),

    GET_STORES_EMPTY_ADDRESSID(false, 2100, "주소 정보(addressId)를 입력해주세요"),
    EMPTY_ORDERID(false, 2101, "주문 정보(orderId)를 입력해주세요"),
    POST_REVIEWS_EMPTY_SCORE(false, 2102, "별점을 입력해주세요"),
    EMPTY_LATITUDE(false, 2103, "위도를 입력해주세요"),
    EMPTY_LONGITUDE(false, 2104, "경도를 입력해주세요"),
    POST_PAYMENT_EMPTY_BANK(false, 2105, "은행 정보(bankId)를 입력해주세요"),
    POST_PAYMENT_EMPTY_TYPE(false, 2106, "결제 수단 타입(CREDIT/ACCOUNT)를 입력해주세요"),
    POST_PAYMENT_WRONG_TYPE(false, 2107, "결제 수단 타입은 CREDIT / ACCOUNT 중 하나를 입력해주세요"),
    POST_PAYMENT_EMPTY_NUMBER(false, 2108, "계좌/카드 번호를 입력해주세요"),
    POST_PAYMENT_WRONG_CREDIT(false, 2109, "카드 번호는 숫자 16자리를 입력해주세요"),
    POST_PAYMENT_WRONG_ACCOUNT(false, 2110, "계좌 번호는 숫자 12자리를 입력해주세요"),
    POST_ADDRESS_EMPTY_NAME(false, 2111, "주소 이름을 입력해주세요"),
    POST_ADDRESS_EMPTY_DETAIL(false, 2112, "세부 주소를 입력해주세요"),
    // Common






    /**
     * 3000 : 의미적 validation
     */





    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    NOT_EXIST_SELECTED_USER(false, 3001, "해당 주문 정보가 존재하지 않습니다"),
    NOT_EXIST_SELECTED_USER_ORDER(false, 3002, "해당  정보가 존재하지 않습니다"),
    NOT_EXIST_SELECTED_USER_COUPONS(false, 3003, "보유하신 쿠폰이 존재하지 않습니다."),
    NOT_EXIST_SELECTED_USER_PAYMENTS(false, 3006, "결제수단 정보가 존재하지 않습니다."),
    NOT_EXIST_SELECTED_USER_ADDRESS(false, 3007, "주소 정보가 존재하지 않습니다."),
    NOT_AVAILABLE_SELECTED_PROD_ID(false, 3008, "판매가 중단 된 상품입니다."),
    POST_USERS_EXISTS_EMAIL(false,3009,"중복된 이메일입니다."),
    POST_USERS_EXISTS_ID(false,3010,"유저 정보를 불러올 수 없습니다."),
    POST_SELLERS_EXISTS_ID(false,3011,"판매자 정보를 불러올 수 없습니다."),
    POST_PROD_EXISTS_ID(false,3012,"상품 정보를 불러올 수 없습니다."),
    POST_PROD_NOT_UPDATED(false,3013,"업데이트 된 내용이 없습니다."),


    // [POST] /users
    DUPLICATED_EMAIL(false, 3014, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3015,"없는 아이디거나 비밀번호가 틀렸습니다."),
    POST_USERS_PASSWD_NOT_CORRECT(false, 3016, "결제 비밀번호가 올바르지 않습니다."),
    INVALID_JWT(false, 3017, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,3018,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 3019, "유저 아이디 값을 확인해주세요."),


    // [POST] /users



    POST_USERS_EXISTS_PHONE_NUMBER(false,3020,"이미 등록된 번호입니다."),
    POST_USERS_EXISTS_SERIAL(false, 3021, "이미 등록된 시리얼 번호입니다."),
    POST_USERS_INVALID_SERIAL(false, 3022, "입력하신 등록 번호가 정확하지 않습니다. 확인 후 다시 입력해주세요."),
    POST_STORE_INVALID_STATUS(false, 3050, "영업을 중지한 식당입니다."),
    POST_STORE_INVALID_INFO(false, 3051, "식당에 대한 정보가 없습니다."),
    POST_MENU_INVALID_STATUS(false, 3052, "품절된 메뉴입니다."),
    POST_MENU_INVALID_ID(false, 3053, "해당 메뉴에 대한 정보가 존재하지 않습니다."),
    POST_STORE_NO_COUPON(false, 3054, "해당 가게에서 사용 가능한 쿠폰이 없습니다."),
    PATCH_ORDER_ALREADY_CANCELED(false, 3055, "이미 취소된 주문입니다."),
    USER_INVALID_COUPON(false, 3056, "이미 사용한 쿠폰이거나 유효기간 지난 쿠폰입니다."),
    USER_NO_EXIST_COUPON(false, 3057, "보유하신 쿠폰이 없습니다."),
    USER_NO_EXIST_ORDER(false, 3058, "주문 정보가 없습니다."),
    POST_NO_EXIST_SERIAL(false, 3059, "잘못된 쿠폰 번호입니다."),
    NOT_EXIST_USER(false, 3113, "존재하지 않는 유저 입니다."),
    WRONG_ADDRESSID(false, 3100, "해당 유저의 주소 정보가 아닙니다"),
    NOT_EXIST_STORE(false, 3101, "존재하지 않는 가게 입니다."),

    NOT_USERORDER(false, 3102, "해당 유저의 주문이 아닙니다"),
    POST_REVIEWS_EXIST_REVIEW(false, 3103, "이미 작성된 리뷰가 있습니다"),
    NOT_EXIST_USER_REVIEW(false, 3104, "해당 유저가 작성한 리뷰가 없습니다"),
    NOT_EXIST_CATEGORY(false, 3105, "존재하지 않는 카테고리입니다."),
    NOT_EXIST_ADDRESS(false, 3106, "존재하지 않는 주소정보 입니다."),
    NOT_EXIST_ORDER(false, 3107, "존재하지 않는 주문정보 입니다."),
    EXIST_PAYMENT(false, 3108, "이미 등록된 결제정보 입니다."),
    NOT_EXIST_BANK(false, 3109, "존재하지 않는 은행정보 입니다."),
    NOT_USER_FAVORITE_STORE(false, 3110, "유저가 즐겨찾기 하지 않는 가게가 포함 되어있습니다"),
    EXIST_USER_HOME_ADDRESS(false, 3111, "이미 추가된 집 주소가 있습니다(유저당 집주소 하나만)"),
    EXIST_USER_OFFICE_ADDRESS(false, 3112, "이미 추가된 회사 주소가 있습니다(유저당 회사주소 하나만)"),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    MODIFY_FAIL_USERINFO(false, 4002, "유저 정보 변경에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_REVIEW_TEXT(false,4015,"리뷰 내용 수정 실패"),
    MODIFY_FAIL_REVIEW_DELETE(false,4016,"삭제된 리뷰입니다."),
    MODIFY_FAIL_STATUS(false,4017,"상태 변경 실패"),
    MODIFY_FAIL_SHIP_OPT(false,4018,"배송옵션 변경 실패"),


    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),
    FAILED_TO_KAKAO_LOGIN(false, 4013, "카카오 로그인 실패");



    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
