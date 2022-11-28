package com.ssafy.BravoSilverLife.controller;

import com.ssafy.BravoSilverLife.entity.User;
import com.ssafy.BravoSilverLife.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Data
    static class CreateUserResponse {
        private Long user_id;

        public CreateUserResponse(Long user_id) {
            this.user_id = user_id;
        }
    }

    @Data
    static class Description {
        private String description;
    }


    @Data
    static class changePhoneNumberRequest {

        private String phoneNumber;
        @NotEmpty
        private String newPhoneNumber;
        @NotEmpty
        private String authNumber;
    }


    //닉네임 변경
    @PutMapping("/profile/{nickname}/{newnickname}")
    @Operation(summary = "닉네임 변경", description = "닉네임 변경성공시 1 실패시 0 반환")
    public int chageNickname(@PathVariable("nickname") String nickname,
                             @PathVariable("newnickname") String newNickname) {
        try {
            return userService.changeNickname(nickname, newNickname);
        } catch (Exception e) {
            return 0;
        }
    }

    @PutMapping("/profile/phonenumber")
    @Operation(summary = "핸드폰번호 변경", description = "핸드폰번호 변경 성공시 1 실패시 0 반환")
    public int changePhoneNumber(@RequestBody @Valid UserController.changePhoneNumberRequest request) {

        try {
            return userService.changePhoneNumber(request.phoneNumber, request.authNumber, request.newPhoneNumber);
        } catch (Exception e) {
            System.out.println("예외발생");
            return 0;
        }

    }

    @PutMapping("/profile/changepw/{nickname}/{password}/{newpassword}")
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 성공시 1 실패시 0 반환")
    public int changePassword(@PathVariable("nickname") String nickname, @PathVariable("password") String password, @PathVariable("newpassword") String newpassword) {
        try {
            return userService.changePassword(nickname, password, newpassword);
        } catch (Exception e) {

            return 0;
        }
    }

    @Data
    static class LoginUserRequest {
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class UpdateNicknameResponse {
        private Long id;
        private String nickname;
    }


}
