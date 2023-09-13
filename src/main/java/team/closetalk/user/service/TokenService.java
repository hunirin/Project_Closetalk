package team.closetalk.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    private static final long ACCESS_TOKEN_EXPIRATION_SECONDS = 1800; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS = 86400; // 1일

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public TokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveAccessToken(String token, String username) {
        redisTemplate.opsForValue().set("access:" + username, token, ACCESS_TOKEN_EXPIRATION_SECONDS, TimeUnit.MINUTES);
    }

    public void saveRefreshToken(String token, String username) {
        redisTemplate.opsForValue().set("refresh:" + username, token, REFRESH_TOKEN_EXPIRATION_SECONDS, TimeUnit.MINUTES);
    }

    public String getAccessToken(String username) {
        return redisTemplate.opsForValue().get("access:" + username);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("refresh:" + username);
    }

    public void deleteAccessToken(String username) {
        redisTemplate.delete("access:" + username);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete("refresh:" + username);
    }
}
