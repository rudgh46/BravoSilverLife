package com.ssafy.BravoSilverLife.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Data
@Schema(description = "프랜차이즈 정보")
public class FranchiseDetail {

    String id;
    String name;
    String logoUrl;
    String thumbUrl;

    double monthlySales;

    JSONArray closureRates;
    JSONArray directStoresCount;
    JSONArray franchiseeCount;
    JSONArray changeOwnerCount;
    JSONArray newCount;
    JSONArray terminatedCount;
    JSONObject initialCost;

}
