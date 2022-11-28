package com.ssafy.BravoSilverLife.service;

import com.ssafy.BravoSilverLife.dto.StoreDto;
import com.ssafy.BravoSilverLife.entity.Store;
import com.ssafy.BravoSilverLife.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreRepository storeRepository;

    @Override
    public List<StoreDto> getStores(String dong, String category) {

        List<Store> temps = storeRepository.findByDongAndCategory(dong, category);
        List<StoreDto> stores = new ArrayList<>();

        StoreDto store;
        for (Store temp : temps) {
            store = StoreDto.of(temp);
            stores.add(store);
        }

        return stores;
    }
}
