package com.ssafy.BravoSilverLife.controller;

import com.ssafy.BravoSilverLife.dto.FranchiseDetail;
import com.ssafy.BravoSilverLife.dto.FranchiseDto;
import com.ssafy.BravoSilverLife.entity.Franchise;
import com.ssafy.BravoSilverLife.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Franchise", description = "FranchiseAPI")
@RestController
@CrossOrigin("*")
@RequestMapping("/v1/franchise")
public class FranchiseController {

    @Autowired
    FranchiseService franchiseService;

    @Operation(summary = "프랜차이즈 매장수 랭킹 확인", description = "프랜차이즈 매장수 랭킹하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/franchise-count")
    public ResponseEntity getFranchiseByCount(String category) throws Exception {
        List<FranchiseDto> franchiseDtos = franchiseService.getFranchisesByCount(category);

        if (franchiseDtos != null) return ResponseEntity.status(200).body(franchiseDtos);
        else return ResponseEntity.status(400).body("카테고리 확인");

    }

    @Operation(summary = "프랜차이즈 1년간 증가 랭킹 확인", description = "프랜차이즈 1년간 증가 랭킹하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/franchise-popular")
    public ResponseEntity getFranchiseByPopular(String category) throws Exception {
        List<FranchiseDto> franchiseDtos = franchiseService.getFranchisesByPopular(category);

        if (franchiseDtos != null) return ResponseEntity.status(200).body(franchiseDtos);
        else return ResponseEntity.status(400).body("카테고리 확인");

    }

    @Operation(summary = "프랜차이즈 상세 정보 확인", description = "프랜차이즈 상세 정보 확인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/franchise-detail")
    public ResponseEntity getFranchiseDetail(String id) throws Exception {
        FranchiseDetail franchiseDetail = franchiseService.getFranchiseDetail(id);

        if (franchiseDetail != null) return ResponseEntity.status(200).body(franchiseDetail);
        else return ResponseEntity.status(400).body("프랜차이즈 확인");

    }


    @Operation(summary = "프랜차이즈 검색", description = "프랜차이즈 검색하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/search")
    public ResponseEntity searchFranchise(String category, String name) throws Exception {
        List<Franchise> franchises = franchiseService.searchFranchise(category, name);
        return ResponseEntity.status(200).body(franchises);
    }
}
