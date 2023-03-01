

package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.ReviewMenu;
import com.example.demo.src.store.StoreDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.NOT_EXIST_ORDER;

@Service
public class ReviewProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewDao reviewDao;
    private final JwtService jwtService;

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public int getUserIdByOrderId(int orderId) throws BaseException {
        try {
            return reviewDao.getUserIdByOrderId(orderId);
        } catch (Exception exception) {
            logger.error("App - getUserIdByOrderId Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistReview(int userId, int orderId) throws BaseException {
        try {
            return reviewDao.isExistReview(userId, orderId);
        } catch (Exception exception) {
            logger.error("App - isExistReview Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistOrder(int orderId) throws BaseException {
        try {
            return reviewDao.isExistOrder(orderId);
        } catch (Exception exception) {
            logger.error("App - isExistOrder Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<ReviewMenu> getReviewMenus(int orderId) throws BaseException {

        if(isExistOrder(orderId) == 0)
            throw new BaseException(NOT_EXIST_ORDER);

        try {
            List<ReviewMenu> reviewMenus = reviewDao.getReviewMenus(orderId);
            return reviewMenus;
        } catch (Exception exception) {
            logger.error("App - getReviewMenus Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


}