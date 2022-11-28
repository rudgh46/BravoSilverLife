package com.ssafy.BravoSilverLife.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDto extends SignUpResultDto{
    private String accessToken;
    private String refreshToken;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String accessToken, String refreshToken) {
        super(success,code,msg);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
