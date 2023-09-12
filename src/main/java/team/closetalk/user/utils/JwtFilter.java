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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import team.closetalk.user.dto.CustomUserDetails;
import team.closetalk.user.service.TokenService;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.split(" ")[1];
            if(jwtUtils.validate(token)){
//                if (isTokenValidInRedis(token)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();

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
//                } else {
//                    log.warn("토큰이 만료되었거나 유효하지 않습니다.");
//                }
            } else {
                log.warn("인증되지 않은 사용자입니다.");
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isTokenValidInRedis(String token) {
        String username = jwtUtils.parseClaims(token).getSubject();
        String storedToken = tokenService.getAccessToken(username);

        // Redis에서 저장된 토큰과 비교
        return storedToken != null && storedToken.equals(token);
    }
}