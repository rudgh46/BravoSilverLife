package com.ssafy.BravoSilverLife.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.json.simple.JSONArray;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Schema(description = "매물 정보")
public class Article {
    long articleNo;
    String articleName;
    String tradeTypeName;
    String floor;
    String maxFloor;
    String rentPrc; // 월세
    String dealOrWarrantPrc; // 보증
    long area1; // 계약
    long area2; // 전용
    JSONArray tagList;
    String articleFeatureDesc;
    String cpPcArticleUrl;
    double latitude;
    double longitude;
    // "direction":"남향",
    // "articleConfirmYmd":"20221026",
}
