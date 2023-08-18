package team.closetalk.closet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.closetalk.closet.entity.ClosetEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClosetDto {
    private String closetName;
    private Boolean isHidden;

    public static ClosetDto viewCloset(ClosetEntity closet) {
        return ClosetDto.builder()
                .closetName(closet.getClosetName())
                .isHidden(closet.getIsHidden())
                .build();
    }

    public ClosetEntity newCloset(String closetName, Boolean isHidden) {
        ClosetEntity closet = new ClosetEntity();
        closet.setClosetName(closetName);
        closet.setIsHidden(isHidden);
        return closet;
    }
}