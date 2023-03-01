package com.example.demo.src.login;


import com.example.demo.src.login.model.KakaoUser;
import com.example.demo.src.login.model.PostKakaoUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OAuthDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<String> getKakaoIds() {
        String getKakaoIdsQuery = "select kakaoId from Users;";
        return this.jdbcTemplate.queryForList(getKakaoIdsQuery, String.class);
    }

    public int isExistedKakaoUser(String kakaoId) {
        String isExistedKakaoUserQuery = "select exists(select * from Users where password = ?);";
        String isExistedKakaoUserParams = kakaoId;

        return this.jdbcTemplate.queryForObject(isExistedKakaoUserQuery, int.class, isExistedKakaoUserParams);
    }

    public KakaoUser getKakaoId(PostKakaoUserReq postKakaoUserReq) {
        String getKakaoIdQuery = "select userId, password, email, name from Users where email = ?;";
        String getKakaoIdParams = postKakaoUserReq.getEmail();

        return this.jdbcTemplate.queryForObject(getKakaoIdQuery,
                (rs,rowNum)-> new KakaoUser(
                        rs.getInt("userId"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("name")
                ),
                getKakaoIdParams);
    }

    public int createKakaoUser(PostKakaoUserReq postKakaoUserReq) {

        System.out.println(postKakaoUserReq.getKakaoId());
        System.out.println(postKakaoUserReq.getEmail());
        System.out.println(postKakaoUserReq.getUserName());
        String createKakaoUserQuery = "insert into Users (email, password, name, phoneNumber, agree, isKakao) values (?,?,?, 'kakaoUserPhone',1,1);";
        Object[] createKakaoUserParams = new Object[] {postKakaoUserReq.getEmail(), postKakaoUserReq.getKakaoId(), postKakaoUserReq.getUserName()};
        this.jdbcTemplate.update(createKakaoUserQuery,createKakaoUserParams);

        String lastInsertIdQuery = "select last_insert_id()"; // update 작업이 끝나고 해당 유저의 pk 를 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}