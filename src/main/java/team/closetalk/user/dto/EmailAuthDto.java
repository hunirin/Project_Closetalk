package team.closetalk.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailAuthDto {
    String email;
    String authCode;
}
