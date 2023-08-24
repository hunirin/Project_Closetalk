package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import team.closetalk.closet.dto.ClosetDto;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.service.ClosetService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/closet")
public class ClosetController {
    private final ClosetService closetService;

    // 옷장 목록
    @PostMapping
    public List<ClosetDto> closetList(Authentication authentication) {
        return closetService.findCloset(authentication);
    }

    // 옷장 생성
    @PostMapping("/create")
    public void closetCreate(@RequestParam(name = "closetName", required = false)
                                 String closetName,
                             @RequestParam(name = "isHidden", required = false,
                                     defaultValue = "false") Boolean isHidden,
                             Authentication authentication) {
        closetService.addCloset(closetName, isHidden, authentication);
    }

    // 옷장 업데이트
    @PutMapping("/{closetName}")
    public void closetModify(@PathVariable("closetName") String closetName,
                             @RequestParam(name = "changeName", required = false)
                             String changeName,
                             @RequestParam(name = "isHidden", required = false)
                             Boolean isHidden,
                             Authentication authentication) {
        if (!(changeName == null || changeName.isEmpty()))
            closetService.modifyClosetName(closetName, changeName, authentication);
        if (isHidden != null)
            closetService.modifyClosetHidden(closetName, isHidden, authentication);
    }

    // 옷장 삭제
    @DeleteMapping("/{closetName}")
    public void closetRemove(@PathVariable("closetName") String closetName,
                             Authentication authentication) {
        closetService.removeCloset(closetName, authentication);
    }

    // 해당 옷장 아이템 목록 조회
    @PostMapping("/{closetName}")
    public List<ClosetItemDto> readItems(@PathVariable("closetName") String closetName,
                                         @RequestParam(name = "category", required = false) String category,
                                         Authentication authentication) {
        Optional<String> optionalCategory = Optional.ofNullable(category);
        // 카테고리 입력이 없을 시 전체 목록, 있으면 카테고리 별 목록
        if (optionalCategory.isEmpty()) return closetService.readByCloset(closetName, authentication);
        else return closetService.readByCategory(closetName, optionalCategory.get(), authentication);
    }
}
