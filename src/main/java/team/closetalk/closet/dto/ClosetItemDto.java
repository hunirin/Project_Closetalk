package team.closetalk.closet.dto;

import lombok.*;
import team.closetalk.closet.entity.ClosetItemEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ClosetItemDto {

    private Long id;
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
                .id(item.getId())
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
