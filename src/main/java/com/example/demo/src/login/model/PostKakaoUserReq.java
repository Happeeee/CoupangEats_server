package com.example.demo.src.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostKakaoUserReq {
    private String kakaoId;
    private String email;
    private String userName;
}