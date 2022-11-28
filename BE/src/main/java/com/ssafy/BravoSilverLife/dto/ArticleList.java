package com.ssafy.BravoSilverLife.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Schema(description = "클러스터내 매물 리스트")
public class ArticleList {
    boolean isMoreData;
    List<Article> articles;
}
