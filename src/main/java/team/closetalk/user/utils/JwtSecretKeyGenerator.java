package team.closetalk.user.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static void main(String[] args) {
        // 시크릿 키 길이 설정 (256 비트)
        int keyLength = 32;

        //  랜덤 값 시크릿 키 생성
        byte[] secretKeyBytes = generateRandomKey(keyLength);

        // 시크릿 키를 Base64 인코딩하여 문자열로 표현
        String secretKey = Base64.getEncoder().encodeToString(secretKeyBytes);

        System.out.println("Generated Secret Key: " + secretKey);
    }

    private static byte[] generateRandomKey(int keyLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[keyLength];
        secureRandom.nextBytes(key);
        return key;
    }
}
