package team.closetalk.issue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class IssueCreateArticleDto {
    private String category;        // 카테고리
    private String title;           // 제목
    private String content;         // 내용
}
