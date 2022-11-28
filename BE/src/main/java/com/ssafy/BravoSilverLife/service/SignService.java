package com.ssafy.BravoSilverLife.service;

import com.ssafy.BravoSilverLife.config.CommonResponse;
import com.ssafy.BravoSilverLife.config.JwtTokenProvider;
import com.ssafy.BravoSilverLife.entity.PhoneAuth;
import com.ssafy.BravoSilverLife.entity.User;
import com.ssafy.BravoSilverLife.dto.SignInResultDto;
import com.ssafy.BravoSilverLife.dto.SignUpResultDto;
import com.ssafy.BravoSilverLife.repository.PhoneAuthRepository;
import com.ssafy.BravoSilverLife.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;

@Service
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MMSService mmsService;

    private final PhoneAuthRepository phoneAuthRepository;

    public SignUpResultDto signUp(String id, String password, String nickname, String phoneNumber, String authNumber) {
        logger.info("[getSignUpResult] 회원 가입 정보 전달");
        User user;


        PhoneAuth pAuth;
        pAuth = phoneAuthRepository.findByPhoneNumber(phoneNumber);

        String getPhoneAuth = pAuth.getPhoneAuth();
        User savedUser = null;

        if(getPhoneAuth.equals(authNumber)) {
            user = User.builder()
                    .id(id)
                    .nickname(nickname)
                    .phoneNumber(phoneNumber)
                    .password(passwordEncoder.encode(password)) //password암호화 인코딩
                    .build();
            savedUser = userRepository.save(user);
        }

        SignUpResultDto signUpResultDto = new SignInResultDto();

        logger.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        if (!savedUser.getId().isEmpty()) {
            logger.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        } else {
            logger.info("[getSignUpResult] 실패 처리 완료");
            setFailResult(signUpResultDto);
        }
        return signUpResultDto;
    }

    //로그인

    public SignInResultDto signIn(String email, String password) throws RuntimeException {
        logger.info("[getSignInResult] signDataHandler로 회원 정보 요청");
        User user = userRepository.findById(email);
        logger.info("[getSignInResult] Email : {} ", email);

        logger.info("[getSignInResult] 패스워드 비교 수행");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.info("비밀번호 안맞아요 !!!!!!!!!");
            logger.info("비밀번호 안맞아요 !!!!!!!!!");
            logger.info("비밀번호 안맞아요 !!!!!!!!!");
            logger.info("비밀번호 안맞아요 !!!!!!!!!");
            throw new RuntimeException();
        }
        logger.info("[getSignInResult] 패스워드 일치");

        logger.info("[getSignInResult] SignInResultDto 생성");
        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getId()), user.getRoles());

        //반환값은 액세스토큰
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        //리프레시 토큰은 테이블에 저장
        user.giveToken(refreshToken);

        logger.info("[refreshToken 값] " + refreshToken);
        logger.info("[accessToken 값] " + accessToken);
        //user.giveToken(signInResultDto.getToken());
        logger.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccessResult(signInResultDto);

        return signInResultDto;
    }

    public SignInResultDto signIn2(String email) throws RuntimeException {
        logger.info("[getSignInResult] signDataHandler로 회원 정보 요청");
        User user = userRepository.findById(email);
        logger.info("[getSignInResult] Email : {} ", email);

        logger.info("[getSignInResult] 패스워드 비교 수행");
        if (!(user.getId() == user.getId())) {
            logger.info("소셜 비밀번호 안맞아요 !!!!!!!!!");
            logger.info("소셜 비밀번호 안맞아요 !!!!!!!!!");
            throw new RuntimeException();
        }
        logger.info("[getSignInResult] 패스워드 일치");

        logger.info("[getSignInResult] SignInResultDto 생성");
        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getId()), user.getRoles());

        //반환값은 액세스토큰
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        //리프레시 토큰은 테이블에 저장
        user.giveToken(refreshToken);

        logger.info("[refreshToken 값] " + refreshToken);
        logger.info("[accessToken 값] " + accessToken);
        //user.giveToken(signInResultDto.getToken());
        logger.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccessResult(signInResultDto);

        return signInResultDto;
    }


    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }


    //중복검사
    public int validateDuplicateId(String id) {
        //이메일 중복검사
        //중복 시 0 / 정상 1
        User findByIdUser = userRepository.findById(id);
        if (findByIdUser == null)
            return 1;
        return 0;
    }

    //
    public int validateDuplcateNickname(String nickname) {
        //닉네임 중복검사
        User findByNicknameUser = userRepository.findByNickname(nickname);
        if (findByNicknameUser == null)
            return 1;
        return 0;
    }

    public int checkPassword(String userEmail, String password) {
        User targetUser = userRepository.findById(userEmail);

        if (!passwordEncoder.matches(password, targetUser.getPassword())) {
            return 0;
        }
        return 1;
    }

    public int resetPassword(String userEmail, String password) {
        User targetUser = userRepository.findById(userEmail);

        try {
            targetUser.resetPassword(passwordEncoder.encode(password));
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
