package com.ssafy.BravoSilverLife.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Data
@Schema(description = "프랜차이즈 정보")
public class FranchiseDto {
    String name;
    String id;
    String url;
    String category;
    String registrationNumber;
    long count;
}