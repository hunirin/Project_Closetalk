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
    private final OotdArticleImagesRepository ootdArticleImagesRepository;


    // POST
    // 피드 생성
    public OotdArticleDto createOotd(OotdArticleDto ootdArticleDto) {
        OotdArticleEntity entity = new OotdArticleEntity();
        entity.setContent(ootdArticleDto.getContent());
        entity.setHashtag(ootdArticleDto.getHashtag());
//        entity.setCreatedAt(ootdArticleDto.getCreatedAt());
        return ootdArticleDto.newEntity(ootdArticleRepository.save(entity));
    }


    // READ
    // 커서 페이지네이션을 통한 무한 스크롤 => no offset
//    public Page<OotdArticleDto> readOotdPagedWithCursor(Long cursor, Integer pageSize) {
//        Pageable pageable;
//        if (cursor == null) {
//            pageable = PageRequest.of(0, pageSize, Sort.by("id").ascending());
//        } else {
//            pageable = PageRequest.of(0, pageSize, Sort.by("id").ascending());
//        }
//
//        Page<OotdArticleEntity> ootdEntityPage = ootdArticleRepository.findByCursor(cursor, pageable);
//        Page<OotdArticleDto> ootdDtoPage = ootdEntityPage.map(OotdArticleDto::fromEntity);
//        return ootdDtoPage;
//    }
    //페이지 단위로 조회
    public Page<OotdArticleDto> readOotdPaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").descending());
        Page<OotdArticleEntity> ootdEntityPage = ootdArticleRepository.findAll(pageable);
        return ootdEntityPage.map(OotdArticleDto::fromEntity);
    }

    // POST
    // 피드 이미지 등록
    public void uploadOotdImage(
            Long id, MultipartFile ootdImage
    ) {

        OotdArticleEntity ootdArticleEntity = getOotdArticleById(id);

        // 폴더명에 articleId를 넣기 위해
        String ootdArticleId = String.valueOf(ootdArticleEntity.getId());

        // 저장되는 시간을 파일명으로
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String currentTimeStr = currentTime.format(formatter);


        // 업로드위치
        // media/article_번호
        String articleImageDir = String.format("src/main/resources/static/ootd/image/article_%s/", ootdArticleId); // 폴더명
        try { // 읽고 쓰는데서 발생할 수 있는 예외 처리
            Files.createDirectories(Path.of(articleImageDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 확장자를 포함한 이미지 이름
        String originalFilename = ootdImage.getOriginalFilename();
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String articleImageFilename = currentTimeStr + "." + extension;
        log.info(articleImageFilename);

        // 폴더와 이미지 이름을 포함한 파일 경로
        String articleImagePath = String.format("../../static/ootd/image/article_%s/", ootdArticleId) + articleImageFilename; // 파일 경로
        log.info(articleImagePath);

        // MultipartFile을 저장
        try {
            ootdImage.transferTo(Path.of(articleImagePath));

            // 파일이 있는 위치를 imageUrl에 저장
            OotdArticleImagesEntity imgEntity = new OotdArticleImagesEntity();
            imgEntity.setImageUrl(articleImagePath);
            imgEntity.setOotdArticle(ootdArticleEntity);
            if (ootdArticleEntity.getThumbnail() == null) {
                ootdArticleEntity.setThumbnail(articleImagePath);
            }

            ootdArticleImagesRepository.save(imgEntity);

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 피드 찾는 메소드
    public OotdArticleEntity getOotdArticleById(Long id) {
        return ootdArticleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
