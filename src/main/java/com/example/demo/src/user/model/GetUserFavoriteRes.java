package com.example.demo.src.user.model;

import com.example.demo.src.store.model.DTO.UserFavorites;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserFavoriteRes {
    private int userFavoriteCount;
    private List<UserFavorites> userFavoriteStores;
}