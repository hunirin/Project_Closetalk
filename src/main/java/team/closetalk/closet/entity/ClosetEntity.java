package team.closetalk.closet.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "closet")
public class ClosetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "closet_name", unique = true)
    private String closetName;
    @Column(name = "is_hidden")
    private Boolean isHidden;

    // 새로운 옷장 생성
    public ClosetEntity(String closetName, Boolean isHidden) {
        this.closetName = closetName;
        this.isHidden = isHidden;
    }

    // 옷장 이름 변경
    public ClosetEntity updateEntity(String closetName) {
        this.closetName = closetName;
        return this;
    }

    // 옷장 공개 여부 수정
    public ClosetEntity updateEntity(Boolean isHidden) {
        this.isHidden = isHidden;
        return this;
    }
}
