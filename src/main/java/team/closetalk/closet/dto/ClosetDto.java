package team.closetalk.closet.dto;

import lombok.*;
import team.closetalk.closet.entity.ClosetEntity;

@Getter
@Builder
public class ClosetDto {
    private String closetName;
    private Boolean isHidden;

    // ClosetEntity -> ClosetDto
    public static ClosetDto toClosetDto(ClosetEntity closet) {
        return ClosetDto.builder()
                .closetName(closet.getClosetName())
                .isHidden(closet.getIsHidden())
                .build();
    }
}