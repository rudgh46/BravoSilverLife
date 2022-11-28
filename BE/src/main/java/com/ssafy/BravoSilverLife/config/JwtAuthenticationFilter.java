package com.ssafy.BravoSilverLife.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;


    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        logger.info("[doFilterInternal] header에 있는 token 값 추출 완료. token : {}", token);

        logger.info("[doFilterInternal] token값 유효성 체크 시작");
        //토큰 만료 안된경우
        if(token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("[doFilterInternal] token 값 유효성 체크 완료");
        }
        else if(token != null && !jwtTokenProvider.validateToken(token)){
            logger.info("[doFilterInternal] accesstoken 만료 됨 -> refreshToken값 요구해야함.");
            String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
            Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

            List<String> roles = new ArrayList<>();
            roles.add(authentication.getAuthorities().toString());

            //헤더로 통해서 같이 넘어온 리프레시 토큰이 만료되지 않았을 때
            if(refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                //refreshToken 확인해서 재발급하기
                String newRefreshToken = jwtTokenProvider.reissueRefreshToken(refreshToken);
                if(newRefreshToken != null) {
                    //refresh 재발급
                    response.setHeader("RefreshToken",newRefreshToken);

                    //access 재발급
                    String newAccessToken = jwtTokenProvider.createToken(authentication.getName(), roles);
                    response.setHeader("X-AUTH-TOKEN", newAccessToken);
                    Authentication authentication2 = jwtTokenProvider.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication2);
                    logger.info("새로운 리프레시 & 액세스 토큰 발급");
                }

            }
        }
        else {
            logger.info("no valid JWT TOKEN FOUND, uri : {}",request.getRequestURI());
        }

        filterChain.doFilter(request,response);

    }
}
