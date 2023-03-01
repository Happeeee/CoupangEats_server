package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.src.store.StoreDao;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewProvider reviewProvider;
    private final ReviewDao reviewDao;
    private final JwtService jwtService;
    @Autowired
    public ReviewService(ReviewProvider reviewProvider, ReviewDao reviewDao, JwtService jwtService) {
        this.reviewProvider = reviewProvider;
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public PostReviewRes postReview(int userId, PostReviewReq postReviewReq) throws BaseException {
        // 주문 id 가 해당 유저의 것이 아닐때
        if(userId != reviewProvider.getUserIdByOrderId(postReviewReq.getOrderId()))
            throw new BaseException(NOT_USERORDER);
        // 이미 작성된 리뷰가 있을 때
        if(reviewProvider.isExistReview(userId, postReviewReq.getOrderId()) == 1)
            throw new BaseException(POST_REVIEWS_EXIST_REVIEW);

        try {
            int reviewId = reviewDao.postReview(userId, postReviewReq);
            return new PostReviewRes(reviewId);
        } catch (Exception exception) {
            logger.error("App - postReview Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}