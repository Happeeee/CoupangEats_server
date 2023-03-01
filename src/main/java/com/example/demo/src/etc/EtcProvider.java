package com.example.demo.src.etc;

import com.example.demo.config.BaseException;
import com.example.demo.src.etc.model.*;
import com.example.demo.src.store.StoreDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class EtcProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EtcDao etcDao;
    private final JwtService jwtService;

    @Autowired
    public EtcProvider(EtcDao etcDao, JwtService jwtService) {
        this.etcDao = etcDao;
        this.jwtService = jwtService;
    }

    public List<GetEventRes> getEventBanners() throws BaseException {
        try {
            List<GetEventRes> getEventRes = etcDao.getEventBanners();
            return getEventRes;
        } catch (Exception exception) {
            logger.error("App - getEventBanners Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetTitleRes> getNoticeList() throws BaseException {
        try {
            List<GetTitleRes> notices = etcDao.getNoticeList();
            return notices;
        } catch (Exception exception) {
            logger.error("App - getNoticeList Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public List<GetNoticeRes> getNoticeContent(int noticeId) throws BaseException{
        try {
            List<GetNoticeRes> notices = etcDao.getNoticeContent(noticeId);
            return notices;
        } catch (Exception exception) {
            logger.error("App - getNoticeList Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetAgreeRes> getAgree() throws BaseException{
        try{
            return etcDao.getAgree();
        }catch (Exception exception){
            logger.error("App - getAgree Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPolicyRes getPolicy() throws BaseException{
        try{
            return etcDao.getPolicy();
        }catch (Exception exception){
            logger.error("App - getPolicy Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}