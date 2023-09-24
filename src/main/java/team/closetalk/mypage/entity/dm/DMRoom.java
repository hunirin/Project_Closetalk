package team.closetalk.mypage.entity.dm;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "direct_message_room")
public class DMRoom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<DMUser> dmUsers = new ArrayList<>();

    @OneToMany(mappedBy = "DMRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    private LocalDateTime latestMessageTimestamp;

    public void addMessage(Message message) {
        messages.add(message);
        message.setDMRoom(this);
        // 최신 메시지 타임스탬프 업데이트
        if (latestMessageTimestamp == null || message.getTimestamp().isAfter(latestMessageTimestamp)) {
            latestMessageTimestamp = message.getTimestamp();
        }
    }
}
