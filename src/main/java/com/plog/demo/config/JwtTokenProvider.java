package com.plog.demo.config;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    /**
     * TODO 나중에 시크릿키, 만료시간 환경변수 처리해야함
     */
    private String accessTokenSecretKey = "SecretKey111";

    private String refreshTokenSecretKey = "SecretKey222";

    //하루
    private final long jwtAccessExpiration = 1000L * 60;

    //일주일
    private final long jwtRefreshExpiration = 1000L * 60 * 60;

    /**
     * secret key 초기화
     */
    @PostConstruct
    public void init(){
        log.info("[init] secret key 초기화");
        accessTokenSecretKey = Base64.getEncoder().encodeToString(accessTokenSecretKey.getBytes(StandardCharsets.UTF_8));
        refreshTokenSecretKey = Base64.getEncoder().encodeToString(accessTokenSecretKey.getBytes(StandardCharsets.UTF_8));
        log.info("[init] secret key 초기화 완료");
    }

    /**
     * JWT accessToken 생성
     */
    public String createAccessToken(String userId){
        log.info("[createAccessToken] 액세스 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtAccessExpiration))
                .signWith(SignatureAlgorithm.HS256, accessTokenSecretKey)
                .compact();

        log.info("[createAccessToken] 액세스 토큰 생성 완료");

        return accessToken;
    }

    /**
     * JWT refreshToken 생성
     */
    public String createRefreshToken(){
        log.info("[createRefreshToken] 액세스 토큰 생성 시작");
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtRefreshExpiration))
                .signWith(SignatureAlgorithm.HS256, refreshTokenSecretKey)
                .compact();

        log.info("[createRefreshToken] 액세스 토큰 생성 완료");

        return refreshToken;
    }

    /**
     * 토큰 기반 회원 정보 추출
     */
    public String getUserId(String accessToken){
        log.info("[getUserId] 토큰 기반 회원 정보 추출");
        Claims info = Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(accessToken).getBody();
        log.info("[getUserId] info = {}, userId = {}", info, info.getSubject());
        return info.getSubject();
    }

    /**
     * request 헤더에서 token 추출
     * @param request
     * @return 토큰 값
     */
    public String resolveToken(HttpServletRequest request){
        log.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
        return request.getHeader("X-AUTH_TOKEN");
    }

    /**
     *  access token 토큰 유효 체크
     */
    public boolean validationAccessToken(String accessToken){
        log.info("[validationAccessToken] 토큰 유효 체크 시작");

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(accessToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (ExpiredJwtException e){
            log.info("[validationAccessToken] 시간 만료 토큰");
            return false;
        }catch (Exception e){
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }

    /**
     *  refresh token 토큰 유효 체크
     */
    public boolean validationRefreshToken(String refreshToken){
        log.info("[validationRefreshToken] 토큰 유효 체크 시작");

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(refreshToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (ExpiredJwtException e){
            log.info("[validationRefreshToken] 시간 만료 토큰");
            return false;
        }catch (Exception e){
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
