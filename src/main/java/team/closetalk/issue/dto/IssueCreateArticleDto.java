package team.closetalk.issue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.issue.enumeration.Category;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class IssueCreateArticleDto {
    private Category category;        // 카테고리
    private String title;           // 제목
    private String content;         // 내용
}
