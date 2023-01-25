package com.likelion.devroutine.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtProvider {

    private final Long expireTimeMs;
    private String secretKey;

    public JwtProvider(
            @Value("${jwt.token.expireTimeMs}") Long expireTimeMs,
            @Value("${jwt.token.secret}") String secretKey) {
        this.expireTimeMs = expireTimeMs;
        this.secretKey = secretKey;
    }

    @PostConstruct
    protected void init() {
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 시작 : {} ", this.secretKey);
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 완료 : {}", secretKey);
    }

    public Token createToken(String userName, String authorities) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role", authorities);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new Token(token, createRefreshToken(userName, authorities));
    }

    public String createRefreshToken(String userName, String authorities) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role", authorities);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs * 36000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //토큰의 만료 여부 검증
    public boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        return expiredDate.before(new Date());
    }

    //토큰에 저장된 userName 반환
    public String getUserName(String token) {
        return extractClaims(token).getSubject();
    }

    public String getAuthorities(String token){
        return (String) extractClaims(token).get("role");
    }

    //토큰 내용 추출
    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    //인증 객체 생성
    public Authentication getAuthentication(String token){
        return new UsernamePasswordAuthenticationToken(getUserName(token), "", List.of(new SimpleGrantedAuthority(getAuthorities(token)))); //userName, Role이 담기도록 구현
    }
}
