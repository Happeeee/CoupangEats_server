package com.example.demo.src.login;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.login.model.PostKakaoUserReq;
import com.example.demo.src.login.model.PostKakaoUserRes;
import com.example.demo.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@RequestMapping("/oauth")
@Slf4j
public class OAuthController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final OAuthService oAuthService;
    @Autowired
    private final OAuthProvider oAuthProvider;
    @Autowired
    private final JwtService jwtService;



    public OAuthController(OAuthProvider oAuthProvider, OAuthService oAuthService, JwtService jwtService) {
        this.oAuthProvider = oAuthProvider;
        this.oAuthService = oAuthService;
        this.jwtService = jwtService;
    }

    /** 담당자 : Happy */
    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<String> kakaoLoginInfo(@RequestParam String code, HttpSession session) {
        String access_Token = oAuthService.getKakaoAccessToken(code);
        HashMap<String, Object> userInfo = oAuthService.getUserInfo(access_Token);

        return new BaseResponse<>(access_Token);
    }

    /** 담당자 : Happy */
    @ResponseBody
    @PostMapping("/kakao")
    public BaseResponse<PostKakaoUserRes> kakaoLogin() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String access_Token = request.getHeader("KAKAO-ACCESS-TOKEN");
            HashMap<String, Object> userInfo = oAuthService.getUserInfo(access_Token);

            PostKakaoUserReq postKakaoUserReq = new PostKakaoUserReq(
                    String.valueOf(userInfo.get("kakaoId")), // object를 string 으로 형변환
                    String.valueOf(userInfo.get("email")),
                    String.valueOf(userInfo.get("userName"))
            );

            System.out.println(postKakaoUserReq.getKakaoId());
            System.out.println(postKakaoUserReq.getUserName());
            System.out.println(postKakaoUserReq.getEmail());


            if(oAuthProvider.isExistedKakaoUser(postKakaoUserReq.getKakaoId()) == 1) {
                PostKakaoUserRes postKakaoUserRes = oAuthProvider.kakaoLogin(postKakaoUserReq);
                return new BaseResponse<>(postKakaoUserRes);
            } else {
                PostKakaoUserRes postKakaoUserRes = oAuthService.createKakaoUser(postKakaoUserReq);
                return new BaseResponse<>(postKakaoUserRes);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}