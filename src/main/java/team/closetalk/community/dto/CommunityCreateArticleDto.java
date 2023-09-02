package team.closetalk.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.community.enumeration.Category;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommunityCreateArticleDto {
    private Category category;    // 카테고리
    private String title;       // 제목
    private String content;     // 내용

    private List<MultipartFile> communityImages;
}