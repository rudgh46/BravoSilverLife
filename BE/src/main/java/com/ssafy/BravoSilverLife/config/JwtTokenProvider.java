package com.ssafy.BravoSilverLife.config;

import com.ssafy.BravoSilverLife.entity.User;
import com.ssafy.BravoSilverLife.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final UserRepository userRepository;


//    @Value("${springboot.jwt.secret}")
    private String secretKey = "secretKeyforAwAProject_The signing key's size is 176 bits which is not secure enough for the HS256 algorithm.";
    private final long tokenValidMillisecond = 1000L*60*60*6; // 1시간
    private final long refreshTokenValidMillisecond = 1000L*60*60*24*14; //2주

    @PostConstruct
    protected void init() {
        logger.info("[init] JwttoeknProvider 내 secretKey 초기화 시작");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        logger.info("[init] JwttoeknProvider 내 secretKey 초기화 완료");
    }

    public String createToken(String id, List<String> roles) {
        logger.info("[createToken] 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(id); // 아이디
        claims.put("roles",roles); //권한
        Date now = new Date(); //날짜

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        logger.info("[createToken] 토큰생성완료");
        return token;
    }

    public String reissueRefreshToken(String refreshToken) throws RuntimeException {
        //refresh토큰을 DB의 refreshtoken과 비교
        String id = getUserId(refreshToken);
        User targetUser = userRepository.findById(id);

        Authentication FrontAuthentication = getAuthentication(refreshToken);
        Authentication DBAuthentication = getAuthentication(targetUser.getRefreshToken());

        //front refreshToken안에 있는 이메일과 DB refreshtoken에 있는 이메일이 같을 때
        if(FrontAuthentication.getName().equals(DBAuthentication.getName())) {
            String newRefreshToken = createRefreshToken(targetUser.getId(), targetUser.getRoles());
            targetUser.giveToken(newRefreshToken);
            return newRefreshToken;
        }
        else {
            logger.info("refreshToken이 일치하지 않습니다.");
            return null;
        }
    }

    public String createRefreshToken(String userEmail, List<String> roles) {
        logger.info("[createRefreshToken] 리프레시 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(userEmail); //이메일
        claims.put("roles",roles); //권한
        Date now = new Date(); //날짜

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        logger.info("[createRefreshToken] 리프레시 토큰생성완료");
        return token;
    }

    public Authentication getAuthentication(String token) {
        logger.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        User user = userRepository.findById(this.getUserId(token));
        //이메일로만 얘가 맞다고 판단하는게 맞는건가????
        logger.info("[getAuthentication] 토큰 인증 정보 조회 완료, User UserEmail : {}, User Role : {}", user.getId(),user.getRoles().get(0));
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public String getUserId(String token) {
        logger.info("[getUserEmail] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        logger.info("[getUserEmail] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }

    public String resolveToken(HttpServletRequest request) {
        logger.info("[resolveToken] HTTP 헤더에서 Token값 추출");
        return request.getHeader("X-AUTH-TOKEN");
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        logger.info("[resolveRefreshToken] HTTP 헤더에서 RefreshToken값 추출");
        return request.getHeader("RefreshToken");
    }

//    public String validateToken(String token) {
//        logger.info("[validateToken] 토큰 유효 체크 시작");
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//
//            return "ACCESS";
//        } catch (ExpiredJwtException e) {
//            //토큰이 만료된 경우 refreshtoken 확인 해야함
//            logger.info("[validateToken] Accesstoken 만료");
//            return "EXPIRED";
//        } catch (JwtException | IllegalArgumentException e) {
//            logger.info("jwtException : {}",e);
//        }
//        return "DENIDE";
//    }

    public boolean validateToken(String token) {
        logger.info("[validateToken] 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            logger.info("[validateToken] 토큰 유효 체크 완료");
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e)
        {
            logger.info("[validateToken] access토큰 기간 만료");
            return false;
        }
        catch (Exception e) {
            logger.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
