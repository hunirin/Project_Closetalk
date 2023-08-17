package team.closetalk.user.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.user.entity.UserEntity;

import java.util.Collection;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    @Getter
    private Long id;
    @Getter
    private String loginId;
    @Getter
    private String nickname;
    private String password;
    @Getter
    private String email;

    @Getter
    @Setter
    private String profileImageUrl;
    @Getter
    @Setter
    private String social;
    @Getter
    @Setter
    private String userRole;
    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private MultipartFile proifleImage;

    public static CustomUserDetails fromEntity(UserEntity entity){
        return CustomUserDetails.builder()
                .id(entity.getId())
                .loginId(entity.getLoginId())
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
