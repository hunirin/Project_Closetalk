package team.closetalk.closet.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "closet")
public class ClosetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String closetName;
    private Boolean isHidden;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private UserEntity userId;
}
