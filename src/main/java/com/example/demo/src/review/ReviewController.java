package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.src.store.StoreService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.EMPTY_ORDERID;
import static com.example.demo.config.BaseResponseStatus.POST_REVIEWS_EMPTY_SCORE;

@RestController
@RequestMapping("/coupang-eats/reviews")
public class ReviewController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService) {
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * (리뷰) 리뷰 작성
     * [POST] /
     * @return BaseResponse<PostReviewRes>
     */
    /** 담당자 : Happy */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostReviewRes> postReview(@RequestBody PostReviewReq postReviewReq) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            if(postReviewReq.getOrderId() == null)
                return new BaseResponse<>(EMPTY_ORDERID);

            if(postReviewReq.getScore() == 0)
                return new BaseResponse<>(POST_REVIEWS_EMPTY_SCORE);

            PostReviewRes postReviewRes = reviewService.postReview(userIdByJwt, postReviewReq);
            return new BaseResponse<>(postReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
