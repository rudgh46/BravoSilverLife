package com.ssafy.BravoSilverLife.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Schema(description = "유동인구")
public class Population {

    int dayAvg;

    float mon;
    float tues;
    float wed;
    float thur;
    float fri;
    float sat;
    float sun;

    float day;
    float weekend;

    float firstHour;
    float secondHour;
    float thirdHour;
    float fourthHour;
    float fifthHour;
    float sixthHour;
}
