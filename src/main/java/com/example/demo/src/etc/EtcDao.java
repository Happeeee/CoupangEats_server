package com.example.demo.src.etc;

import com.example.demo.src.etc.model.*;
import com.example.demo.src.etc.model.DTO.noticeList;
import com.example.demo.src.etc.model.DTO.policyInfo;
import com.example.demo.src.etc.model.DTO.policyNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class EtcDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetEventRes> getEventBanners() {
        String getEventBannersQuery = "select eventIamge, startDate, endDate from EventBanner where (current_timestamp) BETWEEN startDate and endDate;";

        return jdbcTemplate.query(getEventBannersQuery,
                (rs,rowNum) -> new GetEventRes(
                        rs.getString("eventIamge"),
                        rs.getString("startDate"),
                        rs.getString("endDate")
                )
        );
    }

    public List<GetTitleRes> getNoticeList() {
        String getNoticeList = "select noticeId,title,DATE_FORMAT(createdAt,'%Y.%m.%d') AS createdAt from Notices";

        return jdbcTemplate.query(getNoticeList,
                (rs,rowNum) -> new GetTitleRes(
                        rs.getInt("noticeId"),
                        rs.getString("title"),
                        rs.getString("createdAt")
                ));
    }

    public List<GetNoticeRes> getNoticeContent(int noticeId) {
        String getNoticeContent = "select title,content from Notices where noticeId=?";

        return jdbcTemplate.query(getNoticeContent,
                (rs,rowNum) -> new GetNoticeRes(
                        rs.getString("title"),
                        rs.getString("content")
                ),noticeId);
    }

    public List<GetAgreeRes> getAgree() {
        String getAgree = "select id,agreeType,agreeContent from agreement";

        return jdbcTemplate.query(getAgree,
                (rs,rowNum) -> new GetAgreeRes(
                        rs.getInt("id"),
                        rs.getString("agreeType"),
                        rs.getString("agreeContent")
                ));
    }

    public GetPolicyRes getPolicy() {
        GetPolicyRes getPolicyRes = new GetPolicyRes();
        String getPolicy = "select policyType,content from Privacy where Privacy.policyType not like '%[공지]%'";
        List<policyInfo> policyInfoList =  this.jdbcTemplate.query(getPolicy,
                (rs,rowNum) -> new policyInfo(
                        rs.getString("policyType"),
                        rs.getString("content")
                ));

        getPolicyRes.setPolicyInfo(policyInfoList);
        String getNotice = "select policyType,content from Privacy where Privacy.policyType like '%[공지]%'";
        policyNotice policyNotice = new policyNotice();
        policyNotice.setCategory("공지사항");
        List<noticeList> policyNoticeList = this.jdbcTemplate.query(getNotice,
                (rs,rowNum) -> new noticeList(
                        rs.getString("policyType"),
                        rs.getString("content")
                ));
        policyNotice.setContent(policyNoticeList);

        getPolicyRes.setNotice(policyNotice);
        return getPolicyRes;
    }
}