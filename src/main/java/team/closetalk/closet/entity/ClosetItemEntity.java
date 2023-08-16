package team.closetalk.closet.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "closet_item")
public class ClosetItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    // 필수
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String category;
    @Column(name = "item_image_url", nullable = false)
    private String itemImageUrl;

    // 선택
    @Column(name = "item_name")
    private String itemName;
    private Long price;
    private String description;

    @ManyToOne
    @JoinColumn(name = "closet_id")
    private ClosetEntity closetId;
}
