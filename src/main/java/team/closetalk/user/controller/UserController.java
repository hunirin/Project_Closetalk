package team.closetalk.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.user.dto.*;
import team.closetalk.user.service.EmailSendService;
import team.closetalk.user.service.TokenService;
import team.closetalk.user.service.UserService;
import team.closetalk.user.utils.JwtUtils;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private final EmailSendService emailSendService;
    private final TokenService tokenService;

    //C
    //이메일 인증 처리
    @ResponseBody
    @PostMapping(value = "/sendEmail")
    public ResponseEntity<EmailAuthDto> sendAuthEmail(@RequestBody String email){
        String code = emailSendService.makeEmailAuth(email.substring(1,email.length()-1));

        EmailAuthDto emailAuthDto = new EmailAuthDto();
        emailAuthDto.setAuthCode(code);

        return ResponseEntity.status(HttpStatus.OK).body(emailAuthDto);
    }

    //회원가입
    @PostMapping(value = "/register"
            , consumes = "multipart/form-data"
    )
    public void registerUser(  //ResponseEntity<> 로 리턴타입 변경할 것 .
            @RequestParam("loginId") String loginId
            , @RequestParam("password") String password
            , @RequestParam("password-check") String passwordCheck
            , @RequestParam("nickname") String nickname
            , @RequestParam("email") String email
            , @RequestParam("social") String social
            , @RequestParam("profile-image") MultipartFile profileImage
    ){
        if(password.equals(passwordCheck)){
            userService.createUser(CustomUserDetails.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .email(email)
                    .social(social)
                    .profileImage(profileImage)
                    .build()
            );

//            return ResponseEntity.status(HttpStatus.OK).body("join success");
        } else {
            log.warn("password mismatch");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("password mismatch");
        }

    }

    // 로그인(나중에 Service 분리)
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response,
                                  @RequestBody LoginRequestDto loginReqDto) {
        CustomUserDetails responseUser = userService.loadUserByUsername(loginReqDto.getLoginId());
        if (!passwordEncoder.matches(loginReqDto.getPassword(), responseUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String accessToken = jwtUtils.generateAccessToken(responseUser); // Access Token
        tokenService.saveAccessToken(accessToken, responseUser.getLoginId());

        String refreshToken = tokenService.getRefreshToken(responseUser.getLoginId()); // Refresh Token
        if (refreshToken == null || refreshToken.isEmpty()) {
            refreshToken = jwtUtils.generateRefreshToken(responseUser);
            tokenService.saveRefreshToken(refreshToken, responseUser.getLoginId());
        }
        LoginResponseDto loginResDto =
                new LoginResponseDto(responseUser.getNickname(), accessToken);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효 기간 (7일)
        cookie.setHttpOnly(true); // JavaScript로 쿠키 접근 방지
        cookie.setSecure(true); // HTTPS 연결에서만 전송
        response.addCookie(cookie); // HttpServletResponse에 쿠키 추가
        log.info("Cookie: {}", cookie);
        /* 클라이언트 부분 일단 킵
            // 쿠키에서 리프레시 토큰 읽기
            function getCookie(name) {
              const value = `; ${document.cookie}`;
              const parts = value.split(`; ${name}=`);
              if (parts.length === 2) return parts.pop().split(';').shift();
            }

            const refreshToken = getCookie('refreshToken'); // 쿠키 이름 'refreshToken'에 저장된 리프레시 토큰 값
            console.log('Refresh Token:', refreshToken);
         */
        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

        return ResponseEntity.ok()
                .body(loginResDto);
    }

    //회원정보 가져오기
    @GetMapping
    public CustomUserDetails readUserOne(Authentication authentication){
        return userService.loadUserByUsername(CustomUserDetails.fromAuthentication(authentication).getLoginId());
    }


    //회원정보 수정
    @PutMapping
    public String updateUserOne(Authentication authentication, @RequestBody CustomUserDetails customUserDetails){
        customUserDetails.setLoginId(CustomUserDetails.fromAuthentication(authentication).getLoginId());
        customUserDetails.setPassword(passwordEncoder.encode(customUserDetails.getPassword()));
        userService.updateUser(customUserDetails);

        return "Update user information successful";
    }

    //프로필 이미지 변경
    @PutMapping("/profile-image")
    public String updateProfileImage(Authentication authentication, @RequestParam("profile-image") MultipartFile profileImage) throws IOException {
        userService.updateProfileImage(authentication, profileImage);
        return "Update profile image successful";
    }


    //회원 탈퇴
    @DeleteMapping
    public String deleteUserOne(Authentication authentication){
        userService.deleteUser(CustomUserDetails.fromAuthentication(authentication).getLoginId());
        //로그아웃처리
        return "Delete user successful";
    }
}
