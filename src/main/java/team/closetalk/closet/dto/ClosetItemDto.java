package team.closetalk.closet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.closetalk.closet.entity.ClosetItemEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClosetItemDto {
    private Long id;
    private String closetName;
    private Boolean isHidden;
    private ClosetItemEntity closetItem;

    public static ClosetItemDto viewCloset(ClosetItemEntity closetItemEntity) {
        return ClosetItemDto.builder()
                .closetName(closetItemEntity.getClosetId().getClosetName())
                .closetItem(closetItemEntity)
                .build();
    }
}
