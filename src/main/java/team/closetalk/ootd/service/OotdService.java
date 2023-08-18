package team.closetalk.ootd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.ootd.dto.OotdArticleDto;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.entity.OotdArticleImagesEntity;
import team.closetalk.ootd.repository.OotdArticleImagesRepository;
import team.closetalk.ootd.repository.OotdArticleRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@Slf4j
@RequiredArgsConstructor
public class OotdService {
    private final OotdArticleRepository ootdArticleRepository;

    //페이지 단위로 조회
    public Page<OotdArticleDto> readOotdPaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").descending());
        Page<OotdArticleEntity> ootdEntityPage = ootdArticleRepository.findAll(pageable);
        return ootdEntityPage.map(OotdArticleDto::fromEntity);
    }

}
