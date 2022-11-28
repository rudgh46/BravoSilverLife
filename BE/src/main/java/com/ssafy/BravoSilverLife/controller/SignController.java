package com.ssafy.BravoSilverLife.controller;

import com.ssafy.BravoSilverLife.dto.SignInResultDto;
import com.ssafy.BravoSilverLife.dto.SignUpResultDto;
import com.ssafy.BravoSilverLife.dto.UserDto;
import com.ssafy.BravoSilverLife.service.SignService;
import com.ssafy.BravoSilverLife.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SignController {

    private final SignService signService;
    private final UserService userService;

    private Logger logger = LoggerFactory.getLogger(SignController.class);

    @Data
    static class loginRequest {
        @NotEmpty
        private String id;
        @NotEmpty
        private String password;
    }

    @Data
    static class CreateUserRequest {
        @NotEmpty
        private String id;
        @NotEmpty
        private String password;
        @NotEmpty
        private String nickname;
        @NotEmpty
        private String phoneNumber;
        @NotEmpty
        private String authNumber;

    }
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
//    })
    @GetMapping("/userinfo")
    public UserDto userInfo(@RequestHeader(value="RefreshToken") String token)
    {
        return userService.findByToken(token);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "아이디, 비밀번호로 로그인 / 로그인 성공 시 access-token, refresh-token 발급")
    public SignInResultDto signIn(@RequestBody @Valid loginRequest request) {
        SignInResultDto signInResultDto = signService.signIn(request.id, request.password);

        if(signInResultDto.getCode() == 0) {
            logger.info("[signIn] 정상적으로 로그인 되었습니다. id {} , AccessToken : {}, RefreshToken : {}", request.id, signInResultDto.getAccessToken(),signInResultDto.getRefreshToken());
        }

        return signInResultDto;
    }

//    @PostMapping("/sns-sign-in")
//    public SignInResultDto signIn(@RequestBody @Valid CreateUserRequest request) {
//        SignInResultDto signInResultDto = signService.signIn(request.email, request.password);
//
//        if(signInResultDto.getCode() == 0) {
//            logger.info("[signIn] 정상적으로 로그인 되었습니다. id {} , AccessToken : {}, RefreshToken : {}", request.email, signInResultDto.getAccessToken(),signInResultDto.getRefreshToken());
//        }
//
//        return signInResultDto;
//    }


    //반환하는 객체 boolean success, int code, String msg
    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입에 필요한 항목 + MMS 인증번호로 회원가입")
    public SignUpResultDto signUp(@RequestBody @Valid CreateUserRequest request) {
        logger.info("[signUp] 회원가입을 수행합니다. id : {}, password : ****, name : {}", request.getId(), request.getNickname());
        SignUpResultDto signUpResultDto = signService.signUp(request.id, request.password, request.nickname, request.phoneNumber, request.authNumber);

        logger.info("[signUp] 회원가입을 완료했습니다.");

        return signUpResultDto;
    }

    //리프레시 토큰 이용한 access token 재발급
    @PostMapping("/refresh/{user_id}/{refreshToken}")
    public String updateAccessToken(@PathVariable(name = "user_id") Long user_id, @PathVariable(name = "refreshToken") String refreshToken) {
        return userService.updateAccessToken(user_id, refreshToken);
    }


    //아이디 중복검사
    @GetMapping("/sign-up/id/{id}")
    @Operation(summary = "id 중복검사", description = "id 중복검사")
    public int ValidateId(@PathVariable String id) {
        return signService.validateDuplicateId(id);
    }

    //닉네임 중복검사
    @GetMapping("/sign-up/nickname/{nickname}")
    @Operation(summary = "닉네임 중복검사", description = "닉네임 중복검사")
    public int ValidateNickname(@PathVariable String nickname) {
        return signService.validateDuplcateNickname(nickname);
    }

}
