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
    @Column(name = "closet_name", unique = true)
    private String closetName;
    @Column(name = "is_hidden")
    private Boolean isHidden;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private UserEntity userId;
}
