package team.closetalk.community.dto.article.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

    private List<Long> selectClosetItemNumList;
}