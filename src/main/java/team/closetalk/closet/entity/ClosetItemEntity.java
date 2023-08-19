package team.closetalk.closet.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public ClosetItemEntity updateEntity(Map<String, String> itemParams, ClosetEntity closetId) {
        this.modifiedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.brand = itemParams.get("brand");
        this.category = itemParams.get("category");
        this.itemImageUrl = itemParams.get("itemImageUrl");
        this.itemName = itemParams.get("itemName");
        this.price = Long.valueOf(itemParams.get("price"));
        this.description = itemParams.get("description");
        this.closetId = closetId;
        return this;
    }
}
