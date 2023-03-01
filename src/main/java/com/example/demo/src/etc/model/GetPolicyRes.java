package com.example.demo.src.etc.model;

import com.example.demo.src.etc.model.DTO.policyNotice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPolicyRes {
    private List<com.example.demo.src.etc.model.DTO.policyInfo> policyInfo;
    private policyNotice notice;
}
