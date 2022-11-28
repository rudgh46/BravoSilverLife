package com.ssafy.BravoSilverLife.service;

import com.ssafy.BravoSilverLife.config.JwtTokenProvider;
import com.ssafy.BravoSilverLife.dto.UserDto;
import com.ssafy.BravoSilverLife.entity.Bookmark;
import com.ssafy.BravoSilverLife.entity.PhoneAuth;
import com.ssafy.BravoSilverLife.entity.User;
import com.ssafy.BravoSilverLife.repository.BookmarkRepository;
import com.ssafy.BravoSilverLife.repository.PhoneAuthRepository;
import com.ssafy.BravoSilverLife.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final PhoneAuthRepository phoneAuthRepository;

    private final BookmarkRepository bookmarkRepository;

    private final MMSService mmsService;

    public int changeNickname(String nickname, String newNickname) {
        User findByNickname = userRepository.findByNickname(nickname);
        findByNickname.changeNickname(newNickname);

        if (findByNickname.getNickname().equals(newNickname))
            return 1;
        return 0;
    }

    public int changePassword(String nickname, String password, String newpassword) {
//        System.out.println("?????????????????????");
        User findUser = userRepository.findByNickname(nickname);
//        System.out.println("@@@"+findUser.getId());
        if (passwordEncoder.matches(password, findUser.getPassword())) {
            findUser.changePassword(passwordEncoder.encode(newpassword));
            if (passwordEncoder.matches(newpassword, findUser.getPassword())) {
                return 1;
            }
        }

        return 0;
    }

    public int changePhoneNumber(String phoneNumber, String authNumber, String newPhoneNumber) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber); //기존번호로 user 정보 찾고
        PhoneAuth phoneAuth = phoneAuthRepository.findByPhoneNumber(newPhoneNumber); //새 폰번호 인증번호 확인

        if ((phoneAuth.getPhoneAuth()).equals(authNumber)) { // 새 폰번호 인증번호 확인 시
            user.get().changePhoneNumber(newPhoneNumber); // user 정보에서 핸드폰 번호 변경
        }
//        PhoneAuth phoneAuth = phoneAuthRepository.findByPhoneNumber(phoneNumber);
        phoneAuthRepository.deleteByPhoneNumber(phoneNumber); //기존 번호 인증번호 삭제

        return 1;
    }


    //로그인
    public User login(String email, String password) {
        return userRepository.findByIdAndPassword(email, password);
    }

    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findById(email);
    }

    public User findById(String userEmail) {
        return userRepository.findById(userEmail);
    }

    public UserDto findByToken(String token) {
        User targetUser = userRepository.findByToken(token);

        UserDto responseUserDto = UserDto.builder()
                .id(targetUser.getId())
                .nickname(targetUser.getNickname())
                .phoneNumber(targetUser.getPhoneNumber())
                .build();

        return responseUserDto;
    }

    public String updateAccessToken(Long idx, String refreshToken) {
        String targetUserRefreshToken = userRepository.getRefreshToken(idx);
        User targetUser = userRepository.findByIdx(idx);
        if (refreshToken.equals(targetUserRefreshToken)) {
            String newAccessToken = jwtTokenProvider.createToken(targetUser.getId(), targetUser.getRoles());
            return newAccessToken;
        } else {
            return "리프레시 토큰이 맞지 않습니다.";
        }
    }

}
