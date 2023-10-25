package team.closetalk.closet.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.dto.ClosetDto;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.entity.ClosetEntity;
import team.closetalk.closet.entity.ClosetItemEntity;
import team.closetalk.closet.repository.ClosetItemRepository;
import team.closetalk.closet.repository.ClosetRepository;
import team.closetalk.user.entity.UserEntity;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClosetService {
    private final ClosetRepository closetRepository;
    private final ClosetItemRepository closetItemRepository;
    private final EntityRetrievalService entityRetrievalService;

    // 1. 옷장 목록 조회(이름, 공개 여부)
    public List<ClosetDto> findCloset(Authentication authentication) {
        List<ClosetEntity> closetEntities =
                closetRepository.findAllByUserId_LoginId(authentication.getName());
        if (closetEntities.isEmpty()) {
            addCloset("My Closet 1", true, authentication);
            closetEntities = closetRepository.findAllByUserId_LoginId(authentication.getName());
        }
        log.info("가지고 있는 옷장 목록 조회 완료");
        return closetEntities.stream().map(ClosetDto::toClosetDto).toList();
    }

    // 1-1. 옷장 생성
    public void addCloset(String closetName, Boolean isHidden,
                          Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());

        if (closetRepository.findAllByUserId_LoginId(user.getLoginId()).size() == 5) {
            log.error("옷장 최대 생성 개수 제한");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        
        // 옷장 이름이 없으면 Closet (마지막 생성 옷장의 ClosetId + 1)으로 생성
        String newClosetName;
        if (closetName == null || closetName.equals("")) {
            int nextClosetNumber = getNextClosetNumber();
            newClosetName = "My Closet " + nextClosetNumber;
        } else newClosetName = closetName;

        closetRepository.save(new ClosetEntity(newClosetName, isHidden, user));
        log.info("옷장 생성 완료");
    }

    // 1-1. My Closet + count 자동 생성
    public int getNextClosetNumber() {
        String baseName = "My Closet ";
        int maxNumber = 0;

        List<ClosetEntity> closetEntities =
                closetRepository.findAllByClosetNameStartingWith(baseName);
        for (ClosetEntity closetEntity : closetEntities) {
            String existingName = closetEntity.getClosetName();
            String numberString = existingName.substring(baseName.length()).trim();
            try {
                int number = Integer.parseInt(numberString);
                if (number > maxNumber) {
                    maxNumber = number;
                }
            } catch (NumberFormatException e) {
                // 숫자 아닌 경우 무시
            }
        }
        return maxNumber + 1;
    }

    // 1-2. 옷장 삭제 (해당 옷장 내 모든 아이템 포함)
    @Transactional // 메서드 내 모든 작업 중 하나라도 실패 시 전체 작업 취소
    public void removeCloset(String closetName, Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());
        ClosetEntity closet = getClosetEntity(closetName, user.getNickname());

        // 해당 옷장 내 모든 아이템 삭제
        closetItemRepository.deleteAllByClosetId(closet.getId());
        closetItemRepository.deleteClosetItemByClosetId(closet.getId());

        // 해당 옷장 삭제
        closetRepository.deleteById(closet.getId());
        log.info("{} 삭제 완료", closetName);
    }

    // 1-3. 옷장 이름 수정
    public void modifyClosetName(String closetName, String changeName,
                                 Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());
        ClosetEntity closet = getClosetEntity(closetName, user.getNickname());

        if (closet.getClosetName().equals(changeName)) {
            log.error("이전 이름과 동일");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        closetRepository.save(closet.updateEntity(changeName));
        log.info("{}으로 이름 변경 완료", changeName);
    }

    // 1-4. 옷장 공개 여부 수정
    public void modifyClosetHidden(String closetName,
                                   Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());
        ClosetEntity closet = getClosetEntity(closetName, user.getNickname());
        if (closet.getIsHidden()) {
            closetRepository.save(closet.updateEntity(false));
            log.info("{} : 비공개 설정", closet.getClosetName());
        }
        else {
            closetRepository.save(closet.updateEntity(true));
            log.info("{} : 공개 설정", closet.getClosetName());
        }
    }

    // 2. 해당하는 닉네임의 유저 옷장 아이템 목록 조회
    public List<ClosetItemDto> readByCloset(String nickname, String closetName,
                                            Authentication authentication) {
        UserEntity user = getUserEntityByNickname(nickname);
        ClosetEntity closet = getClosetEntity(closetName, user.getNickname());

        if (user == getUserEntity(authentication.getName()) || closet.getIsHidden()) {
            List<ClosetItemEntity> itemEntities =
                    closetItemRepository.findAllByClosetId_Id(closet.getId());
            log.info("{}의 아이템 목록 조회 완료", closet.getClosetName());
            return itemEntities.stream().map(ClosetItemDto::toClosetItemDto).toList();
        } else {
            log.info("비공개 옷장입니다.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    // 3. 해당하는 닉네임의 유저 옷장 카테고리 별 아이템 목록 조회
    public List<ClosetItemDto> readByCategory(String nickname, String closetName, String category,
                                              Authentication authentication) {
        UserEntity user = getUserEntityByNickname(nickname);
        ClosetEntity closet = getClosetEntity(closetName, user.getNickname());

        if (user == getUserEntity(authentication.getName()) || closet.getIsHidden()) {
            List<ClosetItemEntity> itemEntities =
                    closetItemRepository.findAllByClosetId_IdAndCategory(closet.getId(), category);
            log.info("{}의 {} 별 아이템 목록 조회 완료", closet.getClosetName(), category);
            return itemEntities.stream().map(ClosetItemDto::toClosetItemDto).toList();
        } else {
            log.info("비공개 옷장입니다.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    // 4. 모든 아이템 조회
    public List<ClosetItemDto> readAllItem(String nickname,
                                           Authentication authentication) {
        UserEntity user = getUserEntityByNickname(nickname);
        if (user == getUserEntity(authentication.getName())) {
            List<ClosetItemEntity> itemEntities =
                    closetItemRepository.findAllByClosetId_UserId_Nickname(nickname);
            log.info("{}의 아이템 목록 조회 완료", nickname);
            return itemEntities.stream().map(ClosetItemDto::toClosetItemDto).toList();
        } else {
            log.info("잘못된 접근입니다.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    // closetId로 해당 ClosetEntity 찾기
    private ClosetEntity getClosetEntity(String closetName, String nickName) {
        return entityRetrievalService.getClosetEntity(closetName, nickName);
    }

    // LoginId == authentication.getName() 사용자 찾기
    private UserEntity getUserEntity(String LoginId) {
        return entityRetrievalService.getUserEntity(LoginId);
    }

    // nickname == authentication.getName() 사용자 찾기
    private UserEntity getUserEntityByNickname(String nickname) {
        return entityRetrievalService.getUserEntityByNickname(nickname);
    }
}
