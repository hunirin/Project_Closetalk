package team.closetalk.user.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    private static String generateSecretKey() {
        // 시크릿 키 길이 설정 (256 비트)
        int keyLength = 32;

        //  랜덤 값 시크릿 키 생성
        byte[] secretKeyBytes = generateRandomKey(keyLength);

        // 시크릿 키를 Base64 인코딩하여 문자열로 표현
        return Base64.getEncoder().encodeToString(secretKeyBytes);
    }

    private static byte[] generateRandomKey(int keyLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[keyLength];
        secureRandom.nextBytes(key);
        return key;
    }
}

