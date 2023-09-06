package team.closetalk.ootd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.ootd.dto.OotdArticleDto;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.repository.OotdArticleRepository;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class OotdArticleService {
    private final OotdArticleRepository ootdArticleRepository;


    // READ
    // 메인페이지 : 페이지 단위로 조회
    public Page<OotdArticleDto> readOotdPaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").descending());
        Page<OotdArticleEntity> ootdEntityPage = ootdArticleRepository.findAll(pageable);
        return ootdEntityPage.map(OotdArticleDto::readOotdAll);
    }

    // 상세페이지
    public OotdArticleDto readOotdOne(Long articleId) {
        Optional<OotdArticleEntity> optionalOotd = ootdArticleRepository.findById(articleId);

        if (optionalOotd.isPresent()) return OotdArticleDto.readOotdOne(optionalOotd.get());
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
