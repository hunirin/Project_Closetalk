package team.closetalk.closet.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClosetDto {
    private Long id;
    private String closetName;
    private Boolean isHidden;

    public ClosetDto viewCloset() {
        return ClosetDto.builder()
                .closetName(closetName)
                .build();
    }
}
