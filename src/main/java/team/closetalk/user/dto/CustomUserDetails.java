package team.closetalk.user.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.user.entity.UserEntity;

import java.util.Collection;
import java.util.Date;

@Data
@Builder
public class CustomUserDetails implements UserDetails {

    private Long id;

    private String loginId;
    private String nickname;
    private String password;
    private String email;

    private String profileImageUrl;
    private String social;
    private String userRole;
    private Date createdAt;

    private MultipartFile profileImage;

    public static CustomUserDetails fromEntity(UserEntity entity){
        return CustomUserDetails.builder()
                .id(entity.getId())
                .loginId(entity.getLoginId())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .email(entity.getEmail())
                .profileImageUrl(entity.getProfileImageUrl())
                .social(entity.getSocial())
                .userRole(entity.getUserRole())
                .createdAt(entity.getCreatedAt())
                .build();
    }
    public UserEntity newEntity() {
        UserEntity entity = new UserEntity();
        entity.setLoginId(loginId);
        entity.setPassword(password);
        entity.setNickname(nickname);
        entity.setEmail(email);
        entity.setProfileImageUrl(profileImageUrl);
        entity.setSocial(social);
        entity.setUserRole(userRole);
        entity.setCreatedAt(createdAt);
        return entity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
