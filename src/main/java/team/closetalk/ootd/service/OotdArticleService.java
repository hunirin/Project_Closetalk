package team.closetalk.ootd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.ootd.dto.OotdArticleDto;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.entity.OotdArticleImagesEntity;
import team.closetalk.ootd.repository.OotdArticleImagesRepository;
import team.closetalk.ootd.repository.OotdArticleRepository;
import team.closetalk.user.dto.CustomUserDetails;
import team.closetalk.user.entity.UserEntity;
import team.closetalk.user.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class OotdArticleService {
    private final OotdArticleRepository ootdArticleRepository;
    private final OotdArticleImagesRepository ootdArticleImagesRepository;
    private final UserRepository userRepository;


    private final String OOTD_IMAGE_DIRECTORY = "src/main/resources/static/images/ootd/";

    private String saveOotdImageFile(MultipartFile profileImage, String articleId){
        //이미지 저장할 디렉토리 없으면 생성
        try {
            Files.createDirectories(Path.of(OOTD_IMAGE_DIRECTORY + articleId));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String imagePath = OOTD_IMAGE_DIRECTORY + articleId + "/";

        //이미지를 등록한 경우 imagePath 변경 및 이미지 파일 저장
        if(profileImage != null && !profileImage.isEmpty()){
            //이미지 확장자명 가져오기
            String[] originalFilenameSplit = profileImage.getOriginalFilename().split("\\.");
            String extension = originalFilenameSplit[originalFilenameSplit.length - 1];

            //이미지 저장 + 이미지 명은 uuid로 변환
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss24");
            String savedDatetime = simpleDateFormat.format(Date.from(Instant.now()));
            imagePath = imagePath + UUID.randomUUID() + "_" + savedDatetime + "." + extension;

            try {
                profileImage.transferTo(Path.of(imagePath));
            } catch(IOException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return imagePath;
    }

    public Long createOotdArticle(Authentication authentication, OotdArticleDto ootdArticleDto, List<MultipartFile> imageList) {
        //작성자 정보 가져오기
        UserEntity userEntity = userRepository.findByLoginId(CustomUserDetails.fromAuthentication(authentication).getLoginId()).get();

        //OOTD ARTICLE 저장
        OotdArticleEntity ootdArticleEntity = ootdArticleDto.newEntity();
        ootdArticleEntity.setUserEntity(userEntity);
        OotdArticleEntity savedOotdArticle = ootdArticleRepository.save(ootdArticleEntity);
        ootdArticleEntity.setCreatedAt(LocalDateTime.now());
        ootdArticleEntity.setModifiedAt(LocalDateTime.now());

        //OOTD ARTICLE IMAGE 저장
        String articleId = savedOotdArticle.getId().toString();
        for (int i = 0; i < imageList.size(); i++) {
            String imagePath = saveOotdImageFile(imageList.get(i), articleId);
            OotdArticleImagesEntity imageEntity = new OotdArticleImagesEntity();
            imageEntity.setImageUrl(imagePath);
            imageEntity.setOotdArticle(ootdArticleEntity);
            ootdArticleImagesRepository.save(imageEntity);
            if(i == 0) { //OOTD ARTICLE 썸네일 경로 저장
                savedOotdArticle.setThumbnail(imagePath);
                ootdArticleRepository.save(savedOotdArticle);
            }
        }
        return savedOotdArticle.getId();
    }



    // READ
    // 메인페이지 : 페이지 단위로 조회
    public Page<OotdArticleDto> readOotdPaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").descending());
        Page<OotdArticleEntity> ootdEntityPage = ootdArticleRepository.findAll(pageable);
        return ootdEntityPage.map(OotdArticleDto::fromEntityForList);
    }

    // 상세페이지
    public OotdArticleDto readOotdOne(Long articleId) {
        Optional<OotdArticleEntity> optionalOotd = ootdArticleRepository.findById(articleId);

        if (optionalOotd.isPresent()) return OotdArticleDto.fromEntity(optionalOotd.get());
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    public OotdArticleDto updateOotdArticle(Authentication authentication, Long articleId, OotdArticleDto dto){
        //작성자 정보 일치 여부 확인
        Optional<OotdArticleEntity> savedEntity = ootdArticleRepository.findById(articleId);
        if(savedEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(!savedEntity.get().getUserEntity().getLoginId().equals(CustomUserDetails.fromAuthentication(authentication).getLoginId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        //OOTD ARTICLE 저장
        OotdArticleEntity ootdArticleEntity = savedEntity.get();

        ootdArticleEntity.setContent(dto.getContent());
        ootdArticleEntity.setHashtag(dto.getHashtag());
        ootdArticleEntity.setModifiedAt(LocalDateTime.now());
        ootdArticleRepository.save(ootdArticleEntity);

        return OotdArticleDto.fromEntity(ootdArticleEntity);
    }

    public void deleteOotdArticle(Authentication authentication, Long articleId){
        //작성자 정보 일치 여부 확인
        Optional<OotdArticleEntity> savedEntity = ootdArticleRepository.findById(articleId);
        if(savedEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(!savedEntity.get().getUserEntity().getLoginId().equals(CustomUserDetails.fromAuthentication(authentication).getLoginId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        ootdArticleRepository.deleteById(articleId);
    }

}
