package team.closetalk.ootd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.closetalk.ootd.dto.OotdArticleDto;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.repository.OotdArticleRepository;


@Service
@Slf4j
@RequiredArgsConstructor
public class OotdService {
    private final OotdArticleRepository ootdArticleRepository;

    // 페이지 단위로 조회
    public Page<OotdArticleDto> readOotdPaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").descending());
        Page<OotdArticleEntity> ootdEntityPage = ootdArticleRepository.findAll(pageable);
        return ootdEntityPage.map(OotdArticleDto::fromEntity);
    }

}
