package team.closetalk.user.utils;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParseException;
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
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    private final Key signingKey;
    private final JwtParser jwtParser;

    private static final long ACCESS_TOKEN_EXPIRATION_SECONDS = 900; // 15분
    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS = 86400; // 1일

    private static final String JWT_SECRET = generateSecretKey();

    //시크릿키 주입?
    public JwtUtils() {
        this.signingKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
    }
    private static String generateSecretKey() {
        byte[] secretKeyBytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secretKeyBytes);
        return Base64.getEncoder().encodeToString(secretKeyBytes);
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

    public String extractUsernameFromExpiredToken(String token) {
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length == 3) {
            String payload = new String(Base64.getDecoder().decode(tokenParts[1]));
            try {
                Gson gson = new Gson();
                // payload를 JSON으로 파싱
                JsonObject payloadJson = gson.fromJson(payload, JsonObject.class);
                // "sub" 클레임에서 username 추출
                if (payloadJson.has("sub")) {
                    return payloadJson.get("sub").getAsString();
                }
            } catch (JsonParseException ex) {
                // payload를 JSON으로 파싱하는 데 실패한 경우 처리
                ex.printStackTrace(); // 또는 로그에 기록
            }
        }
        return null; // username을 찾을 수 없는 경우 null 또는 기본값 반환
    }

}
