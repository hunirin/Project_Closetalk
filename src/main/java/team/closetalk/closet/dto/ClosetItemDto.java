package team.closetalk.closet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.closetalk.closet.entity.ClosetItemEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
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

    public static ClosetItemDto viewClosetItem(ClosetItemEntity item) {
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
