package com.ssafy.BravoSilverLife.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Schema(description = "매물 클러스터")
public class Cluster {

    long count;
    long markerId;

    double latitude;
    double longitude;
    double topLat;
    double rightLon;
    double bottomLat;
    double leftLon;

//    clusterDiameterRatioInLgeohashBox":0.5
//    "clusterShowingTypeDiameterRatio":0.62
//    "isCortarInsideView":true
//    "realtors":[]
}
