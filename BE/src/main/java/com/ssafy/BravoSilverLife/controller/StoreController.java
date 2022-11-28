package com.ssafy.BravoSilverLife.controller;

import com.ssafy.BravoSilverLife.dto.StoreDto;
import com.ssafy.BravoSilverLife.entity.Store;
import com.ssafy.BravoSilverLife.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Store", description = "StoreAPI")
@RestController
@CrossOrigin("*")
@RequestMapping("/v1/store")
public class StoreController {

    @Autowired
    StoreService storeService;

    @Operation(summary = "동 상점 확인", description = "동 이름으로 상점 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/stores")
    public ResponseEntity getStores(String dong,String category) throws Exception {
        List<StoreDto> stores = storeService.getStores(dong,category);
        return ResponseEntity.status(200).body(stores);


    }
}
