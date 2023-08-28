package team.closetalk.closet.dto;

import lombok.*;
import team.closetalk.closet.entity.ClosetItemEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClosetItemDto {
    private LocalDateTime createdAt;

    private String brand;
    private String category;
    private String itemImageUrl;

    private String itemName;
    private Long price;
    private String description;
    private String closetName;

    // ClosetItemEntity -> ClosetItemDto
    public static ClosetItemDto toClosetItemDto(ClosetItemEntity item) {
        return ClosetItemDto.builder()
                .createdAt(item.getCreatedAt())
                .itemImageUrl(item.getItemImageUrl())
                .category(item.getCategory())
                .brand(item.getBrand())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .description(item.getDescription())
                .closetName(item.getClosetId().getClosetName())
                .build();
    }
}
