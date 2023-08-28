package team.closetalk.closet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Getter
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

    // 아이템 수정
    @Builder
    public ClosetItemEntity updateEntity(Map<String, String> itemParams, ClosetEntity closetId) {
        this.modifiedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 필수 및 선택 항목 변경
        this.brand = getValueOrDefault(itemParams, "brand", this.brand);
        this.category = getValueOrDefault(itemParams, "category", this.category);
        this.itemImageUrl = getValueOrDefault(itemParams, "itemImageUrl", this.itemImageUrl);
        this.itemName = getValueOrDefault(itemParams, "itemName", this.itemName);
        this.price = getValueOrDefault(itemParams, "price", this.price);
        this.description = getValueOrDefault(itemParams, "description", this.description);

        this.closetId = closetId;
        return this;
    }

    // T 타입 체크에서 경고 발생
    // -> Front에서 타입을 제대로 받는다면 문제 없을 것으로 예상
    // 그래서 경고 무시 작성
    @SuppressWarnings("unchecked")
    private static <T> T getValueOrDefault(Map<String, String> itemParams, String key, T defaultValue) {
        // itemParams에 key가 존재하는지?
        if (itemParams.containsKey(key)) {
            // Object 타입 -> defaultValue(기존에 있던 값) 타입으로 변환
            Object value = itemParams.get(key);
            // defaultValue is Long type? -> Long 타입 반환
            // -> form-data를 String으로 받아오기 때문에 이런 과정이 필요한 것
            if (defaultValue instanceof Long) return (T) Long.valueOf(value.toString());
            else return (T) value;
        } else {
            // form-data parameter 없으면 기존의 값으로 대체
            return defaultValue;
        }
    }
}
