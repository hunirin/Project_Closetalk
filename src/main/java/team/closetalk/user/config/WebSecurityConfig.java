package team.closetalk.user.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import team.closetalk.user.service.CustomOAuth2UserService;
import team.closetalk.user.utils.JwtFilter;
//import team.closetalk.user.utils.OAuth2SuccessHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
//    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests(
                        authHttp -> authHttp.requestMatchers(
                                "/"
                                ,"/testPage"
                                , "/js/join-form.js"
//                                "/main"
//                                , "relayAuth"
                        ).permitAll()
                        .requestMatchers(
                                "/joinPage"
                                , "/users/sendEmail"
                                , "/users/register"
                                , "/users/login"
                        ).anonymous()
//                        .requestMatchers(
//                                "/myPage"
////                                , "/users/userInfo"
////                                , "/authPage"
//                        ).authenticated()

                )
                //폼로그인 추가
                .formLogin( formHttp -> formHttp.loginPage("/loginPage")
                        .defaultSuccessUrl("/loginPage")
                        .failureUrl("/loginPage")
                        .permitAll()
                )
                //소셜로그인 추가
//                .oauth2Login( oauthHttp -> oauthHttp.loginPage("/loginPage")
//                        .successHandler(oAuth2SuccessHandler)
//                        .failureUrl("/loginPage")
//                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
//                        .permitAll()
//                )
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                ).addFilterBefore(
                        jwtFilter
                , AuthorizationFilter.class
                );

        return http.build();
    }
}
