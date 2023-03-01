package com.example.demo.src.login;


import com.example.demo.config.BaseException;
import com.example.demo.src.login.model.KakaoUser;
import com.example.demo.src.login.model.PostKakaoUserReq;
import com.example.demo.src.login.model.PostKakaoUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.FAILED_TO_KAKAO_LOGIN;

@Service
public class OAuthProvider {

    @Autowired
    private final OAuthDao oAuthDao;
    @Autowired
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OAuthProvider(OAuthDao oAuthDao, JwtService jwtService) {
        this.oAuthDao = oAuthDao;
        this.jwtService = jwtService;
    }
    public int isExistedKakaoUser(String kakaoId) throws BaseException {
        try {
            return oAuthDao.isExistedKakaoUser(kakaoId);
        } catch (Exception exception) {
            logger.error("App - isExistedKakaoUser Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostKakaoUserRes kakaoLogin(PostKakaoUserReq postKakaoUserReq) throws BaseException {
        try {
            KakaoUser kakaoUser = oAuthDao.getKakaoId(postKakaoUserReq);

            String kakaoId = String.valueOf(kakaoUser.getKakaoId());

            if(postKakaoUserReq.getKakaoId().equals(kakaoId)) {
                int userId = kakaoUser.getUserId();
                String jwt = jwtService.createJwt(userId);
                return new PostKakaoUserRes("카카오 로그인 성공!" ,jwt, userId);
            } else {
                throw new BaseException(FAILED_TO_KAKAO_LOGIN);
            }
        } catch (Exception exception) {
            logger.error("App - kakaoLogin Provider Error", exception);
            throw new BaseException(FAILED_TO_KAKAO_LOGIN);
        }


    }






}