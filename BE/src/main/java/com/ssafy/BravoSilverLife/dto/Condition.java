package com.ssafy.BravoSilverLife.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Data
@Schema(description = "검색 조건")
public class Condition {
    String dongName;
    int rentPriceMin;
    int rentPriceMax;
    int priceMin;
    int priceMax;
    int areaMin;
    int areaMax;
    double leftLon;
    double rightLon;
    double topLat;
    double bottomLat;
}