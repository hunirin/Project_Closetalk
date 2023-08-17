package team.closetalk.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileImageDto {
    MultipartFile profileImage;
}
