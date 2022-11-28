package com.ssafy.BravoSilverLife.service;

import com.ssafy.BravoSilverLife.dto.StoreDto;

import java.util.List;

public interface StoreService {

    List<StoreDto> getStores(String gu, String category);
}
