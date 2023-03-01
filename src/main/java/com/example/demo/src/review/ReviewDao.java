package com.example.demo.src.review;

import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.ReviewMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int postReview(int userId, PostReviewReq postReviewReq) {
        String postReviewQuery = "insert into Reviews(userId, orderId, score, reviewImage, reviewContent) values (?,?,?,?,?)";
        Object[] postReviewParams = new Object[] {
                userId,
                postReviewReq.getOrderId(),
                postReviewReq.getScore(),
                postReviewReq.getReviewImage(),
                postReviewReq.getReviewContent()
        };

        this.jdbcTemplate.update(postReviewQuery, postReviewParams);

        String lastInsertIdQuery = "select last_insert_id()"; // update 작업이 끝나고 해당 유저의 pk 를 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int getUserIdByOrderId(int orderId) {
        String getUserIdByOrderIdQuery = "select userId from Orders where orderId = ?;";
        int getUserIdByOrderIdParams = orderId;

        return this.jdbcTemplate.queryForObject(getUserIdByOrderIdQuery, int.class, getUserIdByOrderIdParams);
    }

    public int isExistReview(int userId, int orderId) {
        String isExistReviewQuery = "select exists(select * from Reviews where userId = ? and orderId = ? and status = 1);";
        Object[] isExistReviewParams = new Object[] {userId, orderId};

        return this.jdbcTemplate.queryForObject(isExistReviewQuery, int.class, isExistReviewParams);
    }

    public int isExistOrder(int orderId) {
        String isExistOrderQuery = "select exists(select * from Orders where orderId = ?);";
        int isExistOrderParams = orderId;

        return this.jdbcTemplate.queryForObject(isExistOrderQuery, int.class, isExistOrderParams);
    }

    public List<ReviewMenu> getReviewMenus(int orderId) {
        String getReviewMenusQuery = "select Orders.orderId, menuName\n" +
                "from Orders\n" +
                "    inner join OrderLists OL on Orders.orderId = OL.orderId\n" +
                "    inner join Menus M on OL.menuId = M.menuId\n" +
                "where OL.orderId = ?;";
        int getReviewMenusParams = orderId;

        return this.jdbcTemplate.query(getReviewMenusQuery,
                (rs,rowNum) -> new ReviewMenu(
                        rs.getString("menuName")
                ) ,getReviewMenusParams
        );

    }
}