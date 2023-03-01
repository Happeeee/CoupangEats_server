package com.example.demo.src.user.model;

import com.example.demo.src.user.model.DTO.ViewOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetReceiptInfo {
    private GetReceiptRes getReceiptRes;
    private List<ViewOrder> viewOrderList;
}
