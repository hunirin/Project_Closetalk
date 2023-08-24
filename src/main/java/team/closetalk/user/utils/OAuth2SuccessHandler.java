package team.closetalk.user.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import team.closetalk.user.dto.CustomUserDetails;
import team.closetalk.user.service.UserService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final UserService userService;

    //인증에 성공했을 때
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User =(OAuth2User) authentication.getPrincipal();

        String provider = oAuth2User.getAttribute("provider");
        String providerId = oAuth2User.getAttribute("id"); //이거의 역할? 로그인 할 때마다 매번 달라지는지 확인 필요
        log.info("이번 로그인 시도의 providerId는: {}", providerId);
        String email = oAuth2User.getAttribute("email");
        String loginId = email.split("@")[0]; //전달 받은 인증정보에서 이메일 앞자리로 따와서 loginId로 사용
        String nickname = oAuth2User.getAttribute("nickname");

        //최초 소셜로그인 시도인 경우 DB 등록
        if(!userService.userExists(loginId)){
            userService.createUser(CustomUserDetails.builder()
                    .loginId(loginId)
                    .password(providerId)
                    .nickname(loginId)
                    .email(email)
                    .social(provider)
                    .build()
            );
        }

        //JWT 발급
        CustomUserDetails customUserDetails = userService.loadUserByUsername(loginId);
        String token = jwtUtils.generateToken(customUserDetails);

        //(임시) JWT 확인
        log.info(token);

//        response.addHeader("Authorization", "Bearer " + token);

        response.sendRedirect("/testPage?token=" + token);
    }
}
