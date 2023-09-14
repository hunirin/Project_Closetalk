package team.closetalk.user.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import team.closetalk.user.dto.CustomUserDetails;
import team.closetalk.user.service.TokenService;
import team.closetalk.user.service.UserService;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.split(" ")[1];
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            if(jwtUtils.validate(token)){
                log.info("토큰 검증 완료");
                String username = jwtUtils.parseClaims(token).getSubject();
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder()
                                .loginId(username)
                                .build()
                        , token
                        , new ArrayList<>()
                );
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            } else {
                log.info("AccessToken 만료 : Redis - RefreshToken 탐색");
                String username = jwtUtils.extractUsernameFromExpiredToken(token);
                String refreshToken = tokenService.getRefreshToken(username);
                if (jwtUtils.validate(refreshToken)) {
                    log.info("새로운 AccessToken 발급");
                    UserDetails responseUser = userService.loadUserByUsername(username);
                    String newAccessToken = jwtUtils.generateAccessToken(responseUser);
                    String newRefreshToken = jwtUtils.generateRefreshToken(responseUser);
                    tokenService.saveRefreshToken(newRefreshToken, username);

                    AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            CustomUserDetails.builder()
                                    .loginId(username)
                                    .build(),
                            newAccessToken,
                            new ArrayList<>()
                    );
                    context.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(context);
                    response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
                } else {
                    log.warn("RefreshToken이 유효하지 않습니다.");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}