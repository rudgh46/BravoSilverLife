package com.ssafy.BravoSilverLife.dto;

import com.ssafy.BravoSilverLife.entity.Store;
import com.ssafy.BravoSilverLife.util.ModelMapperUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Schema(description = "상점 정보")
public class StoreDto {
    String telno;
    String add1;
    String add2;
    String gu;
    String name;
    String category;

    public static StoreDto of(Store storeEntity) {
//        StoreDto storeDto = ModelMapperUtils.getModelMapper().map(storeEntity,StoreDto.class);

        StoreDto storeDto = StoreDto.builder()
                .add1(storeEntity.getAdd1())
                .add2(storeEntity.getAdd2())
                .gu(storeEntity.getGu())
                .name(storeEntity.getName())
                .category(storeEntity.getCategory())
                .build();
        return storeDto;

    }
}
