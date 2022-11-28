package com.ssafy.BravoSilverLife.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.json.simple.JSONArray;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Schema(description = "매물 상세 정보")
public class ArticleDetail {
    
    long articleNo;
    String articleName;
    long cortarNo;
    String buildingTypeName;
    String tradeTypeName;
    double latitude;
    double longitude;
    String cityName;
    String divisionName;
    String sectionName;
    long walkingTimeToNearSubway;
    String exposureAddress;
    long monthlyManagementCost;
    String articleFeatureDescription;
    String detailDescription;
    long parkingCount;
    String parkingPossibleYN;
    JSONArray tagList;
    String floor;
    String maxFloor;
    long area1; //계약
    long area2; // 전용
    String direction;
    String buildingName;
    String cpPcArticleUrl;
    long rentPrice;
    long dealPrice;
    long warrantPrice;
    JSONArray articlePhotos;
}
