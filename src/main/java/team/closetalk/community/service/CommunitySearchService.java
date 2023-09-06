package team.closetalk.community.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.closetalk.community.dto.article.response.CommunityArticleListDto;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.repository.CommunitySearchRepository;

@Data
@Service
@RequiredArgsConstructor
public class CommunitySearchService {
    private final CommunitySearchRepository communitySearchRepository;

    // 커뮤니티 게시물 전체 검색
    public Page<CommunityArticleListDto> searchCommunityPaged(String contentKeyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<CommunityArticleEntity> communityEntityPage =
                communitySearchRepository.findAllByTitleOrContentOrUserId_NicknameContaining(contentKeyword, pageable);

        return communityEntityPage.map(CommunityArticleListDto::fromEntity);
    }

    // 커뮤니티 게시물 제목 검색
    public Page<CommunityArticleListDto> searchTitleCommunityPaged(String titleKeyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<CommunityArticleEntity> communityEntityPage =
                communitySearchRepository.findAllByTitleContaining(titleKeyword, pageable);
        return communityEntityPage.map(CommunityArticleListDto::fromEntity);
    }

        // 커뮤니티 게시물 닉네임 검색
        public Page<CommunityArticleListDto> searchNicknameCommunityPaged (String nicknameKeyword, Integer pageNum, Integer pageSize){
            Pageable pageable = PageRequest.of(
                    pageNum, pageSize, Sort.by("id").ascending());

            Page<CommunityArticleEntity> communityEntityPage =
                    communitySearchRepository.findAllByUserId_NicknameContaining(nicknameKeyword, pageable);
            return communityEntityPage.map(CommunityArticleListDto::fromEntity);
    }
}

