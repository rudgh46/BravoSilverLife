package com.ssafy.BravoSilverLife.service;


import com.ssafy.BravoSilverLife.dto.FranchiseDetail;
import com.ssafy.BravoSilverLife.dto.FranchiseDto;
import com.ssafy.BravoSilverLife.entity.Franchise;

import java.util.List;

public interface FranchiseService {

    List<FranchiseDto> getFranchisesByCount(String category) throws Exception;
    List<FranchiseDto> getFranchisesByPopular(String category) throws Exception;

    void test() throws Exception;

    List<Franchise> searchFranchise(String category, String name) throws Exception;

    FranchiseDetail getFranchiseDetail(String id) throws Exception;
}
