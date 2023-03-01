package com.example.demo.src.etc;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.etc.model.*;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.POST_USERS_EXISTS_PHONE_NUMBER;

@RestController
@RequestMapping("/coupang-eats/etc")
public class EtcController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final EtcProvider etcProvider;
    @Autowired
    private final EtcService etcService;
    @Autowired
    private final JwtService jwtService;

    public EtcController(EtcProvider etcProvider, EtcService etcService, JwtService jwtService) {
        this.etcProvider = etcProvider;
        this.etcService = etcService;
        this.jwtService = jwtService;
    }

    /**
     * 담당자 : Happy
     * 이벤트 배너 불러오기 API
     * [GET] /event-banners
     */
    @ResponseBody
    @GetMapping("/event-banners")
    public BaseResponse<List<GetEventRes>> getEventBanners() {
        try {
            List<GetEventRes> getEventRes = etcProvider.getEventBanners();
            return new BaseResponse<>(getEventRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 담당자 : Kiin
     * (공지사항) 공지사항 리스트 조회 API
     * [GET] /notice
     */
    @GetMapping("/notices")
    public BaseResponse<List<GetTitleRes>> getNoticeList() {

        try {
            List<GetTitleRes> notices = etcProvider.getNoticeList();
            return new BaseResponse<>(notices);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /** 담당자 : kiin */
    @GetMapping("/notices/{noticeId}")
    public BaseResponse<List<GetNoticeRes>> getNoticeContent(@PathVariable("noticeId") int noticeId) {

        try {
            List<GetNoticeRes> notices = etcProvider.getNoticeContent(noticeId);
            return new BaseResponse<>(notices);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /** 담당자 : kiin */
    @GetMapping("/agreements")
    public BaseResponse<List<GetAgreeRes>> getAgree() {

        try {
            List<GetAgreeRes> agrees = etcProvider.getAgree();
            return new BaseResponse<>(agrees);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /** 담당자 : kiin */
    @GetMapping("/policies")
    public BaseResponse<GetPolicyRes> getPolicy(){

        try {
            GetPolicyRes policy = etcProvider.getPolicy();
            return new BaseResponse<>(policy);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}