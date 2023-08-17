package team.closetalk.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;

@Entity
@Data
@DynamicInsert
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;

//    @Column(name = "login_id", unique = true)
    @Column(nullable = false)
    private String loginId;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String password; //암호화해서 저장할 것.
    @Column(unique = true, nullable = false)
    private String email;

    @ColumnDefault(value = "'/src/main/resources/static/images/profile/default_profile.png'")
    private String profileImageUrl;
    private String social;
    @ColumnDefault(value = "'USER'")
    private String userRole;
    private Date createdAt;


}
