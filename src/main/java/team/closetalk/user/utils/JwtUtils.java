package team.closetalk.user.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    private final Key signingKey;
    private final JwtParser jwtParser;

    private static final long ACCESS_TOKEN_EXPIRATION_SECONDS = 1800; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS = 86400; // 1일

    private static final byte[] JWT_SECRET = generateRandomKey(32);

    //시크릿키 주입?
    public JwtUtils() {
        this.signingKey = Keys.hmacShaKeyFor(JWT_SECRET);

        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
    }
    private static byte[] generateRandomKey(int keyLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[keyLength];
        secureRandom.nextBytes(key);
        return key;
    }

    //Jwt 파싱
    public Claims parseClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public boolean validate(String token) {
        try{
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e){
            log.warn("invalid jwt: {}", e.getClass());
            return false;
        }
    }

    public String generateAccessToken(UserDetails userDetails) {
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRATION_SECONDS))); // 액세스 토큰의 만료 시간

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_SECONDS))); // 리프레시 토큰의 만료 시간

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                .compact();
    }
}
