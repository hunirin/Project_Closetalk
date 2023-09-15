package team.closetalk.mypage.entity.dm;

import jakarta.persistence.*;
import lombok.*;
import team.closetalk.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "direct_message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Boolean checked;
    private LocalDateTime timestamp;

    @ToString.Exclude
    @ManyToOne
    private UserEntity sender;

    @ToString.Exclude
    @ManyToOne
    private DMRoom DMRoom;

    public Message(String content, UserEntity user, DMRoom room) {
        this.content = content;
        this.sender = user;
        this.checked = false;
        this.DMRoom = room;
        this.timestamp = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}