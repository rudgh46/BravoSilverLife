package com.ssafy.BravoSilverLife.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
public class UserDto {
    private String id;
    private String nickname;

    private String phoneNumber;

    @Builder
    public UserDto(String id, String phoneNumber, String nickname) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
    }
}
